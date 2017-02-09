package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Reports.InsurerAccountingReport;

/**
 *	Class responsible for implementing the needed functionalities relating 
 *	with creating and storing a file in the PHC
 */
public class PHCConnector {
	
	// The URLs for both the local and the PHC's server
	private static String LOCAL_URL = "C:\\facturas\\xml\\";
	private static String PHC_URL = "XPTO"; // TODO: definir quando for criada

	/**
	 * This method extracts the needed information from the company and the
	 * insurerAccountingInfo to generate the XML, generates the file name, and
	 * calls the class responsible for generating the xml
	 */
	public static void createPHCFile(Company company,
			InsurerAccountingReport info, Timestamp today)
			throws BigBangJewelException {

		String filePath;
		String todayString;
		String docNumber = "112533"; // DUMMY

		if (company == null) {
			throw new BigBangJewelException(
					"Error while generating the XML file: no company. ");
		}

		if (info == null) {
			throw new BigBangJewelException(
					"Error while generating the XML file: no info. ");
		}

		if (today != null) {
			filePath = new SimpleDateFormat("yyyyMMdd.HHmmss").format(today);
			todayString = new SimpleDateFormat("yyyy-MM-dd").format(today);
		} else {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss");
			Calendar cal = Calendar.getInstance();
			filePath = dateFormat.format(cal);
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			todayString = dateFormat.format(cal);
		}

		String comp = company.getAt(Company.I.ACRONYM) == null ? "" : company
				.getAt(Company.I.ACRONYM).toString();

		filePath = LOCAL_URL + filePath + comp + ".xml";

		String totalValue = info.mdblTotalComms == null ? ""
				: info.mdblTotalComms.toString();
		String taxValue = info.mdblTax == null ? "" : info.mdblTax.toString();
		String netValue = info.mdblNetComms == null ? "" : info.mdblNetComms
				.toString();

		try {
			XMLCreator creator = new XMLCreator();
			creator.createXML(company, docNumber, todayString, todayString,
					totalValue, taxValue, netValue, filePath);
		} catch (Throwable e) {
			throw new BigBangJewelException(
					"Error while calling the method to create the XML. "
							+ e.getMessage(), e);
		}
	}
}
