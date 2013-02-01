package com.premiumminds.BigBang.Jewel.Data;

import java.io.Serializable;

import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class IncomingMessageData
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String mstrSubject;
	public String mstrBody;
	public String mstrEmailID;

	public DocOps mobjDocOps;
}
