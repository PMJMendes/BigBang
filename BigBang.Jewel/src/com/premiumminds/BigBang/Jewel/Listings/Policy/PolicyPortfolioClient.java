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
import java.util.regex.Pattern;

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
import com.premiumminds.BigBang.Jewel.Objects.Tax;
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

		public void setCoverages(String[] policyCoverages) {

			if (policyCoverages == null || policyCoverages.length == 0) {
				coverages.add(WHITESPACE);
			}
			for (int i = 0; i < policyCoverages.length; i++) {
				addCoverage(policyCoverages[i]);
			}
		}

		public void addCoverage(String coverage) {
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
	private UUID emptyId;

	// Width Constants
	private static final int POLICY_WIDTH = 100;
	private static final int COMPANY_WIDTH = 180;
	private static final int DATE_WIDTH = 40;
	private static final int FRACTIONING_WIDTH = 70;
	private static final int OBJECT_WIDTH = 200;
	private static final int RISK_SITE_WIDTH = 230;
	private static final int COVERAGES_WIDTH = 230;
	private static final int VALUE_WIDTH = 88;
	private static final int TAX_WIDTH = 88;
	private static final int FRANCHISE_WIDTH = 50;
	private static final int PREMIUM_WIDTH = 88;
	private static final int METHOD_WIDTH = 60;
	private static final int OBSERVATIONS_WIDTH = 40;
	private static final int STRING_BREAK_POINT = 26;
	private static final int METHOD_BREAK_POINT = 18;
	private static final int COVERAGES_BREAK_POINT = 36;

	// Height
	private static final int INNER_HEIGHT = 70;

	// Payment Methods
	private static final String DIRECT_PAYMENT = "Débito Directo";
	private static final String BROKER_PAYMENT = "Pagamento ao Corretor";

	// Fractioning
	private static final String FRAC_YEAR = "Anual";
	private static final String FRAC_SEMESTER = "Semestral";
	private static final String FRAC_TRIMESTER = "Trimestral";
	private static final String FRAC_MONTH = "Mensal";
	private static final String FRAC_UNIQUE = "Única";
	private static final String FRAC_FRACTIONED = "Fraccionada";

	private static final String MULTIPLE_M = "Diversos";
	private static final String MULTIPLE_F = "Diversas";
	private static final String NO_VALUE = "-";
	private static final String WHITESPACE = " ";

	private static final String ESCAPE_CHARACTER = "|_|_|";
	
	private static final int MAX_NUMBER_OF_OBJECTS = 10;

	/*
	 * This Matrix represents the order to display the policies, as well as the
	 * way to group them. Here's how it works: 
	 * - Each position in the array has an array defining what policies should 
	 * be displayed in that section in the report. 
	 * - This grouping can consider the policy category, line or subline. 
	 * - When creating an hashmap with the policies (method getPoliciesMap(Policy[] policies)),
	 * we try to insert the policies with subline+line+category. If it is not possible, we 
	 * include subline+line. If it is still not possile, the policy is included with a key 
	 * with only the category. 
	 * - When creating the report, this matrix is iterated, and gets the policies with the 
	 * key as described here, hence displaying them with this order.
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
			{ Constants.PolicyCategories.FIRE.toString(), 
			  Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.BURGLARY		}, // "Incêndio e Roubo"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL }, // "Máquinas de Casco"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_BREAKDOWN,
			  Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_CEASING
						+ Constants.PolicySubLines.OTHER_DAMAGES_CEASING_MACHINES }, // "Avaria de Máquinas"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_LEASING.toString() }, // "Bens em Leasing"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.ELECTRONIC_EQUIPMENT.toString() }, // "Equipamento Electrónico"
			{ Constants.PolicyCategories.OTHER_DAMAGES.toString() }, // "Outras perdas
			{ Constants.PolicyCategories.CONSTRUCTION_ASSEMBLY.toString() }, // "Obras e Montagens"
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
			{ Constants.PolicyCategories.RESPONSIBILITY.toString() }, // "Responsabilidade"
			/*
			 * From this point forward are the ones not defined by EGS, so there
			 * is no special order
			 */
			{ Constants.PolicyCategories.AGRICULTURAL.toString() }, // "Agrícola"
			{ Constants.PolicyCategories.MARINE_VESSELS.toString() }, // "Embarcações Marítimas"
			{ Constants.PolicyCategories.RAIL_VEHICLES.toString() }, // "Veículos Ferroviários"
			{ Constants.PolicyCategories.AIRCRAFTS.toString() }, // "Aeronaves"
			{ Constants.PolicyCategories.RETIREMENT_FUND.toString() } // "Fundo de Pensões"
	};

	/*
	 * This HashMap is used to allow the translation for the title of the
	 * groupings displayed at the report. It can use as key a concatenation of
	 * Category, Subline and Line or Category and Line. Or simply Category. If
	 * one key is not represented in this Hashmap, it means that we should
	 * display the Category's label.
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
						"Avaria de Máquinas");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL,
				"Máquinas de Casco");
		CATEGORY_TRANSLATOR
				.put(Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_CEASING
						+ Constants.PolicySubLines.OTHER_DAMAGES_CEASING_MACHINES,
						"Avaria de Máquinas");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString(),
				"Outras perdas");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.OTHER_DAMAGES_LEASING
								.toString(), "Bens em Leasing");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.ELECTRONIC_EQUIPMENT
								.toString(), "Equipamento Electrónico");
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
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.FIRE.toString(), "Incêndio e Roubo");
		CATEGORY_TRANSLATOR.put(
				Constants.PolicyCategories.OTHER_DAMAGES.toString()
						+ Constants.PolicyLines.BURGLARY
								.toString(), "Incêndio e Roubo");
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

		// Builds the table with the report. This
		// "table with one TR with one TD" is needed
		// to export to Excel
		Table table;
		TR[] tableRows = new TR[1];
		TD mainContent = new TD();
		mainContent.addElement(buildClientPortfolio(policies, reportParams));
		tableRows[0] = ReportBuilder.buildRow(new TD[] { mainContent });

		table = ReportBuilder.buildTable(tableRows);

		reportResult[0] = table;

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

		// If no client nor group of clients is selected, an error occurs
		if (reportParams[0] == null && reportParams[1] == null) {
			throw new BigBangJewelException(
					"Deve definir-se o Cliente ou Grupo de Clientes a que o relatório se refere");
		}

		try {

			// Gets the entity responsible for controlling policies, in the
			// current namespace
			policyEntity = Entity.GetInstance(Engine.FindEntity(
					Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			// Starts building the SQL used.
			strSQL = new StringBuilder();
			strSQL.append("SELECT * FROM ("
					+ policyEntity.SQLForSelectAll()
					+ ") [AuxPol] WHERE 1=1");

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " "
					+ "Error while getting the policy SQL.", e);
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
		
		// Filters the possible status 
		strSQL.append(" AND [Status] IN " + getPossiblePolicyStatuses(reportParams));

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
			throw new BigBangJewelException(e.getMessage()
					+ " Error while getting the DB reference.", e);
		}

		// Fetches the policies from the DB
		try {
			fetchedPolicies = ldb.OpenRecordset(strSQL.toString());
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage()
					+ " Error while fetching policies.", e);
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
			throw new BigBangJewelException(e.getMessage()
					+ " Error while creating policies' array list.", e);
		}

		// Cleanup
		try {
			fetchedPolicies.close();
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage()
					+ " Error while closing the policies' result set.", e);
		}
		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while disconnecting from the DB.", e);
		}

		return policies.toArray(new Policy[policies.size()]);
	}
	
	/**
	 * The method responsible for printing the report including the header.
	 * Calls the method which prints the policies' info
	 */
	private Table buildClientPortfolio(Policy[] policies, String[] reportParams)
			throws BigBangJewelException {

		Table table;

		// Sets the number of rows (basically sections) of the report
		// cponsidering the fact it has/doesn't have notes
		int numberOfRows = (reportParams[17] == null || reportParams[17]
				.trim().equals("")) ? 3 : 4;

		TR[] tableRows = new TR[numberOfRows];

		// Builds the header row
		TD headerContent = new TD();
		headerContent.addElement(buildHeader(reportParams));
		tableRows[0] = ReportBuilder.buildRow(new TD[] { headerContent });

		// Builds the row with the policy info
		TD infoContent = new TD();
		infoContent
				.addElement(buildClientPortfolioTable(policies, reportParams));
		tableRows[1] = ReportBuilder.buildRow(new TD[] { infoContent });

		// Builds the row with the policy info
		TD notes = new TD();
		notes.addElement(buildNotesTable(reportParams));
		tableRows[2] = ReportBuilder.buildRow(new TD[] { notes });

		if (numberOfRows == 4) {
			TD reportNotes = new TD();
			notes.addElement(buildReportNotesTable(reportParams));
			tableRows[3] = ReportBuilder.buildRow(new TD[] { reportNotes });
		}

		table = ReportBuilder.buildTable(tableRows);

		return table;
	}
	
	/**
	 * This method gets the possible policy statuses, according to user's
	 * preference concerning listing policies still being created, or not
	 */
	private String getPossiblePolicyStatuses(String[] reportParams) {
		
		String result = "('" + Constants.StatusID_Valid + "'";
		
		// Filters the temporary policies
		if (reportParams[16].equals("1")) {
			result = result + ", '" + Constants.StatusID_InProgress + "'";
		}
		result = result + ")";
		
		return result;
	}
	
	/**
	 * This method adds a clause to filter only the ongoing policies
	 */
	private void filterOngoingPolicies(StringBuilder strSQL) {
		strSQL.append(" AND [DURATION] ='" + Constants.DurID_Ongoing + "'");		
	}
	
	/**
	 * This method builds the report's header, with a credite's logo, the
	 * report's title and the date the report was extracted
	 */
	private Table buildHeader(String[] reportParams)
			throws BigBangJewelException {

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
		Strong titleStrong = new Strong("Carteira de Seguros de "
				+ clientOrGroupTitle);
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
	 * The method responsible for printing the table corresponding to a client's
	 * portfolio
	 */
	private Table buildClientPortfolioTable(Policy[] policies,
			String[] reportParams) throws BigBangJewelException {

		Table table;
		TR[] tableRows;
		TD mainContent;
		int rowNum = 0;
		HashMap<String, ArrayList<Policy>> policiesMap;

		tableRows = new TR[3];
		premiumTotal = BigDecimal.ZERO;
		emptyId = UUID.randomUUID();

		// Creates a map with the policies separated by "ramo"
		policiesMap = getPoliciesMap(policies);

		// Builds the row corresponding to the "real content" of the report -
		// meaning... the values from the policies
		mainContent = new TD();
		mainContent.setColSpan(2);
		mainContent.addElement(buildReportInfo(policiesMap, policies.length,
				reportParams));
		tableRows[rowNum++] = ReportBuilder.buildRow(new TD[] { mainContent });

		// Builds the row with the total number of policies
		tableRows[rowNum++] = constructSummaryRow("Nº de Apólices:",
				policies.length, TypeDefGUIDs.T_Integer, true, false, false);

		// Build the row with the total prize
		if (reportParams[10].equals("1")) {
			tableRows[rowNum++] = constructSummaryRow("Total de Prémios:",
					premiumTotal, TypeDefGUIDs.T_Decimal, true, false, true);
		}

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, false);

		return table;
	}
	
	/**
	 * This method builds the report section with the labels corresponding to
	 * the column's names
	 */
	private Table buildNotesTable(String[] reportParams)
			throws BigBangJewelException {

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
		if (reportParams[11].equals("1")) {
			columnLabels[paramCount] = "Franq.";
			columnFull[paramCount++] = "Franquia";
		}

		tableRows = new TR[paramCount + 1];

		if (paramCount > 0) {
			TD[] notesHeader = new TD[1];
			notesHeader[0] = ReportBuilder.buildCell("Notas",
					TypeDefGUIDs.T_String, false);
			notesHeader[0]
					.setStyle("padding-top:20px;padding-left:5px;width:100px;");
			tableRows[0] = ReportBuilder.buildRow(notesHeader);
			tableRows[0].setStyle("height:50px;font-weight:bold;");
		}

		for (int i = 0; i < paramCount; i++) {
			tableRows[i + 1] = constructSummaryRow(columnLabels[i],
					columnFull[i], TypeDefGUIDs.T_String, false, false, false);
		}

		table = ReportBuilder.buildTable(tableRows);
		table.setStyle("width:100%;");

		return table;
	}
	
	/**
	 * This method builds the section with the notes the user added to the
	 * report upon generation
	 */
	private Table buildReportNotesTable(String[] reportParams)
			throws BigBangJewelException {

		Table table;
		TR[] tableRows = new TR[2];
		TD[] notes = new TD[1];

		String reportNotes = reportParams[17];

		// Creates a line with the word "Observações"
		notes[0] = ReportBuilder.buildCell("Observações",
				TypeDefGUIDs.T_String, false);
		notes[0].setStyle("padding-top:20px;padding-left:5px;width:100px;");
		tableRows[0] = ReportBuilder.buildRow(notes);
		tableRows[0].setStyle("height:50px;font-weight:bold;");

		notes = new TD[1];

		// Creates a line with the notes added as parameter
		Div textDiv = new Div(reportNotes);
		TD textTd = new TD();
		notes[0] = textTd;
		notes[0].addElementToRegistry(textDiv);
		notes[0].setColSpan(4);
		notes[0].setStyle("padding-top:20px;padding-left:5px;width:100px;word-wrap:break-word;");
		tableRows[1] = ReportBuilder.buildRow(notes);
		tableRows[1].setStyle("word-wrap:break-word;");

		table = ReportBuilder.buildTable(tableRows);
		table.setStyle("width:100%;");

		return table;
	}
	
	
	
	/**
	 * This method gets and configures the Crédite-EGS' logo to display at the
	 * top of the report
	 */
	private IMG getImage() throws BigBangJewelException {

		IMG result = new IMG();

		// Crédite-egs
		if (Utils.getCurrency().equals("€")) {
			result.setSrc("data:image/png;base64,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");
		} else {
			// Angola
			result.setSrc("data:image/png;base64,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");
		}
		result.setID("credite_logo");
		result.setBorder(0);
		result.setHeight(116);
		result.setWidth(180);

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
	 * This method creates an HashMap with policies grouped by "ramo"
	 */
	private HashMap<String, ArrayList<Policy>> getPoliciesMap(Policy[] policies)
			throws BigBangJewelException {

		HashMap<String, ArrayList<Policy>> policiesMap;
		String policyKey;
		policiesMap = new HashMap<String, ArrayList<Policy>>();

		try {
			for (int i = 0; i < policies.length; i++) {
				policyKey = getPolicyKey(policies[i]);
				if (policiesMap.get(policyKey) == null) {
					policiesMap.put(policyKey, new ArrayList<Policy>());
				}
				policiesMap.get(policyKey).add(policies[i]);
			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " "
					+ "Error while building the policies' map.", e);
		}
		return policiesMap;
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
		// a line with the name of that "ramo", followed by the information
		// about the policies
		for (int i = 0; i < ORDERED_SUBLINES.length; i++) {
			String[] sublineTemp = ORDERED_SUBLINES[i];
			String titleTemp = CATEGORY_TRANSLATOR.get(sublineTemp[0]);

			ArrayList<Policy> policies = policiesMap.get(sublineTemp[0]);

			if (sublineTemp.length > 1) {
				for (int u = 1; u < sublineTemp.length; u++) {
					ArrayList<Policy> sublineTempElement = policiesMap
							.get(sublineTemp[u]);
					if (sublineTempElement != null) {
						if (policies == null) {
							policies = new ArrayList<Policy>();
						}
						policies.addAll(sublineTempElement);
					}
				}
			}

			if (policies != null) {
				Policy[] policyArray = policies.toArray(new Policy[0]);

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
					try {
						tableRows[rowNum] = ReportBuilder.buildRow(buildRow(
								(Policy) policyArray[u], reportParams));
						ReportBuilder.styleRow(tableRows[rowNum++], false);
					} catch (Throwable e) {
						throw new BigBangJewelException(
								e.getMessage()
										+ " Error while building report info from policy "
										+ policyArray[u].getLabel(), e);
					}
				}
			}
		}

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);

		return table;
	}
	
	/**
	 * This method builds a "summary row" with info to display at the end of the
	 * report. It is (REALLY) similar to ReportBuilder's constructDualRow
	 * method, but without the left line, and allowing to print a "money" value
	 * formatted properly.
	 */
	private TR constructSummaryRow(String text, Object value, UUID typeGUID,
			boolean topRow, boolean rightAlign, boolean isMoney) {

		TD[] cells = new TD[2];
		TR row;

		cells[0] = ReportBuilder.buildHeaderCell(text);
		cells[0].setWidth("1px");
		ReportBuilder.styleCell(cells[0], topRow, false);
		if (!isMoney) {
			cells[1] = ReportBuilder.buildCell(value, typeGUID);
			ReportBuilder.styleCell(cells[1], topRow, false);
			if (rightAlign) {
				cells[1].setAlign("right");
			}
		} else {
			cells[1] = safeBuildCell(value, TypeDefGUIDs.T_String, true,
					rightAlign);
			ReportBuilder.styleCell(cells[1], topRow, false);
		}
		row = ReportBuilder.buildRow(cells);
		row.setStyle("height:30px;background:#e6e6e6;");

		return row;
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
			cells[cellNumber] = new TD(buildDoubleHeaderTitle("Venc.",
					"(M / D)"));
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
			cells[cellNumber] = new TD(buildDoubleHeaderTitle("Prémio Total",
					"Anual"));
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = new TD(buildDoubleHeaderTitle("Modalidade de",
					"Pagamento"));
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
	 * This method is responsible for building a row with information about a given policy
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
		// and which cell is being built
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

		try {
			valuesByObject = getValuesByObject(policy);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " Error while getting values by object from policy " + policy.getLabel(), e);
		}

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
			dataCells[cellNumber] = ReportBuilder.buildCell(WHITESPACE,
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		setOuterWidths(dataCells, reportParams);

		return dataCells;
	}
	
	/**
	 * Builds a cell in a "safe way", meaning that if the value is null, the
	 * cell is built simply with a whitespace, preventing cells with the '?'
	 * character
	 */
	private TD safeBuildCell(java.lang.Object pobjValue, UUID pidType,
			boolean addEuro, boolean alignRight) {
		if (pobjValue == null || pobjValue.toString().trim().length() == 0) {
			return ReportBuilder.buildCell(WHITESPACE, TypeDefGUIDs.T_String);
		}
		if (addEuro && !pobjValue.equals(MULTIPLE_F)
				&& !pobjValue.equals(MULTIPLE_M)
				&& !pobjValue.equals(WHITESPACE) && !pobjValue.equals(NO_VALUE)) {

			String valueString = pobjValue.toString();

			if (valueString.contains(",")) {
				valueString = valueString.replace(".", "");
				valueString = valueString.replace(",", ".");
			}

			valueString = valueString + " " + Utils.getCurrency();

			return safeBuildCell(valueString, TypeDefGUIDs.T_String, false,
					alignRight);
		}
		return ReportBuilder.buildCell(pobjValue, pidType, alignRight);
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
	 * The method responsible for getting columns' titles for the content related to the insured object
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
			cells[cellNumber] = cells[cellNumber] = new TD(buildDoubleHeaderTitle ("Taxa/Prémio", "Comercial"));
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

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policyLine = policy.GetSubLine().getLine().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();

		PolicyObject[] policyObjects;

		HashMap<UUID, CoverageData> result = new HashMap<UUID, CoverageData>();

		policyObjects = policy.GetCurrentObjects();

		CoverageData coverageData;

		// Case in which the policy has no objects
		if (policyObjects == null) {
			coverageData = buildCoverageData(policy, null);
			result.put(emptyId, coverageData);
			return result;
		}

		// Special Cases.
		// In the following cases, the object name should read "Diversos". Most
		// of the values will also read "Diversos".
		if (isSimplifiedPortfolio(policy, policyCat, policyLine, policySubLine)) {
			coverageData = buildSimplifiedCoverageData();
			result.put(policy.getKey(), coverageData); // references the policy key
		} else {
			// Iterates the policy's objects (if any)
			// In case the policy does not have objects, it uses a "faux" object
			for (int i = 0; i <= policyObjects.length; i++) {

				UUID objectKey;

				PolicyObject insuredObject;
				if (policyObjects.length != 0 && policyObjects.length > i) {
					insuredObject = policyObjects[i];
					objectKey = insuredObject == null ? emptyId : insuredObject
							.getKey();
				} else {
					insuredObject = null;
					objectKey = emptyId; // faux
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
		}
		return result;
	}
	
	/**
	 * This method is used to split a string, which could be "too wide", and
	 * wreck the report's look. It gets not a String, but an array list with one
	 * element (the string) for it is the way it is represented in the
	 * CoverageData's class.
	 */
	private ArrayList<String> splitValue(ArrayList<String> stringToSplit,
			int breakPosition) {

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
			return ReportBuilder.buildCell(WHITESPACE, TypeDefGUIDs.T_String);
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
	 * This method returns the fractioning textual description
	 */
	private String getFractioning(Policy policy) {

		UUID fractioningID = (UUID) policy.getAt(Policy.I.FRACTIONING);
		
		if (fractioningID == null) {
			return WHITESPACE;
		}
		
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
	 * This method build the table with the report's info related to a given
	 * insured object, namely the object's name, the risk site, the coverages,
	 * the insured values and the tax
	 */
	private TD buildInnerObjectsTable(Policy policy, String[] reportParams,
			PolicyObject[] policyObjects,
			HashMap<UUID, CoverageData> valuesByObject)
			throws BigBangJewelException {

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();
		UUID policyLine = policy.GetSubLine().getLine().getKey();
		// needed to fetch the special cases...
		boolean specialCase = isSimplifiedPortfolio(policy, policyCat,
				policyLine, policySubLine);

		int numberOfIteractions = specialCase || policyObjects == null
				|| policyObjects.length == 0 ? 1 : policyObjects.length;

		// Gets the number of columns to initialize the TD's array
		int cellNumber = Collections
				.frequency(Arrays.asList(reportParams), "1");

		Table table;
		int nrRows = policyObjects != null && policyObjects.length != 0
				&& !specialCase ? policyObjects.length : 1;
		TR[] tableRows = new TR[nrRows];
		nrRows = 0;

		// Iterates the objects (if any, otherwise it makes a
		// "forced iteration") and builds the row
		// corresponding to a given object
		for (int i = 0; i < numberOfIteractions; i++) {

			TD[] dataCells = new TD[cellNumber];

			int currentCell = 0;
			int paramCheck = 0;

			boolean topLine = (i != 0);

			// If there are no objects, uses a "faux" key to retrieve the
			// coverages, insured values, risk site and taxes
			UUID objectKey;
			if (policyObjects != null && policyObjects.length != 0
					&& policyObjects.length > i) {
				objectKey = policyObjects[i].getKey();
			} else {
				objectKey = emptyId;
			}
			// needed to fetch the special cases when it shlud use the
			// policyGuid to fetch the values
			if (specialCase) {
				objectKey = policy.getKey();
			}

			// Needed to guarantee that the following code is executed when
			// there are no objects, and to prevent a
			// java.lang.ArrayIndexOutOfBoundsException
			if (policyObjects == null || policyObjects.length == 0
					|| policyObjects.length > i || specialCase) {

				// Policy Object's Name
				if (reportParams[paramCheck++].equals("1")) {
					try {
						String objectName = (policyObjects == null || policyObjects.length == 0) ? WHITESPACE
								: getObjectName(policy, policyObjects[i]);
						if (objectName.contains(ESCAPE_CHARACTER)) {
							// If it has an escape character, it means it is a
							// Auto-Individual, and must be treated differently
							ArrayList<String> objectNameArray = new ArrayList<String>(
									Arrays.asList(objectName.split(Pattern
											.quote(ESCAPE_CHARACTER))));
							if (objectNameArray.get(1).length() > STRING_BREAK_POINT) {
								// If the car model is larger than the column,
								// it must be split.
								ArrayList<String> tmpArray = new ArrayList<String>();
								tmpArray.add(objectNameArray.get(1));
								tmpArray = splitValue(tmpArray,
										STRING_BREAK_POINT);
								objectNameArray.remove(1);
								for (int u = 0; u < tmpArray.size(); u++) {
									objectNameArray.add(tmpArray.get(u));
								}
							}

							dataCells[currentCell] = buildValuesTable(
									objectNameArray, OBJECT_WIDTH, false, false);
						} else if (objectName != null
								&& objectName.length() > STRING_BREAK_POINT) {
							ArrayList<String> objectNameArray = new ArrayList<String>();
							objectNameArray.add(objectName);
							dataCells[currentCell] = buildValuesTable(
									splitValue(objectNameArray,
											STRING_BREAK_POINT), OBJECT_WIDTH,
									false, false);
						} else {
							dataCells[currentCell] = safeBuildCell(objectName,
									TypeDefGUIDs.T_String, false, false);
						}
						dataCells[currentCell].setWidth(OBJECT_WIDTH);
						dataCells[currentCell].setHeight(INNER_HEIGHT);
						ReportBuilder.styleCell(dataCells[currentCell++],
								topLine, true);
					} catch (Throwable e) {
						throw new BigBangJewelException(
								e.getMessage()
										+ " Error while writing the object name from policy "
										+ policy.getLabel(), e);
					}
				}

				// Risk Site
				if (reportParams[paramCheck++].equals("1")) {
					try {
						dataCells[currentCell] = buildValuesTable(
								splitValue(valuesByObject.get(objectKey)
										.getRiskSite(), STRING_BREAK_POINT),
								RISK_SITE_WIDTH, false, false);
						dataCells[currentCell].setWidth(RISK_SITE_WIDTH);
						dataCells[currentCell].setHeight(INNER_HEIGHT);
						ReportBuilder.styleCell(dataCells[currentCell++],
								topLine, true);
					} catch (Throwable e) {
						throw new BigBangJewelException(
								e.getMessage()
										+ " Error while writing the risk site from policy "
										+ policy.getLabel(), e);
					}
				}

				// Policy Coverages
				if (reportParams[paramCheck++].equals("1")) {
					try {
						dataCells[currentCell] = buildValuesTable(
								valuesByObject.get(objectKey).getCoverages(),
								COVERAGES_WIDTH, false, false);
						dataCells[currentCell].setWidth(COVERAGES_WIDTH);
						dataCells[currentCell].setHeight(INNER_HEIGHT);
						ReportBuilder.styleCell(dataCells[currentCell++],
								topLine, true);
					} catch (Throwable e) {
						throw new BigBangJewelException(
								e.getMessage()
										+ " Error while writing the coverages from policy "
										+ policy.getLabel(), e);
					}
				}

				// Insured Value
				if (reportParams[paramCheck++].equals("1")) {
					try {
						dataCells[currentCell] = buildValuesTable(
								valuesByObject.get(objectKey)
										.getInsuredValues(), VALUE_WIDTH, true,
								true);
						dataCells[currentCell].setWidth(VALUE_WIDTH);
						dataCells[currentCell].setHeight(INNER_HEIGHT);
						ReportBuilder.styleCell(dataCells[currentCell++],
								topLine, true);
					} catch (Throwable e) {
						throw new BigBangJewelException(
								e.getMessage()
										+ " Error while writing the insured value from policy "
										+ policy.getLabel(), e);
					}
				}

				// Sales' Tax
				if (reportParams[paramCheck++].equals("1")) {
					try {
						// If policy is RESPONSIBILITY
						if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY) &&
							 !policyLine.equals(Constants.PolicyLines.RESPONSIBILITY_ENVIRONMENTAL) &&
							 !specialCase) {
							dataCells[currentCell] = buildSpecialTaxTD(
									valuesByObject.get(objectKey).getTaxes(),
									TAX_WIDTH);
						} else {
							dataCells[currentCell] = buildValuesTable(
									valuesByObject.get(objectKey).getTaxes(),
									TAX_WIDTH, false, true);
						}
						dataCells[currentCell].setWidth(TAX_WIDTH);
						dataCells[currentCell].setHeight(INNER_HEIGHT);
						ReportBuilder.styleCell(dataCells[currentCell++],
								topLine, true);
					} catch (Throwable e) {
						throw new BigBangJewelException(e.getMessage()
								+ " Error while writing the taxes from policy "
								+ policy.getLabel(), e);
					}
				}

				// Franchise
				if (reportParams[paramCheck++].equals("1")) {
					try {
						dataCells[currentCell] = buildValuesTable(
								valuesByObject.get(objectKey).getFranchises(),
								FRANCHISE_WIDTH, false, true);
						dataCells[currentCell].setWidth(FRANCHISE_WIDTH);
						dataCells[currentCell].setHeight(INNER_HEIGHT);
						ReportBuilder.styleCell(dataCells[currentCell++],
								topLine, true);
					} catch (Throwable e) {
						throw new BigBangJewelException(
								e.getMessage()
										+ " Error while writing the franchise from policy "
										+ policy.getLabel(), e);
					}
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
	 * This method returns the payment Method according to the type of the
	 * policy's profile
	 */
	private String getPaymentMethod(Policy policy) throws BigBangJewelException {

		UUID profile = policy.getProfile();

		if (profile == null) {
			try {
				profile = policy.GetClient().getProfile();
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage()
						+ " Error while getting client's profile from policy "
						+ policy.getAt(Policy.I.NUMBER), e);
			}
		}
		if (profile == null) {
			return WHITESPACE;
		}

		if (profile.equals(Constants.ProfID_External)) {
			return DIRECT_PAYMENT;
		} else {
			return BROKER_PAYMENT;
		}
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

		UUID currentExercise = getCurrentExercise(policy.GetCurrentExercises());

		CoverageData coverageData = new CoverageData();

		PolicyValue[] policyValues = policy.GetCurrentValues();

		String[] policyCoverages = null;

		/*
		 * Policy Coverages' set
		 */
		try {
			policyCoverages = getPolicyCoverages(policy, insuredObject, policyValues);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while setting coverages from policy "
					+ policy.getLabel(), e);
		}
		coverageData.setCoverages(policyCoverages);

		/*
		 * Policy Risk Site's set
		 */
		try {
			coverageData.setRiskSite(getRiskSite(policy, insuredObject,
					policyValues));
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while setting risk site from policy "
					+ policy.getLabel(), e);
		}

		/*
		 * Policy Insured Values' set
		 */
		try {
			coverageData.setInsuredValues(getInsuredValues(policy,
					insuredObject, currentExercise, policyValues,
					policy.GetCurrentCoverages()));
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while setting the insured values from policy "
					+ policy.getLabel(), e);
		}

		/*
		 * Policy Taxes' set
		 */
		try {
			coverageData.setTaxes(getTaxes(policy, insuredObject,
					currentExercise, policyValues, policy.GetCurrentCoverages()));
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while setting the taxes from policy "
					+ policy.getLabel(), e);
		}

		/*
		 * Policy Franchise's set
		 */
		try {
			coverageData.setFranchises(getFranchises(policy, insuredObject,
					currentExercise, policyValues, policy.GetCurrentCoverages()));
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while setting the policy franchises from policy "
					+ policy.getLabel(), e);
		}

		return coverageData;
	}
	
	/**
	 * This method checks if this is the case where a policy should have a
	 * simplified portfolio, mostly with the word "Diversos" for most values
	 */
	private boolean isSimplifiedPortfolio(Policy policy, UUID policyCat,
			UUID policyLine, UUID policySubLine)
			throws BigBangJewelException {

		return (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE)
				&& policySubLine
						.equals(Constants.PolicySubLines.AUTO_AUTO_FLEET) && getObjNumber(policy)==1)
				|| (policyCat.equals(Constants.PolicyCategories.OTHER_DAMAGES)
						&& policyLine
								.equals(Constants.PolicyLines.OTHER_DAMAGES_MACHINE_BREAKDOWN) && hasMultipleObjects(policy))
				|| (policyCat.equals(Constants.PolicyCategories.OTHER_DAMAGES)
						&& policyLine
								.equals(Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL) && hasMultipleObjects(policy))
				|| (policyCat.equals(Constants.PolicyCategories.OTHER_DAMAGES)
						&& policyLine
								.equals(Constants.PolicyLines.OTHER_DAMAGES_LEASING) && hasMultipleObjects(policy))
				|| (policyCat.equals(Constants.PolicyCategories.OTHER_DAMAGES)
						&& policyLine
								.equals(Constants.PolicyLines.ELECTRONIC_EQUIPMENT) && hasMultipleObjects(policy))
				|| (policy.GetCurrentObjects()!=null && policy.GetCurrentObjects().length>MAX_NUMBER_OF_OBJECTS) ;
	}
	
	/**
	 * Special case where most coverage data simply says "Diversos" | "Diversas"
	 */
	private CoverageData buildSimplifiedCoverageData() {

		CoverageData coverageData = new CoverageData();

		coverageData.addCoverage(MULTIPLE_F);
		coverageData.setRiskSite(NO_VALUE);
		coverageData.setInsuredValues(new ArrayList<String>(Arrays
				.asList(MULTIPLE_M)));
		coverageData.setTaxes(new ArrayList<String>(Arrays.asList(MULTIPLE_F)));
		coverageData.setFranchises(new ArrayList<String>(Arrays
				.asList(MULTIPLE_F)));

		return coverageData;
	}
	
	/**
	 * This method gets the object name according to the policy's category
	 */
	private String getObjectName(Policy policy, PolicyObject policyObject)
			throws BigBangJewelException {

		if (policyObject == null) {
			return WHITESPACE;
		}

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policyLine = policy.GetSubLine().getLine().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();
		
		// Special Cases:
		// For an automobile / Individual, it's the mark and model
		// There are also some cases where it should read "Diversos"
		// For fire, gets the object address
		// Other cases, 
		if (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE)
				&& policySubLine
						.equals(Constants.PolicySubLines.AUTO_AUTO_INDIVIDUAL)) {
			String objName = "";
			objName = policyObject.getAt(PolicyObject.I.NAME) != null ? objName
					+ policyObject.getAt(PolicyObject.I.NAME) : objName;
			objName = (objName.length() != 0 && policyObject
					.getAt(PolicyObject.I.MAKEANDMODEL) != null) ? objName
					+ ESCAPE_CHARACTER : objName;
			objName = policyObject.getAt(PolicyObject.I.MAKEANDMODEL) != null ? objName// Escape
																						// character
					+ policyObject.getAt(PolicyObject.I.MAKEANDMODEL)
					: objName;
			return objName;
		} else if (isSimplifiedPortfolio(policy, policyCat,
				policyLine, policySubLine)) {
			return MULTIPLE_M;
		} else if (policyCat.equals(Constants.PolicyCategories.FIRE)) {
			return getObjectAddress(policyObject);
		} else {
			return (String) policyObject.getAt(PolicyObject.I.NAME);
		}
	}
	
	/**
	 * This method returns a TD containing a table of values for the
	 * Responsibility policies, with a line for the hitting rate and another for
	 * the minimum sales premium
	 */
	private TD buildSpecialTaxTD(ArrayList<String> taxes, int taxWidth) {

		TD content = new TD();
		content.setColSpan(1);

		String[] varsArray = { WHITESPACE, WHITESPACE };

		if (taxes != null) {
			varsArray = taxes.get(0).split(Pattern.quote(ESCAPE_CHARACTER));
		}

		Table table;
		TR[] tableRows = new TR[2];

		TD[] cell = new TD[1];

		cell[0] = safeBuildCell(varsArray[0], TypeDefGUIDs.T_String, true,
				true);
		cell[0].setWidth(taxWidth);
		cell[0].setStyle("overflow:hidden;white-space:nowrap");

		tableRows[0] = ReportBuilder.buildRow(cell);
		ReportBuilder.styleRow(tableRows[0], false);

		cell = new TD[1];
		cell[0] = safeBuildCell(varsArray[1], TypeDefGUIDs.T_String, false, true);
		cell[0].setWidth(taxWidth);
		cell[0].setStyle("overflow:hidden;white-space:nowrap;border-top:1px solid #3f6d9d;");

		tableRows[1] = ReportBuilder.buildRow(cell);
		ReportBuilder.styleRow(tableRows[1], false);

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);

		content.addElement(new Div().addElement(table).setStyle(
				"width:inherit;"));
		ReportBuilder.styleInnerContainer(content);

		return content;
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
	 * This method gets the policy's coverages (or some other information that
	 * someone decided to display in the coverages' column, because...
	 * "what's logic?")
	 */
	private String[] getPolicyCoverages(Policy policy,
			PolicyObject insuredObject, PolicyValue[] policyValues) throws BigBangJewelException {

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policyLine = policy.GetSubLine().getLine().getKey();

		// If policy is RESPONSIBILITY
		if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)) {
			try {
				ArrayList<String> coverages = new ArrayList<String>();
				String top = policy.GetSubLine().getLine().getCategory()
						.getLabel()
						+ " / " + policy.GetSubLine().getLine().getLabel();
				if (top.length() > COVERAGES_BREAK_POINT) {
					ArrayList<String> tmpArray = new ArrayList<String>();
					tmpArray.add(top);
					tmpArray = splitValue(tmpArray,
							COVERAGES_BREAK_POINT);
					for (int u = 0; u < tmpArray.size(); u++) {
						coverages.add(tmpArray.get(u));
					}
				} else {
					coverages.add(top);
				}
				String bottom = getValueWithTags(policyValues, insuredObject,
						null, null, Constants.PolicyValuesTags.ACTIVITY, false,
						false);
				if (bottom.length() > COVERAGES_BREAK_POINT) {
					ArrayList<String> tmpArray = new ArrayList<String>();
					tmpArray.add(bottom);
					tmpArray = splitValue(tmpArray,
							COVERAGES_BREAK_POINT);
					for (int u = 0; u < tmpArray.size(); u++) {
						coverages.add(tmpArray.get(u));
					}
				} else {
					coverages.add(bottom);
				}
				return coverages.toArray(new String[coverages.size()]);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the responsibility coverages for responsibility policy "
								+ policy.getLabel(), e);
			}
		}

		// If policy is divers/Caution
		if (policyCat.equals(Constants.PolicyCategories.DIVERS)
				&& policyLine.equals(Constants.PolicyLines.GUARANTEE)) {
			try {
				ArrayList<String> coverages = new ArrayList<String>();
				String top = getValueWithTags(policyValues, null, null, null,
						Constants.PolicyValuesTags.TYPE_GUARANTEE, true, false);
				if (top.length() > COVERAGES_BREAK_POINT) {
					ArrayList<String> tmpArray = new ArrayList<String>();
					tmpArray.add(top);
					tmpArray = splitValue(tmpArray,
							COVERAGES_BREAK_POINT);
					for (int u = 0; u < tmpArray.size(); u++) {
						coverages.add(tmpArray.get(u));
					}
				} else {
					coverages.add(top);
				}
				String bottom = getValueWithTags(policyValues, null, null,
						null, Constants.PolicyValuesTags.DESCRIPTION, true,
						false);
				if (bottom.length() > COVERAGES_BREAK_POINT) {
					ArrayList<String> tmpArray = new ArrayList<String>();
					tmpArray.add(bottom);
					tmpArray = splitValue(tmpArray,
							COVERAGES_BREAK_POINT);
					for (int u = 0; u < tmpArray.size(); u++) {
						coverages.add(tmpArray.get(u));
					}
				} else {
					coverages.add(bottom);
				}
				return coverages.toArray(new String[coverages.size()]);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the divers coverages for divers policy "
								+ policy.getLabel(), e);
			}
		}

		try {
			PolicyCoverage[] policyCoverages = policy.GetCurrentCoverages();

			if (policyCoverages == null || policyCoverages.length == 0) {
				String[] result = { WHITESPACE };
				return result;
			}
			ArrayList<String> coverages = new ArrayList<String>();
			for (int i = 0; i < policyCoverages.length; i++) {
				// Only inserts the coverages present at the policy
				if (policyCoverages[i].IsPresent() != null
						&& policyCoverages[i].IsPresent()) {
					coverages.add((String) policyCoverages[i].GetCoverage()
							.getAt(Coverage.I.NAME));
				}
			}
			if (coverages.size() > 0) {
				return coverages.toArray(new String[0]);
			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while getting the general case coverages for policy "
					+ policy.getLabel(), e);
		}
		
		String emptyResult[] = {WHITESPACE}; 
		return emptyResult;
	}
	
	/**
	 * This method gets the risk site (if any) according to the policy's
	 * category
	 */
	private String getRiskSite(Policy policy, PolicyObject insuredObject,
			PolicyValue[] policyValues) throws InvocationTargetException,
			JewelEngineException, BigBangJewelException {

		if (policyValues == null) {
			return WHITESPACE;
		}

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policyLine = policy.GetSubLine().getLine().getKey();

		// Cases in which the risk site comes from the country value
		if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)
				&& hasTerritoryExtent(policy)) {
			try {
				return getValueWithTags(policyValues, insuredObject, null,
						null, Constants.PolicyValuesTags.COUNTRY, false, false);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the risk site from country for policy "
								+ policy.getLabel(), e);
			}
		}
				
		// Cases in which the risk site comes from the object address
		if ((policyCat.equals(Constants.PolicyCategories.MULTIRISK) ||
				policyCat.equals(Constants.PolicyCategories.DISEASE) ||
				policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)
				|| (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY) && policyLine
						.equals(Constants.PolicyLines.RESPONSIBILITY_ENVIRONMENTAL)) || (policyCat
				.equals(Constants.PolicyCategories.DIVERS) && policyLine
				.equals(Constants.PolicyLines.GUARANTEE)))
				&& insuredObject != null) {
			try {
				return getObjectAddress(insuredObject);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the risk site from object's address for policy "
								+ policy.getLabel(), e);
			}
		}
		
		// Cases in which the risk site comes from the territorial scope
		if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)
				|| (policyCat
						.equals(Constants.PolicyCategories.PERSONAL_ACCIDENTS) && policyLine
						.equals(Constants.PolicyLines.PERSONAL_ACC_TRADICIONAL))
				|| (policyCat.equals(Constants.PolicyCategories.DIVERS) && policyLine
						.equals(Constants.PolicyLines.DIVERS_ASSISTANCE))
				|| policyCat.equals(Constants.PolicyCategories.LIFE)
				|| (policyCat.equals(Constants.PolicyCategories.OTHER_DAMAGES) && policyLine
						.equals(Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL))
				|| policyCat.equals(Constants.PolicyCategories.TRANSPORTED_GOODS)) {
			try {
				return getValueWithTags(policyValues, insuredObject, null,
						null, Constants.PolicyValuesTags.TERRITORIAL_SCOPE,
						false, false);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the risk site from territorial scope for policy "
								+ policy.getLabel(), e);
			}
		}

		// Cases in which the risk site comes from the itinerary
		if (policyCat.equals(Constants.PolicyCategories.PERSONAL_ACCIDENTS)
				&& policyLine.equals(Constants.PolicyLines.PERSONAL_ACC_TRIP)) {
			try {
				return getValueWithTags(policyValues, insuredObject, null,
						null, Constants.PolicyValuesTags.ITINERARY, true, false);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the risk site from itinerary for policy "
								+ policy.getLabel(), e);
			}
		}
		
		// Cases in which the risk site comes from the contract name
		if (policyCat.equals(Constants.PolicyCategories.CONSTRUCTION_ASSEMBLY)) {
			try {
				return getValueWithTags(policyValues, null, null,
						null, Constants.PolicyValuesTags.CONTRACT_NAME, true,
						false);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the risk site from contract name for policy "
								+ policy.getLabel(), e);
			}
		}

		// Cases in which the risk site comes from the navigation zone
		if (policyCat.equals(Constants.PolicyCategories.MARINE_VESSELS)) {
			try {
				return getValueWithTags(policyValues, insuredObject, null,
						null, Constants.PolicyValuesTags.NAVIGATION_ZONE, true,
						false);
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the risk site from navigation zone for policy "
								+ policy.getLabel(), e);
			}
		}

		return NO_VALUE;
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
		UUID policyLine = policy.GetSubLine().getLine().getKey();

		ArrayList<String> result = new ArrayList<String>();

		// Case in which the value comes from the yealy salary
		if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)
				&& ((policySubLine
						.equals(Constants.PolicySubLines.WORK_ACCIDENTS_OTHERS_FIXED_PREMIUM)) || (policySubLine
						.equals(Constants.PolicySubLines.WORK_ACCIDENTS_CGA_FIXED_PREMIUM)))) {

			try {
				String val = getValueWithTags(policyValues, insuredObject,
						currentExercise, null,
						Constants.PolicyValuesTags.YEARLY_SALARY, true, false);
				if (val == null) {
					val = WHITESPACE;
				}
				result.add(val);
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the insured value from yearly salary for policy "
								+ policy.getLabel(), e);
			}
		}

		// Case in which the value comes from the temporary value from the legal
		// coverage
		if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)) {
			try {
				String valueWithTags = getValueWithTags(policyValues,
						insuredObject, currentExercise,
						Constants.PolicyCoveragesTags.LEGAL,
						Constants.PolicyValuesTags.TEMPORARY_VALUE, false,
						false);
				if (valueWithTags == null || valueWithTags.length() == 0) {
					valueWithTags = getValueWithTags(policyValues,
							insuredObject, currentExercise,
							Constants.PolicyCoveragesTags.LEGAL,
							Constants.PolicyValuesTags.VALUE, false, false);
				}
				if (valueWithTags == null) {
					valueWithTags = WHITESPACE;
				}
				result.add(valueWithTags);
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the insured value from legal coverage for policy "
								+ policy.getLabel(), e);
			}
		}
		
		// Case in which the value comes from the compensation
		if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)
				&& policyLine
						.equals(Constants.PolicyLines.RESPONSIBILITY_ENVIRONMENTAL)) {
			try {
				String val = getValueWithTags(policyValues, insuredObject,
						currentExercise, null,
						Constants.PolicyValuesTags.COMPENSATION_LIMIT, false,
						false);
				if (val == null) {
					val = WHITESPACE;
				}
				result.add(val);
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the insured value fromthe compensation limity for policy "
								+ policy.getLabel(), e);
			}
		}

		// Case in which the value comes from the insured value in the policy
		// header
		if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)
				|| (policyCat.equals(Constants.PolicyCategories.DIVERS) && policyLine
						.equals(Constants.PolicyLines.GUARANTEE))) {
			try {
				String val = getValueWithTags(policyValues, insuredObject,
						currentExercise, null,
						Constants.PolicyValuesTags.VALUE, true, false);
				result.add(val);
				if (val == null) {
					val = WHITESPACE;
				}
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the insured value from capital for policy "
								+ policy.getLabel(), e);
			}
		}

		// Case in which the value comes from the maximum transportation limit
		if (policyCat.equals(Constants.PolicyCategories.TRANSPORTED_GOODS)) {
			try {
				String val = getValueWithTags(policyValues, insuredObject,
						currentExercise, null,
						Constants.PolicyValuesTags.TRANSPORTATION_LIMIT, false,
						false);
				if (val == null) {
					val = WHITESPACE;
				}
				result.add(val);
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the insured value from transportation limit for policy "
								+ policy.getLabel(), e);
			}
		}

		/*
		 * "Default case" - to each coverage corresponds a given insured value
		 */
		for (int i = 0; i < policyCoverages.length; i++) {
			try {
				// Only lists the values for the coverages present at the policy
				if (policyCoverages[i].IsPresent() != null
						&& policyCoverages[i].IsPresent()) {
					String value = getCoverageValues(insuredObject,
									currentExercise, policyValues, policyCoverages[i],
									Constants.PolicyValuesTags.VALUE, false);
					if (value!=null && !value.equals(NO_VALUE) && value.trim().startsWith("-")) {
						value = MULTIPLE_M;
					}
					result.add(value);
				}
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the insured value from coverages for policy "
								+ policy.getLabel(), e);
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
			PolicyValue[] policyValues, PolicyCoverage[] policyCoverages) throws BigBangJewelException {

		ArrayList<String> result = new ArrayList<String>();

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policyLine = policy.GetSubLine().getLine().getKey();

		// Case in which the tax comes from the sales'tax
		if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)
				|| policyCat.equals(Constants.PolicyCategories.LIFE)) {
			try {
				String tax = getValueWithTags(policyValues, insuredObject,
						currentExercise, null,
						Constants.PolicyValuesTags.SALES_TAX, true, true);
				if (tax == null) {
					tax = WHITESPACE;
				} else {
					tax = tax.replace(",", ".");
				}
				result.add(tax);
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the tax from sales'tax for policy "
								+ policy.getLabel(), e);
			}
		}

		// Case in which the tax comes from the Sales' Premium
		if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)
				&& policyLine
						.equals(Constants.PolicyLines.RESPONSIBILITY_ENVIRONMENTAL)) {
			try {
				String policyPremium = policy.getAt(Policy.I.PREMIUM) == null ? WHITESPACE
						: policy.getAt(Policy.I.PREMIUM).toString() + " "
								+ Utils.getCurrency();
				return new ArrayList<String>(Arrays.asList(policyPremium));
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the tax from the policy's premium for environmental policies, for policy "
								+ policy.getLabel(), e);
			}
		}

		// Case in which the tax comes from the hitting rate and minimum sales'
		// premium
		if (policyCat.equals(Constants.PolicyCategories.RESPONSIBILITY)) {
			try {
				String hitRateTmp = NO_VALUE;
				// It only gets the hitting rate when possible (the user did not defined "Sem taxa de acerto" as the hitting rate)
				if (!hasNoHittingRate(policyValues)) {
					hitRateTmp = getValueWithTags(policyValues,
							insuredObject, currentExercise, null,
							Constants.PolicyValuesTags.HITTING_RATE, true, false);
					if (hitRateTmp == null) {
						hitRateTmp = WHITESPACE;
					} else {
						hitRateTmp = hitRateTmp.replace(",", ".");
						String taxType = getValueWithTags(policyValues,
								insuredObject, currentExercise, null,
								Constants.PolicyValuesTags.TAX_TYPE, true, false);
						if (taxType!=null) {
							taxType = taxType.equals("o/oo") ? "‰" : taxType.equals("o/o") ? "%" : taxType;
							hitRateTmp = hitRateTmp + " " + taxType;
						}
					}
				}
				String minPremimTmp = getValueWithTags(policyValues,
						insuredObject, currentExercise, null,
						Constants.PolicyValuesTags.MIN_SALES_PREMIUM, true,
						false);
				if (minPremimTmp == null) {
					minPremimTmp = WHITESPACE;
				}
				result.add(minPremimTmp + ESCAPE_CHARACTER + hitRateTmp);
				return result;
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the hit rate and minimum premium for policy "
								+ policy.getLabel(), e);
			}
		}
		
		boolean anyCovFound = false;

		// Case in which the tax is referent to a coverage
		for (int i = 0; i < policyCoverages.length; i++) {
			if (policyCoverages[i].IsPresent() != null
					&& policyCoverages[i].IsPresent()) {
				try {
					String taxValue = getCoverageValues(insuredObject,
							currentExercise, policyValues, policyCoverages[i],
							Constants.PolicyValuesTags.SALES_TAX, true);
					if (taxValue == null || taxValue.trim().length()==0) {
						taxValue = WHITESPACE;
					} else if (!taxValue.equals(NO_VALUE)) {
						taxValue = taxValue.replace(",", ".");
						anyCovFound = true;
					}
					result.add(taxValue);
				} catch (Throwable e) {
					throw new BigBangJewelException(e.getMessage()
							+ " Error while getting the tax value for "
							+ policyCoverages[i].getLabel()
							+ " coverage for policy " + policy.getLabel(), e);
				}
			}
		}

		// Case in which it should check to see if all values are the same, case
		// in which they should be replaced by a single value
		if ((policyCat.equals(Constants.PolicyCategories.MULTIRISK) || (policyCat
				.equals(Constants.PolicyCategories.OTHER_DAMAGES) && (policyLine
				.equals(Constants.PolicyLines.OTHER_DAMAGES_MACHINE_BREAKDOWN)
				|| policyLine
						.equals(Constants.PolicyLines.OTHER_DAMAGES_MACHINE_HULL)
				|| policyLine
						.equals(Constants.PolicyLines.OTHER_DAMAGES_LEASING) || policyLine
					.equals(Constants.PolicyLines.ELECTRONIC_EQUIPMENT))))) {
			try {
				boolean allExist = true;
				boolean allEqual = true;
				boolean ran = false;
				String oldTemp = "";
				for (String temp : result) {
					if (!temp.equals(oldTemp) && ran) {
						allEqual = false;
					}
					if (temp.equals(WHITESPACE)) {
						allExist = false;
					}
					oldTemp = temp;
					ran = true;
				}
				if (allExist && allEqual && result.size()>0 && !result.get(0).equals(NO_VALUE)) {
					result.clear();
					result.add(oldTemp);
					return result;
				}
				if (allExist && !allEqual) {
					result.clear();
					result.add(MULTIPLE_F);
					return result;
				}
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage()
						+ " Error while getting the aggregated tax for policy "
						+ policy.getLabel(), e);
			}
		}

		// If no tax was found, returns the value from the policy premium
		if (!anyCovFound) {
			try {
				String policyPremium = policy.getAt(Policy.I.PREMIUM) == null ? WHITESPACE
						: policy.getAt(Policy.I.PREMIUM).toString() + " "
								+ Utils.getCurrency();
				return new ArrayList<String>(Arrays.asList(policyPremium));
			} catch (Throwable e) {
				throw new BigBangJewelException(
						e.getMessage()
								+ " Error while getting the tax from the policy's premium for policy "
								+ policy.getLabel(), e);
			}
		}

		if (result.size() > 0) {
			return result;
		}

		// Default - no tax
		return new ArrayList<String>(Arrays.asList(NO_VALUE));
	}
	
	/**
	 * This method gets the taxes according to the category. If they are related
	 * with the coverages, they are returned in the same order than the
	 * coverages, with a white space when the value does not exist for a given
	 * coverage Note that there are some categories without an associated tax
	 */
	private ArrayList<String> getFranchises(Policy policy,
			PolicyObject insuredObject, UUID currentExercise,
			PolicyValue[] policyValues, PolicyCoverage[] policyCoverages) throws BigBangJewelException {

		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		
		ArrayList<String> result = new ArrayList<String>();
		
		// Case in which the franchise should read only '-'
		if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)) {
			return new ArrayList<String>(Arrays.asList(NO_VALUE));
		}
		
		// Case in which the franchise should read only 'DIVERSAS'
		if (policyCat.equals(Constants.PolicyCategories.MULTIRISK)) {
			return new ArrayList<String>(Arrays.asList(MULTIPLE_F));
		}
		
		// Case in which to each coverage corresponds a franchise, or '-' if there is no franchise field related to the coverage
		for (int i = 0; i < policyCoverages.length; i++) {
			// Only lists the values for the coverages present at the policy
			if (policyCoverages[i].IsPresent()!=null && policyCoverages[i].IsPresent()) {
				String value = getCoverageValues(insuredObject,
						currentExercise, policyValues, policyCoverages[i],
						Constants.PolicyValuesTags.FRANCHISE, false);
				
				if (value==null) {
					value = WHITESPACE;
				}
				result.add(value);
			}
		}
		if (result.size() == 0) {
			// Default - no tax
			return new ArrayList<String>(Arrays.asList(NO_VALUE));
		}
		return result;
	}
	
	/**
	 * Given a policy, this method checks if it has multiple insured objects
	 */
	private boolean hasMultipleObjects(Policy policy)
			throws BigBangJewelException {

		PolicyObject[] policyObjects = policy.GetCurrentObjects();

		if (policyObjects == null) {
			return false;
		} else if (policyObjects.length > 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Given a policy, this method returns the number of objects
	 */
	private int getObjNumber(Policy policy)
			throws BigBangJewelException {

		PolicyObject[] policyObjects = policy.GetCurrentObjects();

		if (policyObjects == null) {
			return 0;
		} else if (policyObjects.length > 1) {
			return policyObjects.length;
		}
		return 1;
	}
	
	/**
	 * This method returns a built and ready to display address as associated
	 * with a given insured object
	 */
	private String getObjectAddress(PolicyObject insuredObject)
			throws BigBangJewelException {

		String address = (String) (insuredObject.getAt(PolicyObject.I.ADDRESS1) == null ? ""
				: insuredObject.getAt(PolicyObject.I.ADDRESS1));
		address = (String) (insuredObject.getAt(PolicyObject.I.ADDRESS2) == null ? address
				: address + " " + insuredObject.getAt(PolicyObject.I.ADDRESS2));

		UUID zipGUID = (UUID) insuredObject.getAt(PolicyObject.I.ZIPCODE);

		try {
			if (zipGUID != null) {

				ObjectBase zipCode = Engine.GetWorkInstance(
						Engine.FindEntity(Engine.getCurrentNameSpace(),
								ObjectGUIDs.O_PostalCode), zipGUID);
				address = address + " " + zipCode.getAt(0);
				address = address + " " + zipCode.getAt(1);
			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while getting object address from policy "
					+ insuredObject.getAt(PolicyObject.I.POLICY), e);
		}

		return address;
	}
	
	/**
	 * This method iterates the policy values and returns the one with a given
	 * coverage's tag and a given tax's tag (if any)
	 */
	private String getValueWithTags(PolicyValue[] policyValues,
			PolicyObject insuredObject, UUID exerciseId, String coverageTag,
			String taxTag, boolean isHeader, boolean displayUnit) throws BigBangJewelException {

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
						return getValueMindingUnit(policyValues[i], displayUnit);
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
							return getValueMindingUnit(policyValues[i], displayUnit);
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
							return getValueMindingUnit(policyValues[i], displayUnit);
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
					return getValueMindingUnit(policyValues[i], displayUnit);
				}
			}
		}
		return WHITESPACE;
	}
	
	/**
	 * Checks if a given policy has the Extended Territory Coverage
	 */
	private boolean hasTerritoryExtent(Policy policy)
			throws BigBangJewelException {

		PolicyCoverage[] coverages = policy.GetCurrentCoverages();

		if (coverages == null || coverages.length == 0) {
			return false;
		}

		for (int i = 0; i < coverages.length; i++) {
			// Only inserts the coverages present at the policy
			if (coverages[i].IsPresent() != null
					&& coverages[i].IsPresent()
					&& coverages[i].GetCoverage().getAt(
							Coverage.I.TAG) != null
					&& ((String) coverages[i].GetCoverage().getAt(
							Coverage.I.TAG))
							.equals(Constants.PolicyCoveragesTags.TERRITORIAL)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method iterates the policy values and returns the one with a given
	 * tax's tag, corresponding to a given coverage
	 */
	private String getCoverageValues(PolicyObject insuredObject,
			UUID currentExercise, PolicyValue[] policyValues,
			PolicyCoverage policyCoverage, String valueTag, boolean displayUnit) throws BigBangJewelException {

		UUID objectID = insuredObject == null ? null : insuredObject.getKey();
		
		// First, it needs to check if the tax with the given tag is present for the coverage.
		// If it isn't, it simply returns a '-'
		if (!isTaxAssociated(policyCoverage, valueTag)) {
			return NO_VALUE;
		}

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
				return getValueMindingUnit(policyValues[u], displayUnit);
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
					return getValueMindingUnit(policyValues[u], displayUnit);
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
					return getValueMindingUnit(policyValues[u], displayUnit);
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
				return getValueMindingUnit(policyValues[u], displayUnit);
			}
		}

		return WHITESPACE;
	}
	
	/**
	 * This method checks if the hit rate incidence has been defined as
	 * "No hitting rate"
	 */
	private boolean hasNoHittingRate(PolicyValue[] policyValues) {

		for (int u = 0; u < policyValues.length; u++) {
			if (policyValues[u].GetTax().GetTag() != null
					&& policyValues[u]
							.GetTax()
							.GetTag()
							.equals(Constants.PolicyValuesTags.HIT_RATE_INCIDENCE)) {
				if (policyValues[u].GetValue() != null) {
					if (((String) policyValues[u].GetValue())
							.equals(Constants.ValuesConstants.HIT_RATE_INCIDENCE_NO_VALUE)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * This method returns a giving value, adding the display unit when
	 * requested
	 */
	private String getValueMindingUnit(PolicyValue val, boolean displayUnit)
			throws BigBangJewelException {
		if (val == null) {
			return WHITESPACE;
		}
		if (val.GetTax() != null
				&& val.GetTax().getAt(2) != null
				&& Constants.FieldID_List.equals(val.GetTax().getAt(2))) {
			return getListValue(val.GetTax().getKey().toString(),
					(String) val.GetValue());
		}

		String value = val.GetValue();
		if (displayUnit && value != null && value.length() != 0) {
			value = val.GetTax().GetUnitsLabel() == null ? value : value
					+ val.GetTax().GetUnitsLabel();
		}
		if (value == null) {
			value = WHITESPACE;
		}
		return value;
	}

	/**
	 * This method returns the textual value for a given reference from a list
	 * of values
	 */
	private String getListValue(String key, String value)
			throws BigBangJewelException {

		if (key == null || value == null || key.length() == 0
				|| value.length() == 0) {
			return WHITESPACE;
		}

		MasterDB ldb;
		ResultSet returnedVals;
		String result = WHITESPACE;

		StringBuilder strSQL = new StringBuilder();
		strSQL.append("SELECT ITEMVALUE FROM credite_egs.tblPolicyValueItems where FKTaxAsSubList = '"
				+ key + "' AND PK = '" + value + "'");

		// gets the DB
		try {
			ldb = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(
					e.getMessage()
							+ " Error while getting the DB reference while getting a value from a list.",
					e);
		}

		// fetches the Value
		try {
			returnedVals = ldb.OpenRecordset(strSQL.toString());
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage()
					+ " Error while getting list value.", e);
		}

		// Gets the value
		try {
			if (returnedVals.next()) {
				result = returnedVals.getString("ITEMVALUE");
			}
		} catch (SQLException e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while fetching list value.", e);
		}

		// Cleanup
		try {
			returnedVals.close();
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage()
					+ " Error while closing the policies' result set.", e);
		}
		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while disconnecting from the DB.", e);
		}
		return result;
	}
	
	/**
	 * This method iterates the taxes for a given coverage, and checks if there
	 * is one corresponding to the valueTag. If there isn't, of course there
	 * will not exist a value for that tax, ence a '-' should be returned.
	 */
	private boolean isTaxAssociated(PolicyCoverage policyCoverage,
			String valueTag) throws BigBangJewelException {

		if (policyCoverage == null || valueTag == null) {
			return false;
		}

		Tax[] policyCoveragesTaxes;
		try {
			policyCoveragesTaxes = policyCoverage.GetCoverage()
					.GetCurrentTaxes();
		} catch (BigBangJewelException e) {
			throw new BigBangJewelException(e.getMessage()
					+ " Error while getting the taxes from a coverage.", e);
		}
		for (Tax tx : policyCoveragesTaxes) {
			if (tx.GetTag() != null && tx.GetTag().equals(valueTag)) {
				return true;
			}
		}
		return false;
	}

}
