package com.premiumminds.BigBang.Jewel.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
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
	public MessageAttachmentData[] marrAttachments;

	public String mstrEmailID;

	public DocOps mobjDocOps;
	public ContactOps mobjContactOps;

	public static class MDInputStream
		extends ObjectInputStream
	{
		public MDInputStream(InputStream in)
			throws IOException
		{
			super(in);
		}

	    protected java.io.ObjectStreamClass readClassDescriptor()
	    	throws IOException ,ClassNotFoundException
	    {
	    	ObjectStreamClass desc = super.readClassDescriptor();
	    	if ( MessageData[].class.getName().equals(desc.getName()) )
	    		return ObjectStreamClass.lookup(MessageDataOld[].class);
	    	if ( MessageData.class.getName().equals(desc.getName()) )
	    		return ObjectStreamClass.lookup(MessageDataOld.class);
	    	return desc;
	    };
	}

	public void fromOld(MessageDataOld old)
	{
		int i;

		this.mid = old.mid;
		this.mstrSubject = old.mstrSubject;
		this.midOwner = old.midOwner;
		this.mlngNumber = old.mlngNumber;
		this.midDirection = old.midDirection;
		this.mbIsEmail = old.mbIsEmail;
		this.mdtDate = old.mdtDate;
		this.mstrBody = old.mstrBody;
		this.marrAddresses = old.marrAddresses;
		this.mstrEmailID = old.mstrEmailID;
		this.mobjDocOps = old.mobjDocOps;
		this.mobjContactOps = old.mobjContactOps;

		if ( old.marrAttachments == null )
			this.marrAttachments = null;
		else
		{
			this.marrAttachments = new MessageAttachmentData[old.marrAttachments.length];
			for ( i = 0; i < this.marrAttachments.length; i++ )
			{
				if ( old.marrAttachments[i] == null )
					this.marrAttachments[i] = null;
				else
				{
					this.marrAttachments[i] = new MessageAttachmentData();
					this.marrAttachments[i].midDocument = old.marrAttachments[i];
					this.marrAttachments[i].midOwner = this.mid;
				}
			}
		}
	}

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

		if ( mbIsEmail && (marrAttachments != null) && (marrAttachments.length > 0) )
		{
			pstrBuilder.append(pstrLineBreak).append("Anexos:").append(pstrLineBreak);
			for ( i = 0; i < marrAttachments.length; i++ )
				marrAttachments[i].Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
