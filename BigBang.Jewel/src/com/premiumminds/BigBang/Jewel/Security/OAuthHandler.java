package com.premiumminds.BigBang.Jewel.Security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.smtp.SMTPTransport;


/**
 * Performs OAuth2 authentication.
 * 
 * <p>
 * Before using this class, you must call {@code initialize} to install the
 * OAuth2 SASL provider.
 */
public class OAuthHandler {

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
	 * Connects and authenticates to an IMAP server with OAuth2. You must have
	 * called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the imap server, for example
	 *            {@code imap.googlemail.com}.
	 * @param port
	 *            Port of the imap server, for example 993.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example
	 *            {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the IMAP connection.
	 * 
	 * @return An authenticated IMAPStore that can be used for IMAP operations.
	 */
	public static Store getImapStore(String userEmail)
			throws Exception {
		
		/*
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, getOauthToken());
		
		Session session = Session.getInstance(props);
		session.setDebug(true);

		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		final String emptyPassword = "";
		store.connect("imap.gmail.com", 993, userEmail, emptyPassword);
		return store;
		*/
		
		Properties props = new Properties();
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put("mail.imaps.auth.login.disable", "true");
		props.put("mail.imaps.auth.plain.disable", "true");
		props.put("mail.imaps.socketFactory", 993);
		props.put("mail.imaps.port", 993);
		props.put("mail.imaps.socketFactory.class" , "javax.net.ssl.SSLSocketFactory");
		String oauthToken = (String) getOauthToken();
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);		
		
		Session session = Session.getInstance(props, null);
		session.setDebug(true);
		
		Security.addProvider(new OAuth2Provider());
		Store store  = session.getStore("imaps");
		store.connect("imap.gmail.com", 993, userEmail, oauthToken);
		
		return store;
	}

	/**
	 * Connects and authenticates to an SMTP server with OAuth2. You must have
	 * called {@code initialize}.
	 * 
	 * @param host
	 *            Hostname of the smtp server, for example
	 *            {@code smtp.googlemail.com}.
	 * @param port
	 *            Port of the smtp server, for example 587.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example
	 *            {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the connection.
	 * 
	 * @return An authenticated SMTPTransport that can be used for SMTP
	 *         operations.
	 */
	public static SMTPTransport getSmtpStore(String host, int port,
			String userEmail, boolean debug)
			throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, getOauthToken());
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		SMTPTransport transport = new SMTPTransport(session, unusedUrlName);
		// If the password is non-null, SMTP tries to do AUTH LOGIN.
		final String emptyPassword = "";
		transport.connect(host, port, userEmail, emptyPassword);

		return transport;
	}

	private static String getOauthToken() throws BigBangJewelException {

		File loc = new File(OAuthHandler.class.getClassLoader().getResource("/resources/bigbang-google-apps-1fcf817841a6.p12").getFile());
		
		InputStream inputStream = OAuthHandler.class.getResourceAsStream("/resources/bigbang-google-apps-1fcf817841a6.p12");
		final String PREFIX = "stream2file";
	    final String SUFFIX = ".tmp";
	    File tempFile = null;
		try {
			tempFile = File.createTempFile(PREFIX, SUFFIX);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(tempFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    int read = 0;
		byte[] bytes = new byte[1024];

		try {
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JsonFactory fac = new JacksonFactory(); 
		GoogleCredential credential;
		
		try {
			credential = new GoogleCredential.Builder()
			.setTransport(new NetHttpTransport())
		    .setJsonFactory(fac)
		    .setServiceAccountId("bigbang-access-account@tidy-campaign-139313.iam.gserviceaccount.com")
		    .setServiceAccountScopes(Collections.singleton("https://mail.google.com/"))
		    .setServiceAccountPrivateKeyFromP12File(tempFile)
		    .setServiceAccountUser("joaocamilo@credite-egs.pt")
		    .build();
		    
			credential.refreshToken();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	    
		String accessToken = credential.getAccessToken();
		
		return accessToken;
	}
	
	
}
