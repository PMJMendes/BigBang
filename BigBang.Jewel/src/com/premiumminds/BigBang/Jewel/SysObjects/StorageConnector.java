package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.File;
import java.io.FileOutputStream;

import javax.mail.Message;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnect.StorageMethods;

public class StorageConnector {

	/**
	 * This method uploads a javax.Message to the google storage
	 * 
	 * @throws BigBangJewelException
	 */
	public static void uploadMailMessage(Message tempItem, String mailId)
			throws BigBangJewelException {

		File tempEmlFile = null;

		try {
			// "forbidden" characters' cleanup
			String itemId = mailId.replaceAll("/", "_").replaceAll("<", "").replaceAll(">", "");
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

		// Limpa o file que se enviou para o storage
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

}
