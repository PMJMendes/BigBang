package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnector;

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
	public String mstrFolderID;

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
		mstrEmailID = (String)pobjSource.getAt(Message.I.EMAILID);
		mstrFolderID = (String)pobjSource.getAt(Message.I.FOLDERID);
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
			pobjDest.setAt(Message.I.EMAILID,   mstrEmailID);
			pobjDest.setAt(Message.I.FOLDERID,  mstrFolderID);
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
		
		MessageData stgMessageData;
		try {
			stgMessageData = mbIsEmail ? StorageConnector.getAsData(mstrEmailID) : null;
		} catch (BigBangJewelException e) {
			stgMessageData = null;
		}

		pstrBuilder.append("<p><b>");
		
		if ( mlngNumber == 0 )
			pstrBuilder.append("Mensagem inicial, ");
		else
			pstrBuilder.append("Mensagem número ").append(mlngNumber+1).append(", ");
				
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
		pstrBuilder.append(mdtDate.toString().substring(0, 19));
		
		pstrBuilder.append("</b></p>");
		
		
		MessageAddressData[] messageAddresses = (stgMessageData != null && stgMessageData.marrAddresses != null)  ? 
				stgMessageData.marrAddresses : marrAddresses;
		
		if (mbIsEmail && messageAddresses != null && messageAddresses.length > 0)
		{
			pstrBuilder.append("<p><b>");
			pstrBuilder.append("Endereços:").append(pstrLineBreak);
			pstrBuilder.append("</b></p>");
			String from = "", to = "", replyTo = "", cc = "", bcc = "", other = "";
			for ( i = 0; i < messageAddresses.length; i++ ) {
				MessageAddressData tmp = messageAddresses[i];
				UUID tmpUsage =  tmp.midUsage;
				if (Constants.UsageID_From.equals(tmpUsage)) {
					from = from.length()>0 ? from + "; " + tmp.mstrAddress : from + tmp.mstrAddress;
				} else if ( Constants.UsageID_To.equals(tmpUsage) ) {
					to = to.length()>0 ? to + "; " + tmp.mstrAddress : to + tmp.mstrAddress;
				} else if ( Constants.UsageID_CC.equals(tmpUsage) ) {
					cc = cc.length()>0 ? cc + "; " + tmp.mstrAddress : cc + tmp.mstrAddress;
				} else if ( Constants.UsageID_BCC.equals(tmpUsage) ) {
					bcc = bcc.length()>0 ? bcc + "; " + tmp.mstrAddress : bcc + tmp.mstrAddress;
				} else if ( Constants.UsageID_ReplyTo.equals(tmpUsage) ) {
					replyTo = replyTo.length()>0 ? replyTo + "; " + tmp.mstrAddress : replyTo + tmp.mstrAddress;
				} else {
					other = other.length()>0 ? other + "; " + tmp.mstrAddress : other + tmp.mstrAddress;
				} 
			}
			
			pstrBuilder.append("<p>");
			if (from.length()>0) {
				pstrBuilder.append("<b>");
				pstrBuilder.append("DE: ");
				pstrBuilder.append("</b>");
				pstrBuilder.append(from);
				pstrBuilder.append(pstrLineBreak);
			}
			if (replyTo.length()>0) {
				pstrBuilder.append("<b>");
				pstrBuilder.append("RESPONDER A: ");
				pstrBuilder.append("</b>");
				pstrBuilder.append(replyTo);
				pstrBuilder.append(pstrLineBreak);
			}
			if (to.length()>0) {
				pstrBuilder.append("<b>");
				pstrBuilder.append("PARA: ");
				pstrBuilder.append("</b>");
				pstrBuilder.append(to);
				pstrBuilder.append(pstrLineBreak);
			}
			if (cc.length()>0) {
				pstrBuilder.append("<b>");
				pstrBuilder.append("CC: ");
				pstrBuilder.append("</b>");
				pstrBuilder.append(cc);
				pstrBuilder.append(pstrLineBreak);
			} 
			if (bcc.length()>0) {
				pstrBuilder.append("<b>");
				pstrBuilder.append("BCC: ");
				pstrBuilder.append("</b>");
				pstrBuilder.append(bcc);
				pstrBuilder.append(pstrLineBreak);
			} 
			if (other.length()>0) {
				pstrBuilder.append("<b>");
				pstrBuilder.append("OUTROS ENDEREÇOS: ");
				pstrBuilder.append("</b>");
				pstrBuilder.append(other);
				pstrBuilder.append(pstrLineBreak);
			} 
			pstrBuilder.append("</p>");
		}
		
		pstrBuilder.append(pstrLineBreak);
		
		pstrBuilder.append("<p><b>");
		pstrBuilder.append("Assunto da Mensagem: ");
		pstrBuilder.append("</b>");
		String subject = (stgMessageData != null && stgMessageData.mstrSubject != null)  ? 
				stgMessageData.mstrSubject : mstrSubject;
		if (subject == null || subject.length() == 0) {
			subject = "Mensagem sem assunto";
		}
		pstrBuilder.append(subject);
		pstrBuilder.append("</p>");
		
		pstrBuilder.append("<p><b>");
		pstrBuilder.append("Corpo da Mensagem:");
		pstrBuilder.append("</b>");
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("</p>");
		
		String body = (stgMessageData != null && stgMessageData.mstrBody != null)  ? 
				stgMessageData.mstrBody : mstrBody;
		if (body == null || body.length() == 0) {
			body = "Mensagem sem corpo";
		}
		pstrBuilder.append(body);
	}
}
