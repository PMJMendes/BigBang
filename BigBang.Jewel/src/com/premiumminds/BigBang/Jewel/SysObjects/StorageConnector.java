package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.poi.util.IOUtils;

import Jewel.Engine.SysObjects.FileXfer;

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
			tempEmlFile = createTemporaryFile(itemId, Constants.GoogleAppsConstants.TMP_FILE_EXTENSION);

			// Temporary File's creation
			FileOutputStream outputStream = new FileOutputStream(tempEmlFile);
			tempItem.writeTo(outputStream);

			// Upload to storage
			StorageMethods.uploadFile(itemId + Constants.GoogleAppsConstants.TMP_FILE_EXTENSION, Constants.GoogleAppsConstants.TMP_FILE_CONTENT_TYPE,
					tempEmlFile, Constants.StorageConstants.BUCKET_NAME);

			outputStream.close();

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " 54 ", e);
		}

		if (tempEmlFile != null) {
			tempEmlFile.delete();
		}
	}

	/**
	 * Creates a temporary file
	 */
	private static File createTemporaryFile(String prefix, String suffix) {
		String tempDir = System.getProperty("java.io.tmpdir");
				//+ Constants.GoogleAppsConstants.TMP_FOLDER;
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
				.replaceAll(">", "") + Constants.GoogleAppsConstants.TMP_FILE_EXTENSION;

		try {
			storedObject = StorageMethods.downloadObject(
					Constants.StorageConstants.BUCKET_NAME, convertedId);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try {
			result = File.createTempFile(Constants.GoogleAppsConstants.TMP_FILE_PREFIX, Constants.GoogleAppsConstants.TMP_FILE_EXTENSION);
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
		
		return MailConnector.messageToData(fetchedMessage, fileId);
	}
	
	/**
	 * This methods gets a File stored in Google storage, and creates (and
	 * returns) a MimeMessage
	 */
	private static MimeMessage getMimeMessage(String fileId)
			throws BigBangJewelException {

		Message fetchedMessage = null;

		// Gets the file with the given Id and converts it to a MimeMessage
		File downloadedFile = getMessage(fileId);
		try {
			
			InputStream inStream = new FileInputStream(downloadedFile);
			fetchedMessage = new MimeMessage(null, inStream);
			return (MimeMessage) fetchedMessage;
			
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	/**
	 * This methods gets a File stored in Google storage, and creates (and
	 * returns) an array of FileXfer objects, representing the mails'
	 * attachments
	 */
	public static FileXfer[] getAttachmentsAsFileXfer(String fileId)
			throws BigBangJewelException {

		Map<String, BodyPart> attachmentsMap = getAttsMapFromStorage(fileId);
			
		if (attachmentsMap == null) {
			return null;
		}

		// Removes the mail's text from the attachments
		attachmentsMap.remove("main");
		
		// Creates the array to return
		FileXfer[] result = new FileXfer[attachmentsMap.size()];
		int u=0;
		for (Map.Entry<String, BodyPart> entry : attachmentsMap.entrySet()) {
			try {
				byte[] binaryData = IOUtils.toByteArray(entry.getValue().getInputStream());
				String contentType = entry.getValue().getContentType().split(";")[0];
				FileXfer tmp = new FileXfer(binaryData.length, contentType, entry.getKey(), new ByteArrayInputStream(binaryData));
				result[u++] = tmp;
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}	
		}
		
		return result;
	}

	protected static Map<String, BodyPart> getAttsMapFromStorage(String fileId)
			throws BigBangJewelException {
		// Gets the storage file
		MimeMessage storageMessage = getMimeMessage(fileId);

		// Gets the message's attachments
		Object content = null;
		try {
			content = storageMessage.getContent();
		}  catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
			
		if (content == null || !(content instanceof Multipart)) {
			return null;
		}

		Map<String, BodyPart> attachmentsMap = MailConnector.getAttachmentsMap(storageMessage);
		return attachmentsMap;
	}
	
	/**
	 * This methods gets a File stored in Google storage, and creates (and
	 * returns) an array of FileXfer objects, representing the mails'
	 * attachments
	 */
	public static FileXfer getAttachmentAsFileXfer(String fileId, String attachmentId)
			throws BigBangJewelException {

		Map<String, BodyPart> attachmentsMap = getAttsMapFromStorage(fileId);
		
		if (attachmentsMap == null) {
			return null;
		}

		// Removes the mail's text from the attachments
		attachmentsMap.remove("main");
		
		// Creates the array to return
		FileXfer result = null;
		
		try {
			for (Map.Entry<String, BodyPart> entry : attachmentsMap.entrySet()) {
				if (MimeUtility.decodeText(entry.getKey()).equals(MimeUtility.decodeText(attachmentId))) { 
					try {
						byte[] binaryData = IOUtils.toByteArray(entry.getValue().getInputStream());
						String contentType = entry.getValue().getContentType().split(";")[0];
						result = new FileXfer(binaryData.length, contentType, entry.getKey(), new ByteArrayInputStream(binaryData));
					} catch (Throwable e) {
						throw new BigBangJewelException(e.getMessage(), e);
					}
					break;
				}
			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		} 
		
		return result;
	}

}
