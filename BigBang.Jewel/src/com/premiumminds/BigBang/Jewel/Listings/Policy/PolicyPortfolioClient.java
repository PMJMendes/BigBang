package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.lang.reflect.InvocationTargetException;
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
import org.apache.ecs.html.Div;
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

		CoverageData() {
			coverages = new ArrayList<String>();
			insuredValues = new ArrayList<String>();
			riskSite = new ArrayList<String>();
			taxes = new ArrayList<String>();
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

		public void setCoverages(PolicyCoverage[] policyCoverages) {

			if (policyCoverages == null || policyCoverages.length == 0) {
				coverages.add(" ");
			}
			for (int i = 0; i < policyCoverages.length; i++) {
				// Only inserts the coverages present at the policy
				if (policyCoverages[i].IsPresent()) {
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
	}

	// The total premium to display in the report
	private BigDecimal premiumTotal;

	// A "Faux-id" used when there is no insured object associated with a policy
	private UUID fauxId;

	// Width Constants
	private static final int POLICY_WIDTH = 140;
	private static final int COMPANY_WIDTH = 220;
	private static final int DATE_WIDTH = 40;
	private static final int OBJECT_WIDTH = 230;
	private static final int RISK_SITE_WIDTH = 230;
	private static final int COVERAGES_WIDTH = 330;
	private static final int VALUE_WIDTH = 102;
	private static final int TAX_WIDTH = 40;
	private static final int PREMIUM_WIDTH = 120;
	private static final int OBSERVATIONS_WIDTH = 80;
	private static final int STRING_BREAK_POINT = 40;

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
		reportResult[0] = buildClientPortfolioTable(
				"Relatório de Carteira de Clientes", policies, reportParams);

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
	 * The method responsible for printing the table corresponding to a client's
	 * portfolio
	 */
	private Table buildClientPortfolioTable(String headerTitle,
			Policy[] policies, String[] reportParams)
					throws BigBangJewelException {

		Table table;
		TR[] tableRows;
		TD mainContent;
		int rowNum = 0;
		String clientOrGroupTitle;
		HashMap<String, ArrayList<Policy>> policiesMap;

		tableRows = new TR[5];
		premiumTotal = BigDecimal.ZERO;
		fauxId = UUID.randomUUID();

		// Creates a map with the policies separated by "ramo"
		policiesMap = getPoliciesMap(policies);

		// Gets the table row with the report's name
		tableRows[rowNum++] = ReportBuilder
				.constructDualHeaderRowCell(headerTitle);

		// Gets the client's or group's name (if any) and creates the
		// corresponding row
		clientOrGroupTitle = getClientOrGroup(reportParams);
		if (clientOrGroupTitle != null) {
			tableRows[rowNum++] = ReportBuilder
					.constructDualIntermediateRowCell(clientOrGroupTitle);
		}

		// Builds the row corresponding to the "real content" of the report -
		// meaning... the values from the policies
		mainContent = new TD();
		mainContent.setColSpan(2);
		mainContent.addElement(buildReportInfo(policiesMap, policies.length,
				reportParams));
		ReportBuilder.styleInnerContainer(mainContent);
		tableRows[rowNum++] = ReportBuilder.buildRow(new TD[] { mainContent });

		// Builds the row with the total number of policies
		tableRows[rowNum++] = ReportBuilder.constructDualRow("Nº de Apólices",
				policies.length, TypeDefGUIDs.T_Integer, false);

		// Build the row with the total prize
		if (reportParams[10].equals("1")) {
			tableRows[rowNum++] = ReportBuilder.constructDualRow(
					"Total de Prémios", premiumTotal, TypeDefGUIDs.T_Decimal,
					false);
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

		for (int i = 0; i < policies.length; i++) {
			policySubline = policies[i].GetSubLine().getDescription();
			if (policiesMap.get(policySubline) == null) {
				policiesMap.put(policySubline, new ArrayList<Policy>());
			}
			policiesMap.get(policySubline).add(policies[i]);
		}

		return policiesMap;
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
		for (String subline : policiesMap.keySet()) {
			tableRows[rowNum++] = buildSublineRow(subline);
			Object[] policies = policiesMap.get(subline).toArray();

			for (int i = 0; i < policies.length; i++) {
				tableRows[rowNum] = ReportBuilder.buildRow(buildRow(
						(Policy) policies[i], reportParams));
				ReportBuilder.styleRow(tableRows[rowNum++], false);
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
		.addAll(policyParams, Arrays.copyOfRange(reportParams, 2, 5));
		policyParams.addAll(Arrays.asList(Arrays.copyOfRange(reportParams, 10,
				12)));
		String[] objectParams = Arrays.copyOfRange(reportParams, 5, 10);

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
			cells[cellNumber] = ReportBuilder.buildCell("Companhia",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Até",
					TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(cells[cellNumber++], false, leftLine);
			leftLine = true;
		}
		if (innerObjs > 0) {
			cells[cellNumber++] = buildInnerHeaderRow(objectParams);
		}
		if (policyParams.get(paramCheck++).equals("1")) {
			cells[cellNumber] = ReportBuilder.buildCell("Prémio Total Anual",
					TypeDefGUIDs.T_String);
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
		row.setStyle("height:35px; font-weight:bold;");

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
		.addAll(policyParams, Arrays.copyOfRange(reportParams, 2, 5));
		policyParams.addAll(Arrays.asList(Arrays.copyOfRange(reportParams, 10,
				12)));

		String[] objectParams = Arrays.copyOfRange(reportParams, 5, 10);

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
			premiumTotal = premiumTotal.add((BigDecimal) policy
					.getAt(Policy.I.TOTALPREMIUM));
		}

		// Policy Number
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = safeBuildCell(policy.getLabel(),
					TypeDefGUIDs.T_String);
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
						splitValue(objectNameArray), OBJECT_WIDTH);
			} else {
				dataCells[cellNumber] = safeBuildCell(companyName,
						TypeDefGUIDs.T_String);
			}
			ReportBuilder.styleCell(dataCells[cellNumber++], true, leftLine);
			leftLine = true;
		}

		// Maturity
		if (policyParams.get(paramCheck++).equals("1")) {
			dataCells[cellNumber] = safeBuildCell(maturity,
					TypeDefGUIDs.T_String);
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
			dataCells[cellNumber] = safeBuildCell(
					policy.getAt(Policy.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal);
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
			cells[cellNumber] = ReportBuilder.buildCell("Taxa",
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
		if (innerWidth > 0) {
			cells[cellNumber++].setWidth(innerWidth);
		}

		if (reportParams[paramCheck++].equals("1")) {
			cells[cellNumber++].setWidth(PREMIUM_WIDTH);
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
	private TD safeBuildCell(java.lang.Object pobjValue, UUID pidType) {
		if (pobjValue == null) {
			return ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		}
		return ReportBuilder.buildCell(pobjValue, pidType);
	}

	/**
	 * This method is used to split a string, which could be "too wide", and
	 * wreck the report's look. It gets not a String, but an array list with one
	 * element (the string) for it is the way it is represented in the
	 * CoverageData's class.
	 */
	private ArrayList<String> splitValue(ArrayList<String> riskSite) {

		ArrayList<String> result = new ArrayList<String>();
		String[] split = riskSite.get(0).split("\\s+");
		String tmp = "";

		// Splits when it occupies more than (approximately) one row's length
		for (int i = 0; i < split.length; i++) {
			tmp = tmp + " " + split[i];
			if ((tmp.length() / (STRING_BREAK_POINT - 5)) >= 1) {
				result.add(tmp);
				tmp = "";
			} else if (i + 1 == split.length) {
				result.add(tmp);
			}
		}

		return (result.size() == 0 ? riskSite : result);
	}

	/**
	 * This method returns a TD containing a value or a table of values,
	 * contained in an ArrayList. It's used to build tables to display inside a
	 * TD.
	 */
	private TD buildValuesTable(ArrayList<String> data, int width) {

		TD content;

		content = new TD();
		content.setColSpan(1);

		if (data == null) {
			return ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		}

		// Builds a "simple" TD or a table with a TD with each value
		if (data.size() == 1) {
			content = safeBuildCell(data.get(0), TypeDefGUIDs.T_String);
			content.setWidth(width);
			ReportBuilder.styleCell(content, false, false);
			ReportBuilder.styleInnerContainer(content);
		} else {

			Table table;
			TR[] tableRows = new TR[data.size()];

			// Iterates the values, and adds them to the table
			for (int i = 0; i < data.size(); i++) {
				TD[] cell = new TD[1];

				cell[0] = safeBuildCell(data.get(i), TypeDefGUIDs.T_String);
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
								splitValue(objectNameArray), OBJECT_WIDTH);
					} else {
						dataCells[currentCell] = safeBuildCell(objectName,
								TypeDefGUIDs.T_String);
					}
					dataCells[currentCell].setWidth(OBJECT_WIDTH);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Risk Site
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(
							splitValue(valuesByObject.get(objectKey)
									.getRiskSite()), RISK_SITE_WIDTH);
					dataCells[currentCell].setWidth(RISK_SITE_WIDTH);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Policy Coverages
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getCoverages(), COVERAGES_WIDTH);
					dataCells[currentCell].setWidth(COVERAGES_WIDTH);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Insured Value
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getInsuredValues(), VALUE_WIDTH);
					dataCells[currentCell].setWidth(VALUE_WIDTH);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				// Tax
				if (reportParams[paramCheck++].equals("1")) {
					dataCells[currentCell] = buildValuesTable(valuesByObject
							.get(objectKey).getTaxes(), TAX_WIDTH);
					dataCells[currentCell].setWidth(TAX_WIDTH);
					ReportBuilder.styleCell(dataCells[currentCell++], topLine,
							true);
				}

				setInnerWidths(dataCells, reportParams);
				tableRows[nrRows] = ReportBuilder.buildRow(dataCells);
				tableRows[nrRows++].setStyle("height:70px;");
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
				&& policySubLine.equals(Constants.PolicySubLines.AUTO_FLEET)
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
				.equals(Constants.PolicySubLines.AUTO_INDIVIDUAL)) {
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
				&& policySubLine.equals(Constants.PolicySubLines.AUTO_FLEET)
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
			if (policyCoverages[i].IsPresent()) {
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
				if (policyCoverages[i].IsPresent()) {

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
}
