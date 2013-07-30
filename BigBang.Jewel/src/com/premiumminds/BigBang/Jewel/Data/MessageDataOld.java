package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class MessageDataOld
	extends MessageData
{
	private static final long serialVersionUID = 1L;

	public UUID mid;
	public String mstrSubject;
	public UUID midOwner;
	public int mlngNumber;
	public UUID midDirection;
	public boolean mbIsEmail;
	public Timestamp mdtDate;
	public String mstrBody;
	public MessageAddressData[] marrAddresses;
	public UUID[] marrAttachments;
	public String mstrEmailID;
	public DocOps mobjDocOps;
	public ContactOps mobjContactOps;

	public void FromObject(ObjectBase pobjSource) {
	}

	public void ToObject(ObjectBase pobjDest) throws BigBangJewelException {
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak) {
	}
}
