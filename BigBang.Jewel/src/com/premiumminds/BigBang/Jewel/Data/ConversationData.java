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
		
		pstrBuilder.append("<style> html * {font-size: 1em !important;}</style>");

		pstrBuilder.append("<div>");
		pstrBuilder.append("<p><b>");
		pstrBuilder.append("Assunto da troca de mensagens: ");
		pstrBuilder.append("</b>");
		pstrBuilder.append(mstrSubject);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("</p>");
		
		pstrBuilder.append("<p><b>");
		pstrBuilder.append("Tipo de Informação: ");
		pstrBuilder.append("</b>");
		
		try
		{
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_RequestType), midType);
			pstrBuilder.append(lobjType.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(erro a obter o tipo de informação)");
		}
		pstrBuilder.append("</p>");

		pstrBuilder.append("<p><b>");
		if ( Constants.MsgDir_Incoming.equals(midStartDir) )
			pstrBuilder.append("Iniciada por nós.");
		else
			pstrBuilder.append("Iniciada por eles.");
		pstrBuilder.append("</b></p>");

		if ( mdtDueDate != null )
		{
			pstrBuilder.append("<p><b>");
			
			if ( Constants.MsgDir_Incoming.equals(midPendingDir) )
				pstrBuilder.append("Pendente de envio de nova mensagem, até ");
			else
				pstrBuilder.append("Pendente de recepção de nova mensagem, até ");
			pstrBuilder.append(mdtDueDate.toString().substring(0, 10)).append(pstrLineBreak);
			
			pstrBuilder.append("</b></p>");
			
		}
		
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append(pstrLineBreak);
		
		pstrBuilder.append("</div>");

		if ( marrMessages != null )
		{
			pstrBuilder.append("<hr>");
			
			for ( i = 0; i < marrMessages.length; i++ ) {
				
				pstrBuilder.append("<div>");
				
				marrMessages[i].Describe(pstrBuilder, pstrLineBreak);
				
				pstrBuilder.append(pstrLineBreak);
				pstrBuilder.append(pstrLineBreak);
				pstrBuilder.append("</div>");
				
				if (i < marrMessages.length-1) {
					pstrBuilder.append(pstrLineBreak);
					pstrBuilder.append("<hr>");
				}
			}
		}
	}
}
