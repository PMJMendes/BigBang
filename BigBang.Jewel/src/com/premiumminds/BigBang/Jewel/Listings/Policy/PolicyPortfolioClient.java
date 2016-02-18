package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

/**
 * Class responsible for the creation of the Client's Portfolio Report
 */
public class PolicyPortfolioClient extends PolicyListingsBase {
	
	BigDecimal premiumTotal;
	
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
		mainContent.addElement(buildReportInfo(policiesMap, policies.length));
		ReportBuilder.styleInnerContainer(mainContent);
		tableRows[rowNum++] = ReportBuilder.buildRow(new TD[] {mainContent});

		// Builds the row with the total number of policies
		tableRows[rowNum++] = ReportBuilder.constructDualRow("Nº de Apólices", policies.length, TypeDefGUIDs.T_Integer, false);

		// Build the row with the total prize
		tableRows[rowNum++] = ReportBuilder.constructDualRow("Total de Prémios", premiumTotal, TypeDefGUIDs.T_Decimal, false);

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
									int numberOfPolicies) throws BigBangJewelException {
		
		Table table;
		TR[] tableRows  = new TR[numberOfPolicies + 1 + policiesMap.size()];
		int rowNum= 0;
		
		// Builds the row with the column names
		tableRows[rowNum++] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(tableRows[0], true);
		
		// Iterates the hashmap with the several policies by "ramo", and creates a line 
		// with the name of that "ramo", followed by the information about the policies
		for (String subline: policiesMap.keySet()) {
			tableRows[rowNum++] = buildSublineRow(subline);
			Object[] policies = policiesMap.get(subline).toArray();
			
			for (int i = 0; i < policies.length; i++) {
				tableRows[rowNum] = ReportBuilder.buildRow(buildRow((Policy)policies[i]));
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
	private TR buildSublineRow(String subline) {
		
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
	protected TD[] buildRow(Policy policy) throws BigBangJewelException {
		
		Company company;
		String maturity;
		PolicyObject[] policyObjects;
		PolicyCoverage[] policyCoverages;
		TD[] dataCells = new TD[11];
		
		company = policy.GetCompany();
		maturity = ((policy.getAt(Policy.I.MATURITYMONTH) == null) || 
					((Integer) policy.getAt(Policy.I.MATURITYMONTH) == -1) ||
					(policy.getAt(Policy.I.MATURITYDAY) == null) ||
					((Integer) policy.getAt(Policy.I.MATURITYDAY) == -1)) ?
							null :
							policy.getAt(Policy.I.MATURITYMONTH).toString() + " / " + 
							policy.getAt(Policy.I.MATURITYDAY).toString();
		policyObjects = policy.GetCurrentObjects();
		policyCoverages = policy.GetCurrentCoverages();
		
		// Increases the value for the total premium
		if (policy.getAt(Policy.I.PREMIUM) != null) {
			premiumTotal = premiumTotal.add((BigDecimal)policy.getAt(Policy.I.PREMIUM));
		}
		
		// Policy Number
		dataCells[0] = ReportBuilder.buildCell(policy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(dataCells[0], true, true);
		
		// Insurance Company
		dataCells[1] = ReportBuilder.buildCell(company.getAt(Company.I.NAME), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(dataCells[1], true, true);
		
		// Maturity
		if (maturity == null) {
			dataCells[2] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		} else {
			dataCells[2] = ReportBuilder.buildCell(maturity, TypeDefGUIDs.T_String);
		}
		ReportBuilder.styleCell(dataCells[2], true, true);
		
		// Policy Objects
		if (policyObjects.length == 0) {
			dataCells[3] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		} else {
			dataCells[3] = buildObjectsTable(policyObjects);
		}
		ReportBuilder.styleCell(dataCells[3], true, true);
		
		// Policy Coverages
		if (policyCoverages.length == 0) {
			dataCells[4] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		} else {
			dataCells[4] = buildCoveragesTable(policyCoverages);
		}	
		ReportBuilder.styleCell(dataCells[4], true, true);
		
		// "Local de Risco" - intentionally left blank
		dataCells[5] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(dataCells[5], true, true);
		
		// Insured Value - intentionally left blank
		dataCells[6] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(dataCells[6], true, true);
		
		// Sales' Premium
		if (policy.getAt(Policy.I.PREMIUM) == null) {
			dataCells[7] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		} else {
			dataCells[7] = ReportBuilder.buildCell(policy.getAt(Policy.I.PREMIUM), TypeDefGUIDs.T_Decimal);
		}
		ReportBuilder.styleCell(dataCells[7], true, true);
		
		// Total Premium
		if (policy.getAt(Policy.I.TOTALPREMIUM) == null) {
			dataCells[8] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		} else {
			dataCells[8] = ReportBuilder.buildCell(policy.getAt(Policy.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal);
		}
		ReportBuilder.styleCell(dataCells[8], true, true);
		
		// Notes - intentionally left blank
		dataCells[9] = ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(dataCells[9], true, true);
		
		setWidths(dataCells);

		return dataCells;
	}
	
	/** 
	 * This method is responsible for building the table with the names of all the insured objects from a given policy
	 */
	protected TD buildObjectsTable(PolicyObject[] objects) {
	
		TD content;

		content = new TD();
		content.setColSpan(1);
		
		Table table;
		TR[] tableRows  = new TR[objects.length];
		
		//Iterates the objects, and adds them to a table
		for ( int i = 0; i < objects.length; i++ ) {
			TD[] object = new TD[1];
			
			object[0] = ReportBuilder.buildCell(objects[i].getAt(PolicyObject.I.NAME), TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(object[0], false, false);
			
			tableRows[i] = ReportBuilder.buildRow(object);
			ReportBuilder.styleRow(tableRows[i], false);
		}
		
		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, true);
		
		content.addElement(table);
		ReportBuilder.styleInnerContainer(content);
		
		return content;
	}
	
	/** 
	 * This method is responsible for building the table with the names of all the coverages from a given policy
	 */
	protected TD buildCoveragesTable(PolicyCoverage[] coverages) {
		
		TD content;

		content = new TD();
		content.setColSpan(1);
		
		Table table;
		TR[] tableRows  = new TR[coverages.length];
		
		//Iterates the coverages, and adds them to a table
		for ( int i = 0; i < coverages.length; i++ ) {
			TD[] coverage = new TD[1];
			
			coverage[0] = ReportBuilder.buildCell(coverages[i].GetCoverage().getAt(Coverage.I.NAME), TypeDefGUIDs.T_String);
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
	protected TD[] buildInnerHeaderRow() {
		
		TD[] cells;

		cells = new TD[10];

		cells[0] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(cells[0], false, true);

		cells[1] = ReportBuilder.buildHeaderCell("Comp");
		ReportBuilder.styleCell(cells[1], false, true);

		cells[2] = ReportBuilder.buildHeaderCell("Venc. (M/D)");
		ReportBuilder.styleCell(cells[2], false, true);

		cells[3] = ReportBuilder.buildHeaderCell("Pessoas/Bens Seguros");
		ReportBuilder.styleCell(cells[3], false, true);

		cells[4] = ReportBuilder.buildHeaderCell("Coberturas");
		ReportBuilder.styleCell(cells[4], false, true);

		cells[5] = ReportBuilder.buildHeaderCell("Local de Risco");
		ReportBuilder.styleCell(cells[5], false, true);
		
		cells[6] = ReportBuilder.buildHeaderCell("Capital");
		ReportBuilder.styleCell(cells[6], false, true);

		cells[7] = ReportBuilder.buildHeaderCell("Prémio Comercial");
		ReportBuilder.styleCell(cells[7], false, true);

		cells[8] = ReportBuilder.buildHeaderCell("Prémio Total Anual");
		ReportBuilder.styleCell(cells[8], false, true);

		cells[9] = ReportBuilder.buildHeaderCell("Observações");
		ReportBuilder.styleCell(cells[9], false, true);

		setWidths(cells);

		return cells;
	}
	
	/** 
	 * The method which defines the width for the columns
	 */
	protected void setWidths(TD[] cells) {
		cells[0].setWidth(130);
		cells[1].setWidth(250);
		cells[2].setWidth(100);
		cells[3].setWidth(250);
		cells[4].setWidth(250);
		cells[5].setWidth(90);
		cells[6].setWidth(90);
		cells[7].setWidth(90);
		cells[8].setWidth(90);
		cells[9].setWidth(90);
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
}
