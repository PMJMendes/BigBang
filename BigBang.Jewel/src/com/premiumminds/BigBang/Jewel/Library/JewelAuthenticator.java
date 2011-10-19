package com.premiumminds.BigBang.Jewel.Library;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class JewelAuthenticator
	extends Authenticator
{
	private String mstrUser;
	private String mstrPwd;

	public JewelAuthenticator(String pstrUser, String pstrPwd)
	{
		mstrUser = pstrUser;
		mstrPwd = pstrPwd;
	}
	
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(mstrUser, mstrPwd);
	}
}
