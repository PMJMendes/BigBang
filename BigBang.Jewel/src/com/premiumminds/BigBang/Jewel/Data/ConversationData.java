package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;

public class ConversationData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrSubject;
	public UUID midType;
	public UUID midProcess;
	public UUID midStartDir;
	public UUID midPendingDir;
	public Timestamp mdtDueDate;

	public ConversationData mobjPrevValues;

	public MessageData[] marrMessages;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrSubject   =    (String)pobjSource.getAt(Conversation.I.SUBJECT);
		midType       =      (UUID)pobjSource.getAt(Conversation.I.TYPE);
		midProcess    =      (UUID)pobjSource.getAt(Conversation.I.PROCESS);
		midStartDir   =      (UUID)pobjSource.getAt(Conversation.I.STARTDIRECTION);
		midPendingDir =      (UUID)pobjSource.getAt(Conversation.I.PENDINGDIRECTION);
		mdtDueDate    = (Timestamp)pobjSource.getAt(Conversation.I.DUEDATE);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Conversation.I.SUBJECT,          mstrSubject);
			pobjDest.setAt(Conversation.I.TYPE,             midType);
			pobjDest.setAt(Conversation.I.PROCESS,          midProcess);
			pobjDest.setAt(Conversation.I.STARTDIRECTION,   midStartDir);
			pobjDest.setAt(Conversation.I.PENDINGDIRECTION, midPendingDir);
			pobjDest.setAt(Conversation.I.DUEDATE,          mdtDueDate);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjType;
		int i;

		pstrBuilder.append("Assunto: ").append(mstrSubject).append(pstrLineBreak);

		pstrBuilder.append("Tipo de Informação: ");
		try
		{
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_RequestType), midType);
			pstrBuilder.append(lobjType.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(erro a obter o tipo de informação)");
		}
		pstrBuilder.append(pstrLineBreak);

		if ( Constants.MsgDir_Incoming.equals(midStartDir) )
			pstrBuilder.append("Iniciada por nós.");
		else
			pstrBuilder.append("Iniciada por eles.");
		pstrBuilder.append(pstrLineBreak);

		if ( mdtDueDate != null )
		{
			if ( Constants.MsgDir_Incoming.equals(midPendingDir) )
				pstrBuilder.append("Pendente de envio de nova mensagem, até ");
			else
				pstrBuilder.append("Pendente de recepção de nova mensagem, até ");
			pstrBuilder.append(mdtDueDate.toString().substring(0, 10)).append(pstrLineBreak);
		}

		if ( marrMessages != null )
		{
			for ( i = 0; i < marrMessages.length; i++ )
				marrMessages[i].Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
