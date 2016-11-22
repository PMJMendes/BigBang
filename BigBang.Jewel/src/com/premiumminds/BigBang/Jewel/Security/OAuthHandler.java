package com.premiumminds.BigBang.Jewel.Security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Collections;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;
import com.sun.mail.smtp.SMTPTransport;


/**
 * This class returns the Stores used for IMAP and SMTP calls, already connected
 * using an Oauth2 token, used with a service account associated with BigBang.
 * 
 * Before using this class, you must call initialize to install the OAuth2 SASL
 * provider.
 * 
 * (heavily) inspired by
 * https://github.com/google/gmail-oauth2-tools/blob/master/java/com/google/code/samples/oauth2/OAuth2Authenticator.java
 */
public class OAuthHandler {

	private static int IMAPS_PORT = 993;

	public static final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = 1L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0,
					"Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2",
					"com.premiumminds.BigBang.Jewel.Security.OAuth2SaslClientFactory");
		}
	}

	
	/**
	 * Installs the OAuth2 SASL provider. This must be called exactly once
	 * before calling other methods on this class.
	 */
	public static void initialize() {
		Security.addProvider(new OAuth2Provider());
	}

	
	/**
	 * Giving a user email (the user's google id), this method returns a
	 * connected store using IMAP
	 */
	public static Store getImapStore(String userEmail) throws Exception {
				
		String oauthToken = (String) getOauthToken();
		
		Properties props = new Properties();
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put("mail.imaps.auth.login.disable", "true");
		props.put("mail.imaps.auth.plain.disable", "true");
		props.put("mail.imaps.socketFactory", IMAPS_PORT);
		props.put("mail.imaps.port", IMAPS_PORT);
		props.put("mail.imaps.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");		
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);

		Session session = Session.getInstance(props, null);
		session.setDebug(false); // TODO: Para alterar para ter debug na consola, em desenvolvimento

		Security.addProvider(new OAuth2Provider());
		
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", IMAPS_PORT, userEmail, oauthToken);

		return store;
	}

	/**
	 * Giving a user email (the user's google id), this method returns a
	 * connected store using IMAP
	 * 
	 * TODO: Not working yet, needs to be changed upon implementing message
	 * sending
	 */
	public static Session getSmtpSession(boolean debug) throws Exception {
		
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, getOauthToken());
		Session session = Session.getInstance(props);
		session.setDebug(debug);
		
		return session;
	}
	
	 public static SMTPTransport getSmtpConnection(String host, int port, String userEmail, Session session) throws Exception {
		
		 final URLName unusedUrlName = null;
		 SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
		 // If the password is non-null, SMTP tries to do AUTH LOGIN.
		 final String emptyPassword = "";
		 transport.connect(host, port, userEmail, emptyPassword);

		 return transport;
	 }
	

	/**
	 * This method gets an OAUTH token used to access a user mail account, with
	 * a service account
	 */
	private static String getOauthToken() throws BigBangJewelException {

		final String PREFIX = "stream2file";
		final String SUFFIX = ".tmp";
		OutputStream outputStream = null;
		int read = 0;
		byte[] bytes = new byte[1024];		
		GoogleCredential credential;
		String accessToken = null;
		
		InputStream inputStream = getInputStreamForP12File();
		
		try {			
			File tempFile = null;
			tempFile = File.createTempFile(PREFIX, SUFFIX);
			outputStream = new FileOutputStream(tempFile);
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			credential = new GoogleCredential.Builder()
					.setJsonFactory(new JacksonFactory())
					.setTransport(new NetHttpTransport())
					.setServiceAccountId(Constants.GoogleAppsConstants.ACCOUNT_ID)
					.setServiceAccountScopes(
							Collections.singleton("https://mail.google.com/"))
					.setServiceAccountPrivateKeyFromP12File(tempFile)
					.setServiceAccountUser(MailConnector.getUserEmail()).build();
			
			credential.refreshToken();
			accessToken = credential.getAccessToken();
			outputStream.close();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return accessToken;
	}


	public static InputStream getInputStreamForP12File() {
		InputStream inputStream = Thread
				.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(Constants.GoogleAppsConstants.P12_FILE_URL);
		return inputStream;
	}

}
