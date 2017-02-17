package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Reports.InsurerAccountingReport;

/**
 * Class responsible for implementing the needed functionalities relating with
 * creating and storing a file in the PHC
 */
public class PHCConnector {

	// The URLs for both the local and the PHC's server
	private static String LOCAL_URL = "C:\\facturas\\xml\\";
	private static String PHC_URL = "\\\\WS-PHC\\bigbang\\";

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
		String fileName;

		if (company == null) {
			throw new BigBangJewelException(
					"Error while generating the XML file: no company. ");
		}

		if (info == null) {
			throw new BigBangJewelException(
					"Error while generating the XML file: no info. ");
		}

		if (today != null) {
			fileName = new SimpleDateFormat("yyyyMMdd.HHmmss").format(today);
			todayString = new SimpleDateFormat("yyyy-MM-dd").format(today);
		} else {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss");
			Calendar cal = Calendar.getInstance();
			fileName = dateFormat.format(cal);
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			todayString = dateFormat.format(cal);
		}

		String comp = company.getAt(Company.I.ACRONYM) == null ? "" : company
				.getAt(Company.I.ACRONYM).toString();
		comp = comp.replaceAll("\\s+", "");

		fileName = fileName + comp + ".xml";
		filePath = LOCAL_URL + fileName;

		String totalValue = info.mdblTotalComms == null ? ""
				: info.mdblTotalComms.toString();
		String taxValue = info.mdblTax == null ? "" : info.mdblTax.toString();
		String netValue = info.mdblNetComms == null ? "" : info.mdblNetComms
				.toString();

		// Creates the file on the local folder (at least for now, in the future
		// maybe it will create it in the network folder)
		try {
			XMLCreator creator = new XMLCreator();
			creator.createXML(company, todayString, totalValue, taxValue,
					netValue, filePath);
		} catch (Throwable e) {
			throw new BigBangJewelException(
					"Error while calling the method to create the XML. "
							+ e.getMessage(), e);
		}

		// Now that it finalized the creation and storage of the XML file, saves
		// a copy @ google storage
		File file = new File(filePath); // The file used to check if a file
										// exists
		if (file.exists() && !file.isDirectory()) {
			StorageConnector.uploadFile(file, fileName,
					Constants.GoogleAppsConstants.INVOICE_FILE_EXTENSION,
					Constants.StorageConstants.INVOICE_BUCKET_NAME);
		}

		// Now, it creates a copy in the folder where it will be read by PHC
		try {
			if (file.exists() && !file.isDirectory()) {
				File destinationFile = new File(PHC_URL + fileName);
				FileUtils.copyFile(file, destinationFile);
			}
		} catch (Throwable e) {
			// In case an error occurred, it will create a log, and continue execution.
			// at least for now...
			BufferedWriter writer = null;
			try {
				// create a temporary file
				String timeLog = LOCAL_URL
						+ "logs"
						+ "\\"
						+ new SimpleDateFormat("yyyyMMdd_HHmmss")
								.format(Calendar.getInstance().getTime());
				File logFile = new File(timeLog);

				writer = new BufferedWriter(new FileWriter(logFile));
				writer.write("Company: " + company.getLabel() + "\n");
				writer.write("Today: " + todayString + "\n");
				writer.write("Total Value: " + totalValue + "\n");
				writer.write("Tax Value: " + taxValue + "\n");
				writer.write("Net Value: " + netValue + "\n");
				writer.write("File Path: " + filePath + "\n\n");
				writer.write("Error" + "\n");
				writer.write(e.getMessage() + "\n");
				for (StackTraceElement s : e.getStackTrace()) {
					writer.write(s.toString() + "\n");
				}
			} catch (Exception x) {
				// do nothing
			} finally {
				try {
					// Close the writer regardless of what happens...
					writer.close();
				} catch (Exception x) {
					// do nothing
				}
			}
		}

		// AND now with samba... because why not?
		// http://blog.icodejava.com/875/java-tutorial-using-jcifs-to-copy-files-to-shared-network-drive-using-username-and-password/
		/*
		 * try { if (file.exists() && !file.isDirectory()) { String USER_NAME =
		 * "username here"; String PASSWORD = "pass here"; //e.g. Assuming your
		 * network folder is: \my.myserver.netsharedpublicphotos String
		 * NETWORK_FOLDER = "smb://192.168.0.55/bigbang/"; // Or so says António
		 * "smb://ws-phc/bigbang/"
		 * 
		 * String user = USER_NAME + ":" + PASSWORD;
		 * 
		 * NtlmPasswordAuthentication auth = new
		 * NtlmPasswordAuthentication(user); String path = NETWORK_FOLDER +
		 * fileName;
		 * 
		 * SmbFile sFile = new SmbFile(path, auth); SmbFileOutputStream sfos =
		 * new SmbFileOutputStream(sFile); InputStream in = new
		 * FileInputStream(file); sfos.write(IOUtils.toByteArray(in));
		 * sfos.close(); } } catch (Throwable e) { // in this case, while on
		 * "test phase", we'll not stop execution, // only log the error //
		 * http:
		 * //stackoverflow.com/questions/15754523/how-to-write-text-file-java
		 * BufferedWriter writer = null; try { // create a temporary file String
		 * timeLog = LOCAL_URL + "logs" + "\\" + "4_" + new
		 * SimpleDateFormat("yyyyMMdd_HHmmss")
		 * .format(Calendar.getInstance().getTime()); File logFile = new
		 * File(timeLog);
		 * 
		 * writer = new BufferedWriter(new FileWriter(logFile));
		 * writer.write("Company: " + company.getLabel() + "\n");
		 * writer.write("Today: " + todayString + "\n");
		 * writer.write("Total Value: " + totalValue + "\n");
		 * writer.write("Tax Value: " + taxValue + "\n");
		 * writer.write("Net Value: " + netValue + "\n");
		 * writer.write("File Path: " + filePath + "\n\n"); writer.write("Error"
		 * + "\n"); writer.write(e.getMessage() + "\n"); for (StackTraceElement
		 * s : e.getStackTrace()) { writer.write(s.toString() + "\n"); } } catch
		 * (Exception x) { // do nothing } finally { try { // Close the writer
		 * regardless of what happens... writer.close(); } catch (Exception x) {
		 * // do nothing } } }
		 */
	}
}
