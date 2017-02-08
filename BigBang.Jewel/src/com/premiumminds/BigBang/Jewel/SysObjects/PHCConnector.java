package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.Timestamp;

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

	public static void createPHCFile(Company company, InsurerAccountingReport info, Timestamp today) {
		System.out.println(company.getLabel());
	}
}
