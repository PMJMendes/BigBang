package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.AlignType;
import org.apache.ecs.GenericElement;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Strong;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.PolicyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

/**
 * Class responsible for the creation of the Client's Portfolio's Report
 */
public class PolicyPortfolioClient extends PolicyListingsBase {

	/**
	 * Inner class which holds the coverages, insured values, risk site an taxes
	 * to display in the report in a "as general purpose as possible" way
	 */
	private class CoverageData {

		private ArrayList<String> coverages;
		private ArrayList<String> insuredValues;
		private ArrayList<String> riskSite;
		private ArrayList<String> taxes;
		private ArrayList<String> franchises;

		CoverageData() {
			coverages = new ArrayList<String>();
			insuredValues = new ArrayList<String>();
			riskSite = new ArrayList<String>();
			taxes = new ArrayList<String>();
			franchises = new ArrayList<String>();
		}

		public ArrayList<String> getCoverages() {
			return coverages;
		}

		public ArrayList<String> getInsuredValues() {
			return insuredValues;
		}

		public ArrayList<String> getRiskSite() {
			return riskSite;
		}

		public ArrayList<String> getTaxes() {
			return taxes;
		}
		
		public ArrayList<String> getFranchises() {
			return franchises;
		}

		public void setCoverages(PolicyCoverage[] policyCoverages) {

			if (policyCoverages == null || policyCoverages.length == 0) {
				coverages.add(" ");
			}
			for (int i = 0; i < policyCoverages.length; i++) {
				// Only inserts the coverages present at the policy
				if (policyCoverages[i].IsPresent()!=null && policyCoverages[i].IsPresent()) {
					coverages.add((String) policyCoverages[i].GetCoverage()
							.getAt(Coverage.I.NAME));
				}
			}
		}

		public void setCoverages(String coverage) {
			coverages.add(coverage);
		}

		public void setRiskSite(String site) {
			riskSite.add(site);
		}

		public void setInsuredValues(ArrayList<String> values) {
			insuredValues = values;
		}

		public void setTaxes(ArrayList<String> taxesList) {
			taxes = taxesList;
		}
		
		public void setFranchises(ArrayList<String> franchisesList) {
			franchises = franchisesList;
		}
	}

	// The total premium to display in the report
	private BigDecimal premiumTotal;

	// A "Faux-id" used when there is no insured object associated with a policy
	private UUID fauxId;

	// Width Constants
	private static final int POLICY_WIDTH = 100;
	private static final int COMPANY_WIDTH = 200;
	private static final int DATE_WIDTH = 40;
	private static final int FRACTIONING_WIDTH = 70;
	private static final int OBJECT_WIDTH = 200;
	private static final int RISK_SITE_WIDTH = 230;
	private static final int COVERAGES_WIDTH = 230;
	private static final int VALUE_WIDTH = 92;
	private static final int TAX_WIDTH = 40;
	private static final int FRANCHISE_WIDTH = 40;
	private static final int PREMIUM_WIDTH = 92;
	private static final int METHOD_WIDTH = 90;
	private static final int OBSERVATIONS_WIDTH = 80;
	private static final int STRING_BREAK_POINT = 30;
	private static final int METHOD_BREAK_POINT = 18;
	
	// Height
	private static final int INNER_HEIGHT = 70;
	
	// Payment Methods
	private static final String DIRECT_PAYMENT = "Débito Directo";
	private static final String BROKER_PAYMENT = "Pagamento ao Corretor";
	
	// Fractioning
	public static final String FRAC_YEAR     	= "Anual";
	public static final String FRAC_SEMESTER 	= "Semestral";
	public static final String FRAC_TRIMESTER  	= "Trimestral";
	public static final String FRAC_MONTH    	= "Mensal";
	public static final String FRAC_UNIQUE  	= "Única";
	public static final String FRAC_FRACTIONED 	= "Fraccionada";
	
