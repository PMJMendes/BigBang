package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;

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
 * Class responsible for the creation of the Client's Portfolio Report
 */
public class PolicyPortfolioClient extends PolicyListingsBase {
	
	private BigDecimal premiumTotal;
	
	/** 
	 * The method responsible for creating the report
	 */
	public GenericElement[] doReport(String[] reportParams)
		throws BigBangJewelException {
		
		Policy[] policies;
		GenericElement[] reportResult;

		// Gets the policies from the DB
		policies = getPortfolio(reportParams);

		// Creates the report (this one only has on section)
		reportResult = new GenericElement[1];
		reportResult[0] = buildClientPortfolioTable("Relatório de Carteira de Clientes", policies, reportParams);

		return reportResult;
	}
	
	/** 
	 * The method responsible for printing the table corresponding to a client's portfolio
	 */
	protected Table buildClientPortfolioTable(String headerTitle, Policy[] policies, String[] reportParams)
		throws BigBangJewelException {
			
		Table table;
		TR[] tableRows;
		TD mainContent;
		int rowNum = 0;
		String clientOrGroupTitle;
		HashMap<String, ArrayList<Policy>> policiesMap;

		tableRows = new TR[5];
		premiumTotal = BigDecimal.ZERO;
		
		// Creates a map with the policies separated by "ramo"
		policiesMap = getPoliciesMap(policies);

		// Gets the table row with the report's name
		tableRows[rowNum++] = ReportBuilder.constructDualHeaderRowCell(headerTitle);
		
		// Gets the client's or group's name (if any) and creates the corresponding row
		clientOrGroupTitle = getClientOrGroup(reportParams);
		if (clientOrGroupTitle != null) {
			tableRows[rowNum++] = ReportBuilder.constructDualIntermediateRowCell(clientOrGroupTitle);
		}

		// Builds the row corresponding to the "real content" of the report - meaning... the values from the policies
		mainContent = new TD();
		mainContent.setColSpan(2);
		mainContent.addElement(buildReportInfo(policiesMap, policies.length, reportParams));
		ReportBuilder.styleInnerContainer(mainContent);
		tableRows[rowNum++] = ReportBuilder.buildRow(new TD[] {mainContent});

		// Builds the row with the total number of policies
		tableRows[rowNum++] = ReportBuilder.constructDualRow("Nº de Apólices", policies.length, TypeDefGUIDs.T_Integer, false);

		// Build the row with the total prize
		if (reportParams[10].equals("1")) {
			tableRows[rowNum++] = ReportBuilder.constructDualRow("Total de Prémios", premiumTotal, TypeDefGUIDs.T_Decimal, false);
		}

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, false);

