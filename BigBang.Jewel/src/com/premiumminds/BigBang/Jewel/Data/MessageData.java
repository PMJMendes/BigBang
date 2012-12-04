package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class MessageData
	implements DataBridge
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

	public String mstrEmailID;

	public UUID[] marrAttachments;

	public DocOps mobjDocOps;
	public ContactOps mobjContactOps;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrSubject = (String)pobjSource.getAt(Message.I.SUBJECT);
		midOwner =      (UUID)pobjSource.getAt(Message.I.OWNER);
		mlngNumber = (Integer)pobjSource.getAt(Message.I.NUMBER);
		midDirection =  (UUID)pobjSource.getAt(Message.I.DIRECTION);
		mbIsEmail =  (Boolean)pobjSource.getAt(Message.I.ISEMAIL);
		mdtDate =  (Timestamp)pobjSource.getAt(Message.I.DATE);
		mstrBody = ((Message)pobjSource).getText();
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Message.I.SUBJECT,   mstrSubject);
			pobjDest.setAt(Message.I.OWNER,     midOwner);
			pobjDest.setAt(Message.I.NUMBER,    mlngNumber);
			pobjDest.setAt(Message.I.DIRECTION, midDirection);
			pobjDest.setAt(Message.I.ISEMAIL,   mbIsEmail);
			pobjDest.setAt(Message.I.DATE,      mdtDate);
			((Message)pobjDest).setText(mstrBody);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		int i;

		if ( mlngNumber == 0 )
			pstrBuilder.append("Mensagem inicial, ");
		else
			pstrBuilder.append("Mensagem #").append(mlngNumber).append(", ");
		if ( Constants.MsgDir_Incoming.equals(midDirection) )
		{
			if ( mbIsEmail )
				pstrBuilder.append("recebida em ");
			else
				pstrBuilder.append("anotada em ");
		}
		else
		{
			if ( mbIsEmail )
				pstrBuilder.append("enviada em ");
			else
				pstrBuilder.append("transmitida em ");
		}
		pstrBuilder.append(mdtDate.toString().substring(0, 19)).append(":").append(pstrLineBreak);

		pstrBuilder.append(pstrLineBreak).append(mstrSubject).append(pstrLineBreak);

		pstrBuilder.append(pstrLineBreak).append(mstrBody).append(pstrLineBreak);

		if ( mbIsEmail && (marrAddresses != null) && (marrAddresses.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Endereços:").append(pstrLineBreak);
			for ( i = 0; i < marrAddresses.length; i++ )
				marrAddresses[i].Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