	/*
	 * This Matrix represents the order to display the policies, as well as the way to group them.
	 * Here's how it works:
	 * 		- Each position in the array has an array defining what policies should be displayed in that section in the report.
	 * 		- This grouping can consider the policy category, line or subline.
	 * 		- When creating an hashmap with the policies (method getPoliciesMap(Policy[] policies)), we try to 
	 * 		insert the policies with subline+line+category. If it is not possible, we include subline+line.
	 * 		If it is still not possile, the policy is included with a key with only the category.
	 * 		- When creating the report, this matrix is iterated, and gets the policies with the key as described here, hence 
	 * 		displaying them with this order.
	 */
	private static final String[][] ORDERED_SUBLINES = {
			{ Constants.PolicyCategories.WORK_ACCIDENTS.toString() }, // "Acidentes de Trabalho"
			{ Constants.PolicyCategories.PERSONAL_ACCIDENTS.toString() }, // "Acidentes Pessoais"
			{ Constants.PolicyCategories.DISEASE.toString() }, // "Saúde"
			{ Constants.PolicyCategories.DIVERS.toString()
					+ Constants.PolicyLines.DIVERS_ASSISTANCE.toString() }, // "Assistência"
			{ Constants.PolicyCategories.LIFE.toString() }, // "Vida"
			{ Constants.PolicyCategories.DIVERS.toString() }, // "Diversos"
			{ Constants.PolicyCategories.AUTOMOBILE.toString() }, // "Automóvel e Frota"
			{ Constants.PolicyCategories.MULTIRISK.toString() }, // "Multirriscos"
			{ Constants.PolicyCategories.MULTIRISK.toString()
					+ Constants.PolicyLines.MULTIRISK_ALLRISKS.toString() }, // "All Risks"
			{
					Constants.PolicyCategories.OTHER_DAMAGES.toString()
							+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_BREAKDOWN,
					Constants.PolicyCategories.OTHER_DAMAGES.toString()
							+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL,
					Constants.PolicyCategories.OTHER_DAMAGES.toString()
							+ Constants.PolicyLines.OTHER_DAMAGES_CEASING
							+ Constants.PolicySubLines.OTHER_DAMAGES_CEASING_MACHINES }, // "Máquinas , Avaria de Máquinas e Máquinas de Casco"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString() }, // "Outras
																		// perdas
			{ Constants.PolicyCategories.CONSTRUCTION_ASSEMBLY.toString() }, // "Obras e Montagens"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString()
					+ Constants.PolicyLines.OTHER_DAMAGES_LEASING.toString() }, // "Bens em Leasing"
			{ Constants.PolicyCategories.TRANSPORTED_GOODS.toString() }, // "Mercadorias Transportadas"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_GENERAL.toString() }, // "Responsabilidade Civil Geral"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_HUNTERS.toString() }, // "Rep. Civil Caçador"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_FAMILY.toString() }, // "Resp. Civil Familiar"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_ENVIRONMENTAL
							.toString() }, // "Responsabilidade Ambiental"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_PRODUCTS.toString() }, // "Resp. Civil Produtos"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_PROFESSIONAL
							.toString() }, // "Resp. Civil Profissional"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString()
					+ Constants.PolicyLines.RESPONSIBILITY_EXPLORATION
							.toString() }, // "Resp. Civil Exploração"
			/*
			 * From this point forward are the ones not defined by EGS, so there
			 * is no special order
			 */
			{ Constants.PolicyCategories.FIRE.toString() }, // "Incêndio"
			{ Constants.PolicyCategories.AGRICULTURAL.toString() }, // "Agrícola"
			{ Constants.PolicyCategories.RESPONSIBILITY.toString() }, // "Responsabilidade"
			{ Constants.PolicyCategories.MARINE_VESSELS.toString() }, // "Embarcações Marítimas"
			{ Constants.PolicyCategories.RAIL_VEHICLES.toString() }, // "Veículos Ferroviários"
			{ Constants.PolicyCategories.AIRCRAFTS.toString() }, // "Aeronaves"
			{ Constants.PolicyCategories.RETIREMENT_FUND.toString() } // "Fundo de Pensões"
	};
	
	/*
	 * This HashMap is used to allow the translation for the title of the groupings displayed at the report.
	 * It can use as key a concatenation of Category, Subline and Line or Category and Line. Or simply Category.
	 * If one key is not represented in this Hashmap, it means that we should display the Category's label.
	 */
	private static final HashMap<String, String> CATEGORY_TRANSLATOR;
	static {
		CATEGORY_TRANSLATOR = new HashMap<String, String>();
		CATEGORY_TRANSLATOR.put(Constants.PolicyCategories.DISEASE.toString(),
				"Saúde");
		CATEGORY_TRANSLATOR.put(Constants.PolicyCategories.DIVERS.toString()
				+ Constants.PolicyLines.DIVERS_ASSISTANCE.toString(),
				"Assistência");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.AUTOMOBILE.toString(),
				"Automóvel e Frota");
		CATEGORY_TRANSLATOR.put(Constants.PolicyCategories.MULTIRISK.toString()
				+ Constants.PolicyLines.MULTIRISK_ALLRISKS.toString(),
				"All Risks");
		CATEGORY_TRANSLATOR
				.put(Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_BREAKDOWN,
						"Máquinas , Avaria de Máquinas e Máquinas de Casco");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL,
				"Máquinas , Avaria de Máquinas e Máquinas de Casco");
		CATEGORY_TRANSLATOR
				.put(Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_CEASING
						+ Constants.PolicySubLines.OTHER_DAMAGES_CEASING_MACHINES,
						"Máquinas , Avaria de Máquinas e Máquinas de Casco");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString(),
				"Outras perdas");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_LEASING
								.toString(), "Bens em Leasing");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_GENERAL
								.toString(), "Responsabilidade Civil Geral");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_HUNTERS
								.toString(), "Rep. Civil Caçador");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_FAMILY
								.toString(), "Resp. Civil Familiar");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_ENVIRONMENTAL
								.toString(), "Responsabilidade Ambiental");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_PRODUCTS
								.toString(), "Resp. Civil Produtos");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_PROFESSIONAL
								.toString(), "Resp. Civil Profissional");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.RESPONSIBILITY.toString()
						+ Constants.PolicyLines.RESPONSIBILITY_EXPLORATION
								.toString(), "Resp. Civil Exploração");
	}

	/**
	 * The method responsible for creating the report
	 */
	public GenericElement[] doReport(String[] reportParams)
			throws BigBangJewelException {

		Policy[] policies;
		GenericElement[] reportResult;

		// Gets the policies from the DB
		policies = getPortfolio(reportParams);

		// Creates the report (this one only has one section)
		reportResult = new GenericElement[1];
		reportResult[0] = buildClientPortfolio(policies, reportParams);

		return reportResult;
	}

	/**
	 * The method responsible for getting the data needed by the report
	 */
	private Policy[] getPortfolio(String[] reportParams)
			throws BigBangJewelException {

		StringBuilder strSQL;
		ArrayList<Policy> policies;
		IEntity policyEntity;
		MasterDB ldb;
		ResultSet fetchedPolicies;
		UUID guidAgent;
		
		// Filters by client (if defined in the interface)
		if (reportParams[0] == null && reportParams[1] == null) {
			throw new BigBangJewelException("Deve definir-se o Cliente ou Grupo de Clientes a que o relatório se refere");
		}

		try {

			// Gets the entity responsible for controlling policies, in the
			// current namespace
			policyEntity = Entity.GetInstance(Engine.FindEntity(
					Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			// Starts building the SQL used. Initially, only filtering valid
			// policies
			strSQL = new StringBuilder();
			strSQL.append("SELECT * FROM ("
					+ policyEntity
					.SQLForSelectByMembers(
							new int[] { Policy.I.STATUS },
							new java.lang.Object[] { Constants.StatusID_Valid },
							null) + ") [AuxPol] WHERE 1=1");

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Filters by client (if defined in the interface)
		if (reportParams[0] != null) {
			filterByClient(strSQL, UUID.fromString(reportParams[0]));
		}

		// Filters by client group (if defined in the interface)
		if (reportParams[1] != null) {
			filterByClientGroup(strSQL, UUID.fromString(reportParams[1]));
		}
		
		// Filters the temporary policies
		if (reportParams[15].equals("0")) {
			filterOngoingPolicies(strSQL);
		}

		// If the current user is an "agent", he only sees the corresponding
		// policies
		guidAgent = Utils.getCurrentAgent();
		if (guidAgent != null) {
			filterByAgent(strSQL, guidAgent);
		}

		policies = new ArrayList<Policy>();

		try {
			ldb = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Fetches the policies from the DB
		try {
			fetchedPolicies = ldb.OpenRecordset(strSQL.toString());
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Iterates the policies and adds to the result
		try {
			while (fetchedPolicies.next()) {
				policies.add(Policy.GetInstance(Engine.getCurrentNameSpace(),
						fetchedPolicies));
			}
		} catch (Throwable e) {
			try {
				fetchedPolicies.close();
			} catch (SQLException e1) {
			}
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Cleanup
		try {
			fetchedPolicies.close();
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return policies.toArray(new Policy[policies.size()]);
	}
	
	/**
	 * This method adds a clause to filter only the ongoing policies
	 */
	private void filterOngoingPolicies(StringBuilder strSQL) {
		strSQL.append(" AND [DURATION] ='" + Constants.DurID_Ongoing + "'");		
	}

	/**
	 * The method responsible for printing the report including the header
	 * Calls the method which prints the policies' info
	 */
	private Table buildClientPortfolio(Policy[] policies, String[] reportParams)
					throws BigBangJewelException {

		Table table;
		TR[] tableRows = new TR[3];
		
		// Builds the header row
		TD headerContent = new TD();
		headerContent.addElement(buildHeader(reportParams));
		tableRows[0] = ReportBuilder.buildRow(new TD[] { headerContent });
		
		// Builds the row with the policy info
		TD infoContent = new TD();
		infoContent.addElement(buildClientPortfolioTable(policies, reportParams));
		tableRows[1] = ReportBuilder.buildRow(new TD[] { infoContent });

		// Builds the row with the policy info
		TD notes = new TD();
		notes.addElement(buildNotesTable(reportParams));
		tableRows[2] = ReportBuilder.buildRow(new TD[] { notes });
		
		table = ReportBuilder.buildTable(tableRows);
		
		return table;
	}

	/**
	 * This method builds the report's header, with a credite's logo, the report's title
	 * and the date the report was extracted
	 */
	private Table buildHeader(String[] reportParams) throws BigBangJewelException {
		
		Table table;
		TR[] tableRows = new TR[1];
		TD[] cells = new TD[3];
		
		String clientOrGroupTitle = getClientOrGroup(reportParams);
		
		// Builds the cell with the header image
		TD imageContent = new TD();
		imageContent.addElement(getImage());
		imageContent.setAlign(AlignType.left);
		imageContent.setStyle("width:10%; padding-bottom:15px;");
		cells[0] = imageContent;
			
		// Builds the cell with the report title and client name
		TD title = new TD();
		Strong titleStrong = new Strong("Carteira de Seguros de " + clientOrGroupTitle);
		titleStrong.setStyle("font-size: 15px;");
		title.addElement(titleStrong);
		title.setAlign(AlignType.middle);
		title.setStyle("padding-top:30px;");
		cells[1] = title;		
				
		// Builds the cell with the date the report was extracted
		TD dateTd = new TD();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Strong dateStrong = new Strong(dateFormat.format(date).toString());
		dateStrong.setStyle("font-size: 15px;");
		dateTd.addElement(dateStrong);
		dateTd.setAlign(AlignType.right);
		dateTd.setStyle("width:10%; padding-top:80px;");
		cells[2] = dateTd;	
		
		tableRows[0] = ReportBuilder.buildRow(cells);
		tableRows[0].setStyle("height:135px; font-weight:bold;");
		
		table = ReportBuilder.buildTable(tableRows);
		table.setStyle("width:100%;");
		
		return table;
	}

	/**
	 * This method builds the report section with the labels corresponding
	 * to the column's names
	 */
	private Table buildNotesTable(String[] reportParams) throws BigBangJewelException {
		
		Table table;
		TR[] tableRows;
		
		int paramCount = 0;
		String[] columnLabels = new String[4];
		String[] columnFull = new String[4];
		
		if (reportParams[4].equals("1")) {
			columnLabels[paramCount] = "Venc. (M / D)";
			columnFull[paramCount++] = "Vencimento (Mês / Dia)";
		}
		if (reportParams[5].equals("1")) {
			columnLabels[paramCount] = "Frac.";
			columnFull[paramCount++] = "Fraccionamento";
		}
		if (reportParams[10].equals("1")) {
			columnLabels[paramCount] = "Taxa Com.";
			columnFull[paramCount++] = "Taxa Comercial";
		}
		if (reportParams[11].equals("1")) {
			columnLabels[paramCount] = "Franq.";
			columnFull[paramCount++] = "Franquia";
		}
		
		tableRows = new TR[paramCount+1];
		
		if (paramCount>0) {
			TD[] notesHeader = new TD[1];
			notesHeader[0] = ReportBuilder.buildCell("Notas", TypeDefGUIDs.T_String, false);
			notesHeader[0].setStyle("padding-top:20px;padding-left:5px;width:100px;");
			tableRows[0] = ReportBuilder.buildRow(notesHeader);
			tableRows[0].setStyle("height:50px;font-weight:bold;");
		}
		
		for (int i=0; i<paramCount; i++) {
			tableRows[i+1] = constructSummaryRow(columnLabels[i],
					columnFull[i], TypeDefGUIDs.T_String, false, false);
		}
		
		table = ReportBuilder.buildTable(tableRows);
		table.setStyle("width:100%;");
		
		return table;
	}
	
	/**
	 * The method responsible for printing the table corresponding to a client's
	 * portfolio
	 */
	private Table buildClientPortfolioTable(Policy[] policies, 
			String[] reportParams)
					throws BigBangJewelException {

		Table table;
		TR[] tableRows;
		TD mainContent;
		int rowNum = 0;
		HashMap<String, ArrayList<Policy>> policiesMap;

		tableRows = new TR[3];
		premiumTotal = BigDecimal.ZERO;
		fauxId = UUID.randomUUID();

		// Creates a map with the policies separated by "ramo"
		policiesMap = getPoliciesMap(policies);

		// Builds the row corresponding to the "real content" of the report -
		// meaning... the values from the policies
		mainContent = new TD();
		mainContent.setColSpan(2);
		mainContent.addElement(buildReportInfo(policiesMap, policies.length,
				reportParams));
		ReportBuilder.styleInnerContainer(mainContent);
		tableRows[rowNum++] = ReportBuilder.buildRow(new TD[] { mainContent });

		// Builds the row with the total number of policies
		tableRows[rowNum++] = constructSummaryRow("Nº de Apólices:",
				policies.length, TypeDefGUIDs.T_Integer, true, false);

		// Build the row with the total prize
		if (reportParams[10].equals("1")) {
			tableRows[rowNum++] = constructSummaryRow(
					"Total de Prémios:", premiumTotal, TypeDefGUIDs.T_Decimal,
					true, false);
		}

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, false);

		return table;
	}
	
	/**
	 * This method builds a "summary row" with info to display at the end of the report.
	 * It is (REALLY) similar to ReportBuilder's constructDualRow method, but without the 
	 * left line. Eventually new changes will be made to this method, thus motivating this
	 * "branching"
	 */
	private TR constructSummaryRow(String text, 
			Object value, UUID typeGUID, boolean topRow, boolean rightAlign) {
		
		TD[] cells = new TD[2];
		TR row;

		cells[0] = ReportBuilder.buildHeaderCell(text);
		cells[0].setWidth("1px");
		ReportBuilder.styleCell(cells[0], topRow, false);
		cells[1] = ReportBuilder.buildCell(value, typeGUID);
		ReportBuilder.styleCell(cells[1], topRow, false);
		if (rightAlign) {
			cells[1].setAlign("right");
		}
		row = ReportBuilder.buildRow(cells);
		ReportBuilder.styleRow(row, false);

		return row;
	}

	/**
	 * This method creates an HashMap with policies grouped by "ramo"
	 */
	private HashMap<String, ArrayList<Policy>> getPoliciesMap(Policy[] policies) {

		HashMap<String, ArrayList<Policy>> policiesMap;
		String policyKey;
		policiesMap = new HashMap<String, ArrayList<Policy>>();

		for (int i = 0; i < policies.length; i++) {
			policyKey = getPolicyKey(policies[i]);
			if (policiesMap.get(policyKey) == null) {
				policiesMap.put(policyKey, new ArrayList<Policy>());
			}
			policiesMap.get(policyKey).add(policies[i]);
		}

		return policiesMap;
	}

	/**
	 * This method gets the key used to insert the policy in the policies' hash map.
	 * This method takes into consideration the ordering defined in ORDERED_SUBLINES' array.
	 * It tries to group the policies with category + subline + line. If not possible, tries with
	 * category + subline. If it is still not possible, it inserts the policy considering only 
	 * category.
	 */
	private String getPolicyKey(Policy policy) {
		
		String policyCategoryKey = policy.GetSubLine().getLine().getCategory().getKey().toString();
		String policyLineKey = policyCategoryKey + policy.GetSubLine().getLine().getKey().toString();
		String policySubLineKey = policyLineKey + policy.GetSubLine().getKey().toString();
		String result = policy.GetSubLine().getLine().getCategory().getKey().toString();
		
		// Iterates to check if the String with the Category, Line and Subline GUIDS is contained in 
		// the ordering array
		for (int i=0; i < ORDERED_SUBLINES.length; i++) {
			String[] temp = ORDERED_SUBLINES[i];
			if (Arrays.asList(temp).contains(policySubLineKey)) {
				return policySubLineKey;
			}
		}
		
		// Iterates to check if the String with the Category and Line GUIDS is contained in 
		// the ordering array
		for (int i=0; i < ORDERED_SUBLINES.length; i++) {
			String[] temp = ORDERED_SUBLINES[i];
			if (Arrays.asList(temp).contains(policyLineKey)) {
				return policyLineKey;
			}
		}
		
		// So, this is one of those policies which will be grouped and displayed by Category alone
		return result;
	}

	/**
	 * This method gets the client's name or group's name to display in the
	 * second row in the table
	 */
	private String getClientOrGroup(String[] reportParams)
			throws BigBangJewelException {

		String displayName;

		// If the client's name is defined, it always displays the client's
		// name. If the group name is defined, it is displayed
		if (reportParams[0] != null) {
			Client client = Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(reportParams[0]));
			displayName = (String) client.getAt(Client.I.NAME);
		} else if (reportParams[1] != null) {
			ClientGroup clientGroup = ClientGroup.GetInstance(
					Engine.getCurrentNameSpace(),
					UUID.fromString(reportParams[1]));
			displayName = (String) clientGroup.getAt(ClientGroup.I.NAME);
		} else {
			displayName = null;
		}

		return displayName;
	}

	/**
	 * The method responsible for building the rows in the report with the
	 * policies' info
	 */
	private Table buildReportInfo(
			HashMap<String, ArrayList<Policy>> policiesMap,
			int numberOfPolicies, String[] reportParams)
					throws BigBangJewelException {

		Table table;
		TR[] tableRows = new TR[numberOfPolicies + 1 + policiesMap.size()];
		int rowNum = 0;

		// Builds the row with the column names
		tableRows[rowNum++] = ReportBuilder
				.buildRow(buildOuterHeaderRow(reportParams));
		ReportBuilder.styleRow(tableRows[0], true);

		// Iterates the hashmap with the several policies by "ramo", and creates
		// a line
		// with the name of that "ramo", followed by the information about the
		// policies
		for (int i=0; i<ORDERED_SUBLINES.length; i++) {
			String [] sublineTemp = ORDERED_SUBLINES[i];
			String titleTemp = CATEGORY_TRANSLATOR.get(sublineTemp[0]);
			
			ArrayList<Policy> policies = policiesMap.get(sublineTemp[0]);
			
			if (sublineTemp.length > 1) {
				for (int u=1; u<sublineTemp.length; u++) {
					ArrayList<Policy> sublineTempElement = policiesMap.get(sublineTemp[u]);
					if (sublineTempElement != null) {
						policies.addAll(sublineTempElement);
					}
				}
			}
			
			if (policies != null) {
				Policy [] policyArray = policies.toArray(new Policy[0]);
				
				if (titleTemp == null) {
					titleTemp = CATEGORY_TRANSLATOR.get(policyArray[0]
							.GetSubLine().getLine().getCategory().getKey()
							.toString());
					if (titleTemp == null) {
						titleTemp = policyArray[0].GetSubLine().getLine()
								.getCategory().getLabel();
					}
				}
				
				tableRows[rowNum++] = buildSublineRow(titleTemp);
				
	
				for (int u = 0; u < policyArray.length; u++) {
					tableRows[rowNum] = ReportBuilder.buildRow(buildRow(
							(Policy) policyArray[u], reportParams));
					ReportBuilder.styleRow(tableRows[rowNum++], false);
				}
			}
		}

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);

		return table;
	}

	/**
	 * The method responsible for getting columns' titles for the content
	 * related to the policy
	 */
	private TD[] buildOuterHeaderRow(String[] reportParams) {

		TD[] cells;

		// Splits the report params in those referring to the values associated
		// with a policy, and those referring to the values associated with the
		// insured object
		ArrayList<String> policyParams = new ArrayList<String>();
		Collections
		.addAll(policyParams, Arrays.copyOfRange(reportParams, 2, 6));
		policyParams.addAll(Arrays.asList(Arrays.copyOfRange(reportParams, 12,
				15)));
		String[] objectParams = Arrays.copyOfRange(reportParams, 6, 12);

		// Counts the number of columns related with the policy and the insured
		// object which should be displayed
		int innerObjs = Collections.frequency(Arrays.asList(objectParams), "1");
		int cellNumber = Collections.frequency(policyParams, "1");
		if (objectParams.length > 0) {
			cellNumber++;
		}

		cells = new TD[cellNumber];
		int paramCheck = 0;
		cellNumber = 0;

		boolean leftLine = false;

		// Creates the header row
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Apólice",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Segurador",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = new TD(buildDoubleHeaderTitle ("Venc.", "(M / D)"));
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Frac.",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (innerObjs > 0) {
			cells[cellNumber++] = buildInnerHeaderRow(objectParams);
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = new TD(buildDoubleHeaderTitle ("Prémio Total", "Anual"));
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = new TD(buildDoubleHeaderTitle ("Modalidade de", "Pagamento"));
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Observações",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}

		setOuterWidths(cells, reportParams);

		return cells;
	}

	/**
	 * This method builds the row with the information about the preceding
	 * policies' "ramo"
	 */
	private TR buildSublineRow(String subline) {

		TD rowContent;
		TR row;

		// Builds the TD with the content
		rowContent = ReportBuilder.buildCell("Ramo: " + subline,
				TypeDefGUIDs.T_String);
		rowContent.setColSpan(10);
		ReportBuilder.styleCell(rowContent, true, false);

		// Builds the TR encapsulating the TD
		row = ReportBuilder.buildRow(new TD[] { rowContent });
		row.setStyle("height:35px;background:#e2f2ff;font-weight:bold;");

		return row;
	}

	/**
	 * This method is responsible for building a row with information about a
	 * given policy
	 */
	private TD[] buildRow(Policy policy, String[] reportParams)
			throws BigBangJewelException {

		Company company;
		String maturity;
		PolicyObject[] policyObjects;
		HashMap<UUID, CoverageData> valuesByObject;

		// Gets the report parameters related to the policy information
		ArrayList<String> policyParams = new ArrayList<String>();
		Collections
		.addAll(policyParams, Arrays.copyOfRange(reportParams, 2, 6));
		policyParams.addAll(Arrays.asList(Arrays.copyOfRange(reportParams, 12,
				15)));

		String[] objectParams = Arrays.copyOfRange(reportParams, 6, 12);
		
		// Splits the report params in those referring to the values associated
		// with a policy, and those referring to the values associated with the
		// insured object
		int cellNumber = Collections.frequency(policyParams, "1");
		int nrInnerTDs = Collections
				.frequency(Arrays.asList(objectParams), "1");
		if (nrInnerTDs > 0) {
			cellNumber++;
		}

		TD[] dataCells = new TD[cellNumber];

		// Variables used to know which parameter is being checked for display,
		// and which
		// cell is being built
		cellNumber = 0;
		int paramCheck = 0;

		boolean leftLine = false;

		company = policy.GetCompany();

		maturity = ((policy.getAt(Policy.I.MATURITYMONTH) == null)
				|| ((Integer) policy.getAt(Policy.I.MATURITYMONTH) == -1)
				|| (policy.getAt(Policy.I.MATURITYDAY) == null) || ((Integer) policy
						.getAt(Policy.I.MATURITYDAY) == -1)) ? null : policy.getAt(
								Policy.I.MATURITYMONTH).toString()
								+ " / " + policy.getAt(Policy.I.MATURITYDAY).toString();

		policyObjects = policy.GetCurrentObjects();

		valuesByObject = getValuesByObject(policy);

		// Increases the value for the total premium
		if (policy.getAt(Policy.I.TOTALPREMIUM) != null) {
			premiumTotal = premiumTotal.add((BigDecimal) policy.getAt(Policy.I.TOTALPREMIUM));
		}

		// Policy Number
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = safeBuildCell(policy.getLabel(),
					TypeDefGUIDs.T_String, false, false);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		// Insurance Company
		if (policyParams.get(paramCheck++).equals("1")) {
			String companyName = (String) company.getAt(Company.I.NAME);
			if (companyName != null
					&& companyName.length() > STRING_BREAK_POINT) {
				ArrayList<String> objectNameArray = new ArrayList<String>();
				objectNameArray.add(companyName);
				dataCells[cellNumber] = buildValuesTable(
						splitValue(objectNameArray, STRING_BREAK_POINT), OBJECT_WIDTH, false, false);
			} else {
				dataCells[cellNumber] = safeBuildCell(companyName,
						TypeDefGUIDs.T_String, false, false);
			}
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		// Maturity
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = safeBuildCell(maturity,
					TypeDefGUIDs.T_String, false, false);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}
		
		// Fractioning
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = safeBuildCell(getFractioning(policy),
					TypeDefGUIDs.T_String, false, false);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		// If there are columns related with a given insured object to
		// display...
		if (nrInnerTDs > 0) {
			dataCells[cellNumber] = buildInnerObjectsTable(policy,
					objectParams, policyObjects, valuesByObject);
			dataCells[cellNumber++].setStyle("border-top:1px solid #3f6d9d;");
		}

		// Total Premium
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = safeBuildCell( policy.getAt(Policy.I.TOTALPREMIUM), 
					TypeDefGUIDs.T_Decimal, true, true);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		// Payment Method
		if (policyParams.get(paramCheck++).equals("1")) {
			String method = getPaymentMethod(policy);
			
			if (method==null) {
				dataCells[cellNumber] = safeBuildCell(method,
						TypeDefGUIDs.T_String, false, false);
			} else {
				ArrayList<String> methodArray = new ArrayList<String>();
				methodArray.add(method);
				dataCells[cellNumber] = buildValuesTable(
						splitValue(methodArray, METHOD_BREAK_POINT), OBJECT_WIDTH, false, false);
			}
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}
				
		// Notes - intentionally left blank
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = ReportBuilder.buildCell(" ",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		setOuterWidths(dataCells, reportParams);

		return dataCells;
	}

	/**
	 * This method returns the fractioning textual description
	 */
	private String getFractioning(Policy policy) {

		UUID fractioningID = (UUID) policy.getAt(Policy.I.FRACTIONING);
		
		if (fractioningID.equals(Constants.FracID_Year)) {
			return FRAC_YEAR;
		} else if (fractioningID.equals(Constants.FracID_Semester)) {
			return FRAC_SEMESTER;
		} else if (fractioningID.equals(Constants.FracID_Quarter)) {
			return FRAC_TRIMESTER;
		} else if (fractioningID.equals(Constants.FracID_Month)) {
			return FRAC_MONTH;
		} else if (fractioningID.equals(Constants.FracID_Single)) {
			return FRAC_UNIQUE;
		} else if (fractioningID.equals(Constants.FracID_Variable)) {
			return FRAC_FRACTIONED;
		}
		return null;
	}

	/**
	 * This method returns the payment Method according to the type of the policy's profile
	 */
	private String getPaymentMethod(Policy policy) throws BigBangJewelException {

		UUID profile = policy.getProfile();
		
		if (profile == null) {
			profile = policy.GetClient().getProfile();
		}
		if (profile == null) {
			return null;
		}
		
		if (profile.equals(Constants.ProfID_External)) {
			return DIRECT_PAYMENT;
		} else {
			return BROKER_PAYMENT;
		}
	}

	/**
	 * The method responsible for getting columns' titles for the content
	 * related to the insured object
	 */
	private TD buildInnerHeaderRow(String[] reportParams) {

		Table table;
		TR[] tableRows = new TR[1];
		int cellNumber = Collections
				.frequency(Arrays.asList(reportParams), "1");
		TD[] cells = new TD[cellNumber];
		cellNumber = 0;
		int paramCheck = 0;

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Pessoas/Bens Seguros",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Local de Risco",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Coberturas",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Capital",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = cells[cellNumber] = new TD(buildDoubleHeaderTitle ("Taxa", "Com."));
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Franq.",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		setInnerWidths(cells, reportParams);

		tableRows[0] = ReportBuilder.buildRow(cells);

		ReportBuilder.styleRow(tableRows[0], true);

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);
		return new TD(table);
	}

	/**
	 * The method which defines the width for the columns
	 */
	private void setOuterWidths(TD[] cells, String[] reportParams) {

		int cellNumber = Collections
				.frequency(Arrays.asList(reportParams), "1");

		cellNumber = 0;

		int paramCheck = 2;

		int innerWidth = 0;

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(POLICY_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(COMPANY_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(DATE_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(FRACTIONING_WIDTH);
		}

		// The column holding the inner-table (insured objects' values)
		if (reportParams[paramCheck++].equals("1")) {
			innerWidth += OBJECT_WIDTH;
		}
		if (reportParams[paramCheck++].equals("1")) {
			innerWidth += RISK_SITE_WIDTH;
		}
		if (reportParams[paramCheck++].equals("1")) {
			innerWidth += COVERAGES_WIDTH;
		}
		if (reportParams[paramCheck++].equals("1")) {
			innerWidth += VALUE_WIDTH;
		}
		if (reportParams[paramCheck++].equals("1")) {
			innerWidth += TAX_WIDTH;
		}
		if (reportParams[paramCheck++].equals("1")) {
			innerWidth += FRANCHISE_WIDTH;
		}
		if (innerWidth > 0) {
			cells[cellNumber++].setWidth(innerWidth);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(PREMIUM_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(METHOD_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(OBSERVATIONS_WIDTH);
		}
	}

	/**
	 * This method builds an HashMap with the coverages, the insured values, the
	 * risk site and the taxes related with a given object. This HashMap is
	 * retrieved in a way that it can be used in a consistent form, even if the
	 * way it is being built changes (A LOT) according to the policy's category
	 */
	private HashMap<UUID, CoverageData> getValuesByObject(Policy policy)
			throws BigBangJewelException {

		PolicyObject[] policyObjects;

		HashMap<UUID, CoverageData> result = new HashMap<UUID, CoverageData>();

		policyObjects = policy.GetCurrentObjects();

		CoverageData coverageData;

		// Iterates the policy's objects (if any)
		// In case the policy does not have objects, it uses a "faux" object
		for (int i = 0; i <= policyObjects.length; i++) {

			UUID objectKey;

			PolicyObject insuredObject;
			if (policyObjects.length != 0 && policyObjects.length > i) {
				insuredObject = policyObjects.length == 0 ? null
						: policyObjects[i];
				objectKey = insuredObject.getKey();
			} else {
				insuredObject = null;
				objectKey = fauxId; // faux
			}

			// Needed to guarantee that the following code is executed when
			// there are no objects, and
			// to prevent a java.lang.ArrayIndexOutOfBoundsException
			if (policyObjects.length == 0 || policyObjects.length > i) {

				// Gets the Data related to the coverages and inserts in the
				// HashMap
				coverageData = buildCoverageData(policy, insuredObject);
				result.put(objectKey, coverageData);

			}
		}
		return result;
	}

	/**
	 * Builds a cell in a "safe way", meaning that if the value is null, the
	 * cell is built simply with a whitespace, preventing cells with the '?'
	 * character
	 */
	private TD safeBuildCell(java.lang.Object pobjValue, UUID pidType, 
			boolean addEuro, boolean alignRight) {
		if (pobjValue == null || pobjValue.toString().trim().length() == 0) {
			return ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		}
		if (addEuro) {
			String valueString = pobjValue + " €";
			return safeBuildCell(valueString, TypeDefGUIDs.T_String, false, alignRight);
		}
		return ReportBuilder.buildCell(pobjValue, pidType, alignRight);
	}

	/**
	 * This method is used to split a string, which could be "too wide", and
	 * wreck the report's look. It gets not a String, but an array list with one
	 * element (the string) for it is the way it is represented in the
	 * CoverageData's class.
	 */
	private ArrayList<String> splitValue(ArrayList<String> stringToSplit, int breakPosition) {

		ArrayList<String> result = new ArrayList<String>();
		String[] split = stringToSplit.get(0).split("\\s+");
		String tmp = "";

		// Splits when it occupies more than (approximately) one row's length
		for (int i = 0; i < split.length; i++) {
			tmp = tmp + " " + split[i];
			if ((tmp.length() / (breakPosition - 6)) >= 1) {
				result.add(tmp);
				tmp = "";
			} else if (i + 1 == split.length) {
				result.add(tmp);
			}
		}

		return (result.size() == 0 ? stringToSplit : result);
	}

	/**
	 * This method returns a TD containing a value or a table of values,
	 * contained in an ArrayList. It's used to build tables to display inside a
	 * TD.
	 */
	private TD buildValuesTable(ArrayList<String> data, int width,
			boolean addEuro, boolean alignRight) {

		TD content;

		content = new TD();
		content.setColSpan(1);

		if (data == null) {
			return ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		}

		// Builds a "simple" TD or a table with a TD with each value
		if (data.size() == 1) {
			content = safeBuildCell(data.get(0), TypeDefGUIDs.T_String, addEuro, alignRight);
			content.setWidth(width);
			ReportBuilder.styleCell(content, false, false);
			ReportBuilder.styleInnerContainer(content);
		} else {

			Table table;
			TR[] tableRows = new TR[data.size()];

			// Iterates the values, and adds them to the table
			for (int i = 0; i < data.size(); i++) {
				TD[] cell = new TD[1];

				cell[0] = safeBuildCell(data.get(i), TypeDefGUIDs.T_String, addEuro, alignRight);
				cell[0].setWidth(width);
				cell[0].setStyle("overflow:hidden;white-space:nowrap");

				tableRows[i] = ReportBuilder.buildRow(cell);
				ReportBuilder.styleRow(tableRows[i], false);
			}

			table = ReportBuilder.buildTable(tableRows);
			ReportBuilder.styleTable(table, true);

			content.addElement(new Div().addElement(table).setStyle(
					"width:inherit;"));
			ReportBuilder.styleInnerContainer(content);
		}

		return content;
	}

	/**
	 * This method build the table with the report's info related to a given
	 * insured object, namely the object's name, the risk site, the coverages,
	 * the insured values and the tax
	 */
	private TD buildInnerObjectsTable(Policy policy, String[] reportParams,
			PolicyObject[] policyObjects,
			HashMap<UUID, CoverageData> valuesByObject) {

		// Gets the number of columns to initialize the TD's array
		int cellNumber = Collections
				.frequency(Arrays.asList(reportParams), "1");

		Table table;
		int nrRows = policyObjects.length != 0 ? policyObjects.length : 1;
		TR[] tableRows = new TR[nrRows];
		nrRows = 0;

		// Iterates the objects (if any, otherwise it makes a
		// "forced iteration") and builds the row
		// corresponding to a given object
		for (int i = 0; i <= policyObjects.length; i++) {

			TD[] dataCells = new TD[cellNumber];

			int currentCell = 0;
			int paramCheck = 0;

			boolean topLine = (i != 0);

			// If there are no objects, uses a "faux" key to retrieve the
			// coverages, insured values, risk site and taxes
			UUID objectKey;
			if (policyObjects.length != 0 && policyObjects.length > i) {
				objectKey = policyObjects[i].getKey();
			} else {
				objectKey = fauxId;
			}

			// Needed to guarantee that the following code is executed when
			// there are no objects, and to prevent a
			// java.lang.ArrayIndexOutOfBoundsException
			if (policyObjects.length == 0 || policyObjects.length > i) {

				// Policy Object's Name
				if (reportParams[paramCheck++].equals("1")) {
					String objectName = (policyObjects.length == 0) ? " "
							: getObjectName(policy, policyObjects[i]);
					if (objectName != null
							&& objectName.length() > STRING_BREAK_POINT) {
						ArrayList<String> objectNameArray = new ArrayList<String>();
						objectNameArray.add(objectName);
						dataCells[currentCell] = buildValuesTable(
								splitValue(objectNameArray, STRING_BREAK_POINT), OBJECT_WIDTH, false, false);
					} else {
						dataCells[currentCell] = safeBuildCell(objectName,
								TypeDefGUIDs.T_String, false, false);
					}
					dataCells[currentCell].setWidth(OBJECT_WIDTH);
					dataCells[currentCell].setHeight(INNER_HEIGHT);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Risk Site
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(
							splitValue(valuesByObject.get(objectKey)
									.getRiskSite(), STRING_BREAK_POINT), RISK_SITE_WIDTH, false, false);
					dataCells[currentCell].setWidth(RISK_SITE_WIDTH);
					dataCells[currentCell].setHeight(INNER_HEIGHT);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Policy Coverages
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getCoverages(), COVERAGES_WIDTH, false, false);
					dataCells[currentCell].setWidth(COVERAGES_WIDTH);
					dataCells[currentCell].setHeight(INNER_HEIGHT);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Insured Value
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getInsuredValues(), VALUE_WIDTH, true, true);
					dataCells[currentCell].setWidth(VALUE_WIDTH);
					dataCells[currentCell].setHeight(INNER_HEIGHT);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Sales' Tax
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getTaxes(), TAX_WIDTH, false, true);
					dataCells[currentCell].setWidth(TAX_WIDTH);
					dataCells[currentCell].setHeight(INNER_HEIGHT);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}
				
				// Franchise
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getFranchises(), FRANCHISE_WIDTH, false, true);
					dataCells[currentCell].setWidth(FRANCHISE_WIDTH);
					dataCells[currentCell].setHeight(INNER_HEIGHT);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				setInnerWidths(dataCells, reportParams);
				tableRows[nrRows++] = ReportBuilder.buildRow(dataCells);
			}
		}
		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);
		return new TD(table);
	}

	/**
	 * The method which defines the width for the columns displaying the values
	 * associated with a given insured object
	 */
	private void setInnerWidths(TD[] cells, String[] reportParams) {

		int cellNumber = 0;
		int paramCheck = 0;

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(OBJECT_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(RISK_SITE_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(COVERAGES_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(VALUE_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(TAX_WIDTH);
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(FRANCHISE_WIDTH);
		}
	}

	/**
	 * This method populates a CoverageData class with the values related to a
	 * given object. Upon population, it acts differently according to the
	 * policy's category
	 */
	private CoverageData buildCoverageData(Policy policy,
			PolicyObject insuredObject) throws BigBangJewelException {

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();

		UUID currentExercise = getCurrentExercise(policy.GetCurrentExercises());

		CoverageData coverageData = new CoverageData();

		PolicyValue[] policyValues = policy.GetCurrentValues();

		PolicyCoverage[] policyCoverages = null;

		/*
		 * Policy Coverages' set
		 */
		// In case of an Automobile / Fleet policy, and only one policy object,
		// it should read "Diversos"
		if (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE)
				&& policySubLine.equals(Constants.PolicySubLines.AUTO_AUTO_FLEET)
				&& policy.GetCurrentObjects().length == 1) {
			coverageData.setCoverages("Diversos");
		} else {
			policyCoverages = policy.GetCurrentCoverages();
			coverageData.setCoverages(policyCoverages);
		}

		/*
		 * Policy Risk Site's set
		 */
		try {
			coverageData.setRiskSite(getRiskSite(policy, insuredObject,
					policyValues));
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		/*
		 * Policy Insured Values' set
		 */
		coverageData.setInsuredValues(getInsuredValues(policy, insuredObject,
				currentExercise, policyValues, policyCoverages));

		/*
		 * Policy Taxes' set
		 */
		coverageData.setTaxes(getTaxes(policy, insuredObject, currentExercise,
				policyValues, policyCoverages));
		
		/*
		 * Policy Franchise's set
		 */
		coverageData.setFranchises(getFranchises(policy, insuredObject, currentExercise,
				policyValues, policyCoverages));

		return coverageData;
	}

	/**
	 * This method gets the object name according to the policy's category
	 */
	private String getObjectName(Policy policy, PolicyObject policyObject) {

		if (policyObject == null) {
			return " ";
		}

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();

		// For an automobile / Individual, it's the mark and model
		// otherwise, it's the policy object's name
		if (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE)
				&& policySubLine
				.equals(Constants.PolicySubLines.AUTO_AUTO_INDIVIDUAL)) {
			return (String) policyObject.getAt(PolicyObject.I.MAKEANDMODEL);
		} else {
			return (String) policyObject.getAt(PolicyObject.I.NAME);
		}
	}

	/**
	 * This method gets and iterates the policy exercises, and returns the
	 * current one (the one with the newer start date)
	 */
	private UUID getCurrentExercise(PolicyExercise[] exercises) {
		Timestamp maxDate = new Timestamp(1);
		UUID result = null;
		for (int i = 0; i < exercises.length; i++) {
			if (((Timestamp) exercises[i].getAt(PolicyExercise.I.STARTDATE))
					.after(maxDate)) {
				maxDate = (Timestamp) exercises[i]
						.getAt(PolicyExercise.I.STARTDATE);
				result = (UUID) exercises[i].getKey();
			}
		}
		return result;
	}

	/**
	 * This method gets the risk site (if any) according to the policy's
	 * category
	 */
	private String getRiskSite(Policy policy, PolicyObject insuredObject,
			PolicyValue[] policyValues) throws InvocationTargetException,
			JewelEngineException {

		if (policyValues == null) {
			return " ";
		}

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();

		// If policy is MULTIRRISK
		if (policyCat.equals(Constants.PolicyCategories.MULTIRISK)
				&& insuredObject != null) {
			String address = (String) (insuredObject
					.getAt(PolicyObject.I.ADDRESS1) == null ? ""
							: insuredObject.getAt(PolicyObject.I.ADDRESS1));
			address = (String) (insuredObject.getAt(PolicyObject.I.ADDRESS2) == null ? address
					: address + " "
					+ insuredObject.getAt(PolicyObject.I.ADDRESS2));
			UUID zipGUID = (UUID) insuredObject.getAt(PolicyObject.I.ZIPCODE);
			if (zipGUID != null) {
				ObjectBase zipCode = Engine.GetWorkInstance(
						Engine.FindEntity(Engine.getCurrentNameSpace(),
								ObjectGUIDs.O_PostalCode), zipGUID);
				address = address + " " + zipCode.getAt(0);
				address = address + " " + zipCode.getAt(1);
			}

			return address;
		}

		// If policy is RESPONSABILITY or CONSTRUCTION_ASSEMBLY, the risk site
		// is retrieved from the policy values
		for (int i = 0; i < policyValues.length; i++) {
			if (policyValues[i].GetTax().GetTag() != null
					&& (policyValues[i].GetObjectID() == null
					|| insuredObject == null || policyValues[i]
							.GetObjectID().equals(insuredObject.getKey()))
							&& policyValues[i].GetValue() != null) {

				if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)) {
					if (policyValues[i]
							.GetTax()
							.GetTag()
							.equals(Constants.PolicyValuesTags.TERRITORIAL_SCOPE)) {
						return policyValues[i].GetValue();
					}
				}
				if (policyCat
						.equals(Constants.PolicyCategories.CONSTRUCTION_ASSEMBLY)) {
					if (policyValues[i].GetTax().GetTag()
							.equals(Constants.PolicyValuesTags.CONTRACT_NAME)) {
						return policyValues[i].GetValue();
					}
				}
			}
		}

		return "-";
	}

	/**
	 * This method gets the insured values according to the category. If they
	 * are related with the coverages, they are returned in the same order than
	 * the coverages, with a white space when the value does not exist for a
	 * given coverage
	 */
	private ArrayList<String> getInsuredValues(Policy policy,
			PolicyObject insuredObject, UUID currentExercise,
			PolicyValue[] policyValues, PolicyCoverage[] policyCoverages)
					throws BigBangJewelException {

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();

		ArrayList<String> result = new ArrayList<String>();

		/*
		 * Special Cases
		 */
		if (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE)
				&& policySubLine.equals(Constants.PolicySubLines.AUTO_AUTO_FLEET)
				&& policy.GetCurrentObjects().length == 1) {
			// For an automobile / Fleet with only one insured object, it should
			// read "Diversos"
			result.add("Diversos");
			return result;
		} else if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)) {
			// For Work Accidents, the insured value corresponds to the
			// temporary value from the legal coverage
			String valueWithTags = getValueWithTags(policyValues,
					insuredObject, currentExercise,
					Constants.PolicyCoveragesTags.LEGAL,
					Constants.PolicyValuesTags.TEMPORARY_VALUE, false);
			if (valueWithTags == null || valueWithTags.length() == 0) {
				valueWithTags = getValueWithTags(policyValues, insuredObject,
						currentExercise, Constants.PolicyCoveragesTags.LEGAL,
						Constants.PolicyValuesTags.VALUE, false);
			}
			result.add(valueWithTags);
			return result;
		} else if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)) {
			// For Responsibility policies, the value comes from the insured
			// value in the policy header
			result.add(getValueWithTags(policyValues, insuredObject,
					currentExercise, null, Constants.PolicyValuesTags.VALUE,
					true));
			return result;
		}

		/*
		 * "Default case" - to each coverage corresponds a given insured value
		 */
		for (int i = 0; i < policyCoverages.length; i++) {
			// Only lists the values for the coverages present at the policy
			if (policyCoverages[i].IsPresent()!=null && policyCoverages[i].IsPresent()) {
				result.add(getCoverageValues(insuredObject, currentExercise,
						policyValues, policyCoverages[i],
						Constants.PolicyValuesTags.VALUE));
			}
		}

		if (result.size() == 0) {
			return null;
		}

		return result;
	}

	/**
	 * This method gets the taxes according to the category. If they are related
	 * with the coverages, they are returned in the same order than the
	 * coverages, with a white space when the value does not exist for a given
	 * coverage Note that there are some categories without an associated tax
	 */
	private ArrayList<String> getTaxes(Policy policy,
			PolicyObject insuredObject, UUID currentExercise,
			PolicyValue[] policyValues, PolicyCoverage[] policyCoverages) {

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();

		ArrayList<String> result = new ArrayList<String>();

		/*
		 * Special Cases
		 */
		if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)
				|| policyCat.equals(Constants.PolicyCategories.LIFE)) {
			// If the policy is in the work accidents or life categories, the
			// tax corresponds to the sales tax from the policy header
			result.add(getValueWithTags(policyValues, insuredObject,
					currentExercise, null,
					Constants.PolicyValuesTags.SALES_TAX, true));
			return result;
		} else if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)) {
			// If the policy is a responsibility policy, the tax is the
			// "hitting rate"
			result.add(getValueWithTags(policyValues, insuredObject,
					currentExercise, null,
					Constants.PolicyValuesTags.HITTING_RATE, true));
			return result;
		}

		/*
		 * Direct tax-coverage correspondence
		 */
		if (policyCat.equals(Constants.PolicyCategories.MULTIRISK)
				|| policyCat
				.equals(Constants.PolicyCategories.PERSONAL_ACCIDENTS)
				|| policyCat
				.equals(Constants.PolicyCategories.CONSTRUCTION_ASSEMBLY)) {

			for (int i = 0; i < policyCoverages.length; i++) {

				// Only lists the values for the coverages present at the policy
				if (policyCoverages[i].IsPresent()!=null && policyCoverages[i].IsPresent()) {

					result.add(getCoverageValues(insuredObject,
							currentExercise, policyValues, policyCoverages[i],
							Constants.PolicyValuesTags.SALES_TAX));
				}
			}
			return result;
		}

		// Default - no tax
		return new ArrayList<String>(Arrays.asList("-"));
	}

	/**
	 * This method iterates the policy values and returns the one with a given
	 * coverage's tag and a given tax's tag (if any)
	 */
	private String getValueWithTags(PolicyValue[] policyValues,
			PolicyObject insuredObject, UUID exerciseId, String coverageTag,
			String taxTag, boolean isHeader) {

		UUID objectID = insuredObject == null ? null : insuredObject.getKey();

		for (int i = 0; i < policyValues.length; i++) {

			// The value is either associated to a given coverage, or there is
			// no coverage to compare
			if (coverageTag == null
					|| coverageTag.equals(policyValues[i].GetTax()
							.GetCoverage().GetTag())
							|| (isHeader && policyValues[i].GetTax().GetCoverage()
									.IsHeader())) {
				// The value is either associated to a given tax, or there is
				// no tax to compare
				if (taxTag == null
						|| (policyValues[i].GetTax().GetTag() != null && policyValues[i]
								.GetTax().GetTag().equals(taxTag))) {
					// Checks if the value corresponds to a given object and
					// exercise, or
					// in case any of these is null, if the value is not
					// associated with it
					if ((policyValues[i].GetObjectID() == null
							&& objectID == null || (objectID != null && objectID
							.equals(policyValues[i].GetObjectID())))
							&& (policyValues[i].GetExerciseID() == null
							&& exerciseId == null || (exerciseId != null && exerciseId
							.equals(policyValues[i].GetExerciseID())))) {
						return policyValues[i].GetValue();
					}
				}
			}
		}

		// If we got to this point, it could mean that it was not possible to
		// get the value associated with an object or an exercise. That is,
		// although the policy has associated objects and exercises, the values
		// are not associated with one (or both) of those. Further iterations
		// are needed, and returns the first associated with the policy,
		// starting from "end to start", considering that the last ones are the
		// more recent
		if (objectID == null && exerciseId != null) {
			for (int i = policyValues.length - 1; i >= 0; i--) {

				// The value is either associated to a given coverage, or there
				// is no coverage to compare
				if (coverageTag == null
						|| coverageTag.equals(policyValues[i].GetTax()
								.GetCoverage().GetTag())
								|| (isHeader && policyValues[i].GetTax().GetCoverage()
										.IsHeader())) {
					// The value is either associated to a given tax, or there
					// is no tax to compare
					if (taxTag == null
							|| (policyValues[i].GetTax().GetTag() != null && policyValues[i]
									.GetTax().GetTag().equals(taxTag))) {
						// Checks if the value is associated with the exercise
						if (exerciseId.equals(policyValues[i].GetExerciseID())) {
							return policyValues[i].GetValue();
						}
					}
				}
			}
		}
		if (objectID != null && exerciseId == null) {
			for (int i = policyValues.length - 1; i >= 0; i--) {

				// The value is either associated to a given coverage, or there
				// is no coverage to compare
				if (coverageTag == null
						|| coverageTag.equals(policyValues[i].GetTax()
								.GetCoverage().GetTag())
								|| (isHeader && policyValues[i].GetTax().GetCoverage()
										.IsHeader())) {
					// The value is either associated to a given tax, or there
					// is no tax to compare
					if (taxTag == null
							|| (policyValues[i].GetTax().GetTag() != null && policyValues[i]
									.GetTax().GetTag().equals(taxTag))) {
						// Checks if the value is associated with the object
						if (objectID.equals(policyValues[i].GetObjectID())) {
							return policyValues[i].GetValue();
						}
					}
				}
			}
		}
		for (int i = policyValues.length - 1; i >= 0; i--) {

			// The value is either associated to a given coverage, or there is
			// no coverage to compare
			if (coverageTag == null
					|| coverageTag.equals(policyValues[i].GetTax()
							.GetCoverage().GetTag())
							|| (isHeader && policyValues[i].GetTax().GetCoverage()
									.IsHeader())) {
				// The value is either associated to a given tax, or there is
				// no tax to compare
				if (taxTag == null
						|| (policyValues[i].GetTax().GetTag() != null && policyValues[i]
								.GetTax().GetTag().equals(taxTag))) {
					return policyValues[i].GetValue();
				}
			}
		}
		return null;
	}

	/**
	 * This method iterates the policy values and returns the one with a given
	 * tax's tag, corresponding to a given coverage
	 */
	private String getCoverageValues(PolicyObject insuredObject,
			UUID currentExercise, PolicyValue[] policyValues,
			PolicyCoverage policyCoverage, String valueTag) {

		UUID objectID = insuredObject == null ? null : insuredObject.getKey();

		// Tries to get the values associated with the object and the exercise
		for (int u = 0; u < policyValues.length; u++) {
			if (policyValues[u].GetTax().GetTag() != null
					&& policyValues[u].GetTax().GetCoverage().getKey()
					.equals(policyCoverage.GetCoverage().getKey())
					&& policyValues[u].GetTax().GetTag().equals(valueTag)
					&& (policyValues[u].GetObjectID() == null
					&& objectID == null || (objectID != null && objectID
					.equals(policyValues[u].GetObjectID())))
					&& (policyValues[u].GetExerciseID() == null
					&& currentExercise == null || (currentExercise != null && currentExercise
					.equals(policyValues[u].GetExerciseID())))
					&& policyValues[u].GetValue() != null) {
				return policyValues[u].GetValue();
			}
		}

		// It was not inserted, so it checks if it could get the value
		// associated with only the exercise, or only the object
		if (objectID == null && currentExercise != null) {
			for (int u = 0; u < policyValues.length; u++) {
				if (policyValues[u].GetTax().GetTag() != null
						&& policyValues[u].GetTax().GetCoverage().getKey()
						.equals(policyCoverage.GetCoverage().getKey())
						&& policyValues[u].GetTax().GetTag().equals(valueTag)
						&& policyValues[u].GetObjectID() == null
						&& (currentExercise.equals(policyValues[u]
								.GetExerciseID()))
								&& policyValues[u].GetValue() != null) {
					return policyValues[u].GetValue();
				}
			}
		}
		if (objectID != null && currentExercise == null) {
			for (int u = 0; u < policyValues.length; u++) {
				if (policyValues[u].GetTax().GetTag() != null
						&& policyValues[u].GetTax().GetCoverage().getKey()
						.equals(policyCoverage.GetCoverage().getKey())
						&& policyValues[u].GetTax().GetTag().equals(valueTag)
						&& objectID.equals(policyValues[u].GetObjectID())
						&& policyValues[u].GetExerciseID() == null
						&& policyValues[u].GetValue() != null) {
					return policyValues[u].GetValue();
				}
			}
		}

		// Iterates the values and gets the insured values' ones, not associated
		// with the object or the exercise
		for (int u = policyValues.length - 1; u >= 0; u--) {
			if (policyValues[u].GetTax().GetTag() != null
					&& policyValues[u].GetTax().GetCoverage().getKey()
					.equals(policyCoverage.GetCoverage().getKey())
					&& policyValues[u].GetTax().GetTag().equals(valueTag)
					&& policyValues[u].GetValue() != null) {
				return policyValues[u].GetValue();
			}
		}

		return " ";
	}
	
	/**
	 * This method gets the taxes according to the category. If they are related
	 * with the coverages, they are returned in the same order than the
	 * coverages, with a white space when the value does not exist for a given
	 * coverage Note that there are some categories without an associated tax
	 */
	private ArrayList<String> getFranchises(Policy policy,
			PolicyObject insuredObject, UUID currentExercise,
			PolicyValue[] policyValues, PolicyCoverage[] policyCoverages) {

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < policyCoverages.length; i++) {

			// Only lists the values for the coverages present at the policy
			if (policyCoverages[i].IsPresent()!=null && policyCoverages[i].IsPresent()) {

				result.add(getCoverageValues(insuredObject,
						currentExercise, policyValues, policyCoverages[i],
						Constants.PolicyValuesTags.FRANCHISE));
			}
		}
		
		if (result.size() == 0) {
			// Default - no tax
			return new ArrayList<String>(Arrays.asList("-"));
		}
		
		return result;
	}
	
	/**
	 * Builds a dual-row table to use in the table header, for longer column names
	 */
	public Table buildDoubleHeaderTitle (String lineOne, String lineTwo) {
		
		Table table;
		TR[] tableRows = new TR[2];
		TD[] tdOne = new TD[1];
		TD[] tdTwo = new TD[1];
		
		tdOne[0] = ReportBuilder.buildCell(lineOne, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(tdOne[0], false, false);
		
		tdTwo[0] = ReportBuilder.buildCell(lineTwo, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(tdTwo[0], false, false);
		
		tableRows[0] = ReportBuilder.buildRow(tdOne);
		tableRows[0].setStyle("font-weight:bold;");
		tableRows[1] = ReportBuilder.buildRow(tdTwo);
		tableRows[1].setStyle("font-weight:bold;");
		
		table = ReportBuilder.buildTable(tableRows);
		return table;
	}
	
	/**
	 * This method gets and configures the Crédite-EGS' logo to display
	 * ate the top of the report
	 */
	private IMG getImage() {
		
		IMG result = new IMG();
		
		result.setSrc("/images/logo.png");
		result.setID("credite_logo");
		result.setBorder(0);
		result.setHeight(116);
		result.setWidth(180);
		
		return result;
	}
}