		return table;
	}
	
	/** 
	 * This method creates an HashMap with policies grouped by "ramo"
	 */
	private HashMap<String, ArrayList<Policy>> getPoliciesMap(Policy[] policies) {
		
		HashMap<String, ArrayList<Policy>> policiesMap;
		String policySubline; 
		policiesMap = new HashMap<String, ArrayList<Policy>>();
		
		for(int i = 0; i < policies.length; i++) {
			policySubline = policies[i].GetSubLine().getDescription();
			if (policiesMap.get(policySubline) == null) {
				policiesMap.put(policySubline, new ArrayList<Policy>());
			}
			policiesMap.get(policySubline).add(policies[i]);
		}
		
		return policiesMap;
	}
	
	/** 
	 * This method gets the client name or group name to display in the second row in the table
	 */
	private String getClientOrGroup (String[] reportParams) throws BigBangJewelException {
		
		String displayName;
		
		// If the client's name is defined, it always displays the client's name. If the group name is defined, it is displayed
		if ( reportParams[0] != null ) {
			Client client = Client.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(reportParams[0]));
			displayName = (String) client.getAt(Client.I.NAME);
		} else if ( reportParams[1] != null ) {
			ClientGroup clientGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(reportParams[1]));
			displayName = (String) clientGroup.getAt(ClientGroup.I.NAME);
		} else {
			displayName = null;
		}
		
		return displayName;
	}

	/** 
	 * The method responsible for building the rows in the report with the policies' info
	 */
	protected Table buildReportInfo(HashMap<String, ArrayList<Policy>> policiesMap, 
									int numberOfPolicies, String[] reportParams) throws BigBangJewelException {
		
		Table table;
		TR[] tableRows  = new TR[numberOfPolicies + 1 + policiesMap.size()];
		int rowNum= 0;
		
		// Builds the row with the column names
		tableRows[rowNum++] = ReportBuilder.buildRow(buildInnerHeaderRow(reportParams));
		ReportBuilder.styleRow(tableRows[0], true);
		
		// Iterates the hashmap with the several policies by "ramo", and creates a line 
		// with the name of that "ramo", followed by the information about the policies
		for (String subline: policiesMap.keySet()) {
			tableRows[rowNum++] = buildSublineRow(subline, reportParams);
			Object[] policies = policiesMap.get(subline).toArray();
			
			for (int i = 0; i < policies.length; i++) {
				tableRows[rowNum] = ReportBuilder.buildRow(buildRow((Policy)policies[i], reportParams));
				ReportBuilder.styleRow(tableRows[rowNum++], false);
			}
		}
		
		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);
		
		return table;
	}

	/** 
	 * This method builds the row with the information about the preceding policies' "ramo"
	 */
	private TR buildSublineRow(String subline, String[] reportParams) {
		
		TD rowContent;
		TR row;
		
		// Builds the TD with the content
		rowContent = ReportBuilder.buildCell("Ramo: " + subline, TypeDefGUIDs.T_String);
		rowContent.setColSpan(10);
		ReportBuilder.styleCell(rowContent, true, false);
		
		// Builds the TR encapsulating the TD 
		row = ReportBuilder.buildRow(new TD[] {rowContent});
		row.setStyle("height:35px; font-weight:bold;");
		
		return row;
	}
	
	/** 
	 * This method is responsible for building a row with information about a given policy
	 */
	protected TD[] buildRow(Policy policy, String[] reportParams) throws BigBangJewelException {
		
		Company company;
		String maturity;
		UUID currentExercise;
		PolicyObject[] policyObjects;
		HashMap<UUID, CoverageData> valuesByObject;
		String riskSite = "";
		
		int cellNumber = Collections.frequency(Arrays.asList(reportParams), "1");
		TD[] dataCells = new TD[cellNumber];
		cellNumber = 0;
		int paramCheck = 2;
		
		currentExercise = getCurrentExercise(policy.GetCurrentExercises());
		
		company = policy.GetCompany();
		maturity = ((policy.getAt(Policy.I.MATURITYMONTH) == null) || 
					((Integer) policy.getAt(Policy.I.MATURITYMONTH) == -1) ||
					(policy.getAt(Policy.I.MATURITYDAY) == null) ||
					((Integer) policy.getAt(Policy.I.MATURITYDAY) == -1)) ?
							null :
							policy.getAt(Policy.I.MATURITYMONTH).toString() + " / " + 
							policy.getAt(Policy.I.MATURITYDAY).toString();
		
		policyObjects = policy.GetCurrentObjects();
		
		valuesByObject = getValuesByObject(policy, currentExercise);

		// Increases the value for the total premium
		if (policy.getAt(Policy.I.TOTALPREMIUM) != null) {
			premiumTotal = premiumTotal.add((BigDecimal)policy.getAt(Policy.I.TOTALPREMIUM));
		}
		
		// Policy Number
		if (reportParams[paramCheck++].equals("1")) {
			dataCells[cellNumber] = ReportBuilder.buildCell(policy.getLabel(), TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
		}
		
		// Insurance Company
		if (reportParams[paramCheck++].equals("1")) {
			dataCells[cellNumber] = ReportBuilder.buildCell(company.getAt(Company.I.NAME), TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
		}
		
		// Maturity
		if (reportParams[paramCheck++].equals("1")) {
			if (maturity == null) {
				dataCells[cellNumber] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
			} else {
				dataCells[cellNumber] = ReportBuilder.buildCell(maturity, TypeDefGUIDs.T_String);
			}
			ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
		}
		
		// Iterates Policy Objects 
		if (policyObjects.length == 0) {
			
			// Empty policyObjects column
			if (reportParams[paramCheck++].equals("1")) {
				dataCells[cellNumber] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
				ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
			}
			
			// Policy Coverages
			if (reportParams[paramCheck++].equals("1")) {
				dataCells[cellNumber] = buildCoverageRelatedTable(valuesByObject.get(UUID.fromString("0")).getCoverages());
				ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
			}
			
			// "Local de Risco" - null
			if (reportParams[paramCheck++].equals("1")) {
				dataCells[cellNumber] = ReportBuilder.buildCell("-", TypeDefGUIDs.T_String);
				ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
			}
			
			// Insured Value
			if (reportParams[paramCheck++].equals("1")) {
				dataCells[cellNumber] = buildCoverageRelatedTable(valuesByObject.get(UUID.fromString("0")).getInsuredValues());
				ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
			}
			
			// Tax
			if (reportParams[paramCheck++].equals("1")) {
				dataCells[cellNumber] = buildCoverageRelatedTable(valuesByObject.get(UUID.fromString("0")).getTaxes());
				ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
			}
		} else {
			
			for (int i=0; i<policyObjects.length; i++) {
				
				riskSite = getRiskSite(policy, policyObjects[i]);
				
				UUID objectKey = policyObjects[i].getKey();
				
				// Policy Object name
				if (reportParams[paramCheck++].equals("1")) {
					String objectName = getObjectName(policy, policyObjects[i]);
					dataCells[cellNumber] = ReportBuilder.buildCell(objectName, TypeDefGUIDs.T_String);
					ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
				}
				
				// Policy Coverages
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[cellNumber] = buildCoverageRelatedTable(valuesByObject.get(objectKey).getCoverages());
					ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
				}
				
				// "Local de Risco"
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[cellNumber] = ReportBuilder.buildCell(riskSite, TypeDefGUIDs.T_String);
					ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
				}
				
				// Insured Value
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[cellNumber] = buildCoverageRelatedTable(valuesByObject.get(objectKey).getInsuredValues());
					ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
				}
				
				// Tax
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[cellNumber] = buildCoverageRelatedTable(valuesByObject.get(objectKey).getTaxes());
					ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
				}
			}
		}
		
		// Total Premium
		if (reportParams[paramCheck++].equals("1")) {
			if (policy.getAt(Policy.I.TOTALPREMIUM) == null) {
				dataCells[cellNumber] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
			} else {
				dataCells[cellNumber] = ReportBuilder.buildCell(policy.getAt(Policy.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal);
			}
			ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
		}
		
		// Notes - intentionally left blank
		if (reportParams[paramCheck++].equals("1")) {
			dataCells[cellNumber] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(dataCells[cellNumber++], true, true);
		}
		
		setWidths(dataCells, reportParams);

		return dataCells;
	}

	private String getObjectName(Policy policy, PolicyObject policyObject) {
		
		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();

		// Risk site differs with different policies' categories
		if (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE) &&
			policySubLine.equals(Constants.PolicySubLines.AUTO_INDIVIDUAL)) {
			return (String) policyObject.getAt(PolicyObject.I.MAKEANDMODEL);
		} else {
			return (String) policyObject.getAt(PolicyObject.I.NAME);
		}
	}

	private HashMap<UUID, CoverageData> getValuesByObject(
			Policy policy, UUID currentExercise) throws BigBangJewelException {
		
		PolicyObject[] objects;
		PolicyValue[] values;
		PolicyCoverage[] coverages;
		
		HashMap<UUID, CoverageData> result = new HashMap<UUID, CoverageData>();
		
		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		UUID policySubLine = policy.GetSubLine().getKey();
		
		objects = policy.GetCurrentObjects();
		coverages = policy.GetCurrentCoverages();
		
		CoverageData policyCoverage = new CoverageData();
		
		// Special Cases
		if (policyCat.equals(Constants.PolicyCategories.AUTOMOBILE)) {
			if (policySubLine.equals(Constants.PolicySubLines.AUTO_INDIVIDUAL)) {
				values = policy.GetCurrentValues();
				policyCoverage.buildArrays(coverages, values, true, false, null, currentExercise);
				policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList("-")));
			} else {
				policyCoverage.setInsuredValues(new ArrayList<String>(Arrays.asList("Diversos")));
				policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList("-")));
				if (policy.GetCurrentObjects().length== 1) {
					policyCoverage.setCoverages(new ArrayList<String>(Arrays.asList("Diversos")));
				} else {
					policyCoverage.buildArrays(coverages, null, false, false, null, currentExercise);
				}
			}
		} else if (policyCat.equals(Constants.PolicyCategories.WORK_ACCIDENTS)) {
			values = policy.GetCurrentValues();
			policyCoverage.buildArrays(coverages, values, false, false, null, currentExercise);
			policyCoverage.setInsuredValues(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, Constants.PolicyCoverages.WORK_LEGAL, "CPROV"))));
			policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, null, "TCOM"))));
		} else if (policyCat.equals(Constants.PolicyCategories.LIFE)) {
			values = policy.GetCurrentValues();
			policyCoverage.buildArrays(coverages, values, true, false, null, currentExercise);
			policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, null, "TCOM"))));
		} else if (policyCat.equals(Constants.PolicyCategories.RESPONSABILITY)) {
			values = policy.GetCurrentValues();
			policyCoverage.buildArrays(coverages, values, true, false, null, currentExercise);
			policyCoverage.setInsuredValues(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, Constants.PolicyCoverages.RESPONSABILITY_VALUE, "CAP"))));
			policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, null, "TXACE"))));
		} else if (policyCat.equals(Constants.PolicyCategories.PERSONAL_ACCIDENTS)) {
			values = policy.GetCurrentValues();
			policyCoverage.buildArrays(coverages, values, true, false, null, currentExercise);
			policyCoverage.setInsuredValues(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, Constants.PolicyCoverages.RESPONSABILITY_VALUE, "CAP"))));
			policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList("-")));
		} else if (policyCat.equals(Constants.PolicyCategories.DISEASE)) {
			values = policy.GetCurrentValues();
			policyCoverage.buildArrays(coverages, values, true, false, null, currentExercise);
			policyCoverage.setInsuredValues(new ArrayList<String>(Arrays.asList(getValueWithCoverageAndTag(policy, Constants.PolicyCoverages.RESPONSABILITY_VALUE, "CAP"))));
			policyCoverage.setTaxes(new ArrayList<String>(Arrays.asList("-")));
		}
		
		if (objects == null) {
			values = policy.GetCurrentValues();
			policyCoverage.buildArrays(coverages, values, true, true, null, currentExercise);
			result.put(UUID.fromString("0"), policyCoverage);
		}
		
		for (int i=0; i<objects.length; i++) {
			values = policy.GetCurrentKeyedValues(objects[i].getKey(), currentExercise);
			policyCoverage.buildArrays(coverages, values, true, true, objects[i].getKey(), currentExercise);
			result.put(objects[i].getKey(), policyCoverage);
		}
		
		return result;
	}

	private String getValueWithCoverageAndTag(Policy policy, UUID coveragePK,
			String tag) throws BigBangJewelException {
		
		PolicyValue[] values = policy.GetCurrentValues();
		
		for (int i=0; i<values.length; i++) {
			if (values[i].GetTax().GetTag() != null && 
				(values[i].GetTax().GetTag().equals(tag) ||
				 tag == null) &&
				(values[i].GetTax().GetCoverage().getKey().equals(coveragePK) ||
				 coveragePK == null) &&
				 values[i].GetValue() != null) {
				
				return values[i].GetValue();
			}
		}
		
		return null;
	}

	private UUID getCurrentExercise(PolicyExercise[] exercises) {
		Timestamp maxDate = new Timestamp(1);
		UUID result = null; 
		for (int i = 0; i<exercises.length; i++) {
			if (((Timestamp)exercises[i].getAt(PolicyExercise.I.STARTDATE)).after(maxDate)) {
				result = exercises[i].getKey();
			}
		}
		return result;
	}

	/** 
	 * This method is responsible for building the table with the names of all the coverages from a given policy
	 */
	protected TD buildCoverageRelatedTable(ArrayList<String> data) {
		
		TD content;

		content = new TD();
		content.setColSpan(1);
		
		Table table;
		TR[] tableRows  = new TR[data.size()];
		
		//Iterates the coverages, and adds them to a table
		for ( int i = 0; i < data.size(); i++ ) {
			
			TD[] coverage = new TD[1];
			
			coverage[0] = ReportBuilder.buildCell(data.get(i), TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(coverage[0], false, false);
				
			tableRows[i] = ReportBuilder.buildRow(coverage);
			ReportBuilder.styleRow(tableRows[i], false);
		}
		
		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);
		
		content.addElement(table);
		ReportBuilder.styleInnerContainer(content);
		
		return content;
	}
	

	
	/** 
	 * The method responsible for getting columns' titles
	 */
	protected TD[] buildInnerHeaderRow(String[] reportParams) {

		TD[] cells;

		int cellNumber = Collections.frequency(Arrays.asList(reportParams), "1");
		cells = new TD[cellNumber];
		int paramCheck = 2;

		cellNumber = 0;

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Apólice");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Companhia");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Venc. (M/D)");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Pessoas/Bens Seguros");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Coberturas");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Local de Risco");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Capital");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Taxa");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Prémio Total Anual");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber] = ReportBuilder.buildHeaderCell("Observações");
			ReportBuilder.styleCell(cells[cellNumber++], false, true);
		}

		setWidths(cells, reportParams);

		return cells;
	}
	
	/** 
	 * The method which defines the width for the columns
	 */
	protected void setWidths(TD[] cells, String[] reportParams) {
		
		int cellNumber = Collections.frequency(Arrays.asList(reportParams), "1");
		double multiplier = cellNumber / 10;

		cellNumber = 0;
		
		int paramCheck = 2;

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (130 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (250 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (100 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (250 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (250 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (90 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (90 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (90 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (90 * multiplier));
		}
		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth((int) (90 * multiplier));
		}
	}
	
	/** 
	 * The method responsible for getting the data needed by the report
	 */
	protected Policy[] getPortfolio(String[] reportParams)
		throws BigBangJewelException {
		
		StringBuilder strSQL;
		ArrayList <Policy> policies;
		IEntity policyEntity;
		MasterDB ldb;
		ResultSet fetchedPolicies;
		UUID guidAgent;

		try {
			
			// Gets the entity responsible for controlling policies, in the current namespace
			policyEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			// Starts building the SQL used. Initially, only filtering valid policies
			strSQL = new StringBuilder();
			strSQL.append("SELECT * FROM (" +
					policyEntity.SQLForSelectByMembers(new int[] {Policy.I.STATUS},
					new java.lang.Object[] {Constants.StatusID_Valid}, null) + ") [AuxPol] WHERE 1=1");
		
		} catch (Throwable e){
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Filters by client (if defined in the interface)
		if ( reportParams[0] != null ) {
			filterByClient(strSQL, UUID.fromString(reportParams[0]));
		}

		// Filters by client group (if defined in the interface)
		if ( reportParams[1] != null ) {
			filterByClientGroup(strSQL, UUID.fromString(reportParams[1]));
		}

		// If the current user is an "agent", he only sees the corresponding policies
		guidAgent = Utils.getCurrentAgent();
		if ( guidAgent != null ) {
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
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Iterates the policies and adds to the result
		try {
			while (fetchedPolicies.next()) {
				policies.add(Policy.GetInstance(Engine.getCurrentNameSpace(), fetchedPolicies));
			}
		} catch (Throwable e) {
			try { fetchedPolicies.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Cleanup
		try {
			fetchedPolicies.close();
		} catch (Throwable e) {
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return policies.toArray(new Policy[policies.size()]);
	}
	
	private String getRiskSite(Policy policy, PolicyObject object) throws BigBangJewelException {
		
		UUID policyCat = policy.GetSubLine().getLine().getCategory().getKey();
		
		PolicyValue[] vals = policy.GetCurrentValues();
		
		// Risk site differs with different policies' categories
		if (policyCat.equals(Constants.PolicyCategories.MULTIRISK)) {
			String address = (String) (object.getAt(PolicyObject.I.ADDRESS1) == null ? 
						"": 
							object.getAt(PolicyObject.I.ADDRESS1));
			address = (String) (object.getAt(PolicyObject.I.ADDRESS2) == null ? 
					address : 
						address + " " + object.getAt(PolicyObject.I.ADDRESS2));
			address = (String) (object.getAt(PolicyObject.I.ZIPCODE) == null ? 
					address : 
						address + ", " + object.getAt(PolicyObject.I.ZIPCODE));
			address = (String) (object.getAt(PolicyObject.I.CITYNUMBER) == null ? 
					address : 
						address + " - " + object.getAt(PolicyObject.I.CITYNUMBER));

			// TODO: incompleto e eventualmente errado. check ReceiptReturnLetterReport, l.61
			
			return address;
		}
		
		for (int i = 0; i<vals.length; i++) {
			if (vals[i].GetTax().GetTag() != null &&
				vals[i].GetObjectID().equals(object.getKey()) &&
				vals[i].GetValue() != null) {
				
				if (policyCat.equals(Constants.PolicyCategories.RESPONSABILITY)) {
					if( vals[i].GetTax().GetTag().equals("AMBT")) {
						return vals[i].GetValue();
					}
				}	
				if (policyCat.equals(Constants.PolicyCategories.CONSTRUCTION_ASSEMBLY)) {
					if( vals[i].GetTax().GetTag().equals("NEMP")) {
						return vals[i].GetValue();
					}
				}	
				
			}
		}
		
		return "-";
	}
	
	@SuppressWarnings("unused")
	private class CoverageData {
		
		private ArrayList <String> coverages;
		private ArrayList <String> insuredValues;
		private ArrayList <String> taxes;
		
		CoverageData () {
			coverages = new ArrayList<String>();
			insuredValues = new ArrayList<String>();
			taxes = new ArrayList<String>();
		}
		
		CoverageData (boolean storeValues, boolean storeTaxes) {
			coverages = new ArrayList<String>();
			if (storeValues) {
				insuredValues = new ArrayList<String>();
			}
			if (storeTaxes) {
				taxes = new ArrayList<String>();
			}
		}

		public ArrayList<String> getCoverages() {
			return coverages;
		}

		public ArrayList<String> getInsuredValues() {
			return insuredValues;
		}

		public ArrayList<String> getTaxes() {
			return taxes;
		}
		
		public void setCoverages(ArrayList<String> coverages) {
			this.coverages = coverages;
		}

		public void setInsuredValues(ArrayList<String> insuredValues) {
			this.insuredValues = insuredValues;
		}

		public void setTaxes(ArrayList<String> taxes) {
			this.taxes = taxes;
		}

		public void buildArrays(PolicyCoverage[] policyCoverages, PolicyValue[] policyValues,
				boolean insertValue, boolean insertTax, UUID objectId, UUID exerciseID) {
			
			//Iterates the coverages, and adds them to a table
			for ( int i = 0; i < policyCoverages.length; i++ ) {
				
				// Only lists coverages present at the policy
				if (policyCoverages[i].IsPresent()) {
					coverages.add((String) policyCoverages[i].GetCoverage().getAt(Coverage.I.NAME));
					
					boolean wasValInserted = false;
					boolean wasTaxInserted = false;
					
					// Iterates the values and gets the insured values' ones
					for ( int u = 0; u < policyValues.length; u++ ) {
						if (policyValues[u].GetTax().GetTag() != null && 
							policyValues[u].GetTax().GetCoverage().getKey().equals(policyCoverages[i].GetCoverage().getKey()) &&
							(objectId == null || policyValues[u].GetObjectID().equals(objectId)) &&
							(exerciseID == null || policyValues[u].GetExerciseID().equals(exerciseID)) &&
							policyValues[u].GetValue() != null) {
							
							if (policyValues[u].GetTax().GetTag().equals("CAP") && insertValue) {
								insuredValues.add(policyValues[u].GetValue());
								wasValInserted = true;
							}
							
							if (policyValues[u].GetTax().GetTag().equals("TCOM") && insertTax) {
								taxes.add(policyValues[u].GetValue());
								wasTaxInserted = true;
							}
						}
					}
					
					if (!wasValInserted && insertValue) {
						insuredValues.add(" ");
					}
					
					if (!wasTaxInserted && insertTax) {
						taxes.add(" ");
					}
				}
			}
		}
	};
}
