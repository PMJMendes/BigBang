package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.MessageAttachment;

public class MessageAttachmentData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midOwner;
	public UUID midDocument;
	public String mstrAttId;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midOwner    =   (UUID)pobjSource.getAt(MessageAttachment.I.OWNER);
		midDocument =   (UUID)pobjSource.getAt(MessageAttachment.I.DOCUMENT);
		mstrAttId   = (String)pobjSource.getAt(MessageAttachment.I.ATTACHMENTID);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MessageAttachment.I.OWNER,        midOwner);
			pobjDest.setAt(MessageAttachment.I.DOCUMENT,     midDocument);
			pobjDest.setAt(MessageAttachment.I.ATTACHMENTID, mstrAttId);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		Document lobjDoc;
		DocumentData lobjData;


		if ( mstrAttId !=  null )
		{
			pstrBuilder.append(pstrLineBreak).append("Anexo arquivado no servidor de Email.").append(pstrLineBreak);
		}

		if ( midDocument != null )
		{
			try
			{
				lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), midDocument);
			}
			catch (BigBangJewelException e)
			{
				pstrBuilder.append("(Erro a obter a definição do anexo.)").append(pstrLineBreak);
				return;
			}
			lobjData = new DocumentData();
			lobjData.FromObject(lobjDoc);
			lobjData.Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
