package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnect.StorageMethods;

public class StorageConnector {

	/**
	 * This method uploads a javax.Message to the google storage
	 */
	public static void uploadMailMessage(Message tempItem, String mailId)
			throws BigBangJewelException {

		File tempEmlFile = null;

		try {
			// "forbidden" characters' cleanup
			String itemId = mailId.replaceAll("/", "_").replaceAll("<", "")
					.replaceAll(">", "");
			tempEmlFile = createTemporaryFile(itemId, ".eml");

			// Temporary File's creation
			FileOutputStream outputStream = new FileOutputStream(tempEmlFile);
			tempItem.writeTo(outputStream);

			// Upload to storage
			StorageMethods.uploadFile(itemId + ".eml", "message/rfc822",
					tempEmlFile, Constants.StorageConstants.BUCKET_NAME);

			outputStream.close();

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if (tempEmlFile != null) {
			tempEmlFile.delete();
		}
	}

	/**
	 * Creates a temporary file
	 */
	private static File createTemporaryFile(String prefix, String suffix) {
		String tempDir = System.getProperty("java.io.tmpdir")
				+ "\\mailMigration";
		String fileName = (prefix != null ? prefix : "")
				+ (suffix != null ? suffix : "");
		return new File(tempDir, fileName);
	}

	/**
	 * Gets a message stored in Google Storage
	 */
	private static File getMessage(String id) throws BigBangJewelException {

		ByteArrayOutputStream storedObject = null;
		File result = null;
		OutputStream outStream = null;
		String convertedId =  id.replaceAll("/", "_").replaceAll("<", "")
				.replaceAll(">", "") + ".eml";

		try {
			storedObject = StorageMethods.downloadObject(
					Constants.StorageConstants.BUCKET_NAME, convertedId);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try {
			result = File.createTempFile("tempFile", ".eml");
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try {
			outStream = new FileOutputStream(result);
			storedObject.writeTo(outStream);
			outStream.close();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * This methods gets a File stored in Google storage, and creates (and
	 * returns) a MessageData object with the relevant information
	 */
	public static MessageData getAsData(String fileId)
			throws BigBangJewelException {

		Message fetchedMessage = null;

		// Gets the file with the given Id and converts it to a MimeMessage
		File downloadedFile = getMessage(fileId);
		try {
			InputStream inStream = new FileInputStream(downloadedFile);
			fetchedMessage = new MimeMessage(null, inStream);
			//downloadedFile.delete();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		return MailConnector.messageToData(fetchedMessage);
	}

}
