package com.premiumminds.BigBang.Jewel.Data;

import java.io.Serializable;
import java.util.UUID;

public class OutgoingMessageData
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public UUID[] marrUsers;
	public UUID[] marrContactInfos;
	public String[] marrCCs;
	public String[] marrBCCs;

	public String mstrSubject;
	public String mstrBody;

	public UUID[] marrAttachments;
}
