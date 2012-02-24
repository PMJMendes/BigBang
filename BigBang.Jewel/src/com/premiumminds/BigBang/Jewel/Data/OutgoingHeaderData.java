package com.premiumminds.BigBang.Jewel.Data;

import java.io.Serializable;
import java.util.UUID;

public class OutgoingHeaderData
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public UUID[] marrUsers;
	public UUID[] marrContactInfos;
	public String[] marrCCs;
	public String[] marrBCCs;

}
