package com.premiumminds.BigBang.Jewel.SysObjects.StorageConnect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Security.OAuthHandler;

public class StorageUtils {

	public static String getOauthToken() throws BigBangJewelException {

		GoogleCredential credential = getCredential();
		String accessToken = null;

		try {
			credential.refreshToken();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		accessToken = credential.getAccessToken();

		return accessToken;
	}

	public static GoogleCredential getCredential() {

		InputStream inputStream = OAuthHandler.class
				.getResourceAsStream("/resources/bigbang-google-apps-1fcf817841a6.p12");

		final String PREFIX = "stream2file";
		final String SUFFIX = ".tmp";

		File tempFile = null;
		try {
			tempFile = File.createTempFile(PREFIX, SUFFIX);
			tempFile.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(tempFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		int read = 0;
		byte[] bytes = new byte[1024];

		try {
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JsonFactory fac = new JacksonFactory();
		GoogleCredential credential = null;

		try {
			HttpTransport transport = GoogleNetHttpTransport
					.newTrustedTransport();

			credential = new GoogleCredential.Builder()
					.setTransport(transport)
					.setJsonFactory(fac)
					.setServiceAccountId(Constants.StorageConstants.SERVICE_ACCOUNT_ID)
					.setServiceAccountScopes(
							Collections.singleton(Constants.StorageConstants.STORAGE_SCOPE))
					.setServiceAccountPrivateKeyFromP12File(tempFile).build();

			credential.refreshToken();

		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return credential;
	}

}
