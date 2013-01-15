package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.Objects.MessageAttachment;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ExternAbortProcess
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrReviveEmailID;
	public transient ConversationData mobjData;

	public ExternAbortProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_ExternAbortProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProcess;
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		MessageAttachment lobjAttachment;
		MessageAddress lobjAddr;
		Message lobjMsg;
		Conversation lobjConv;
		int i, j;

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			for ( AgendaItem lobjItem: larrItems.values() )
			{
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjProcess = GetProcess();
		lobjProcess.Stop(pdb);
		lobjProcess.SetDataObjectID(null, pdb);

		try
		{
			if ( mobjData != null )
			{
				if ( mobjData.marrMessages != null )
				{
					for ( i = 0; i < mobjData.marrMessages.length; i++ )
					{
						if ( mobjData.marrMessages[i].marrAttachments != null )
						{
							for ( j = 0; j < mobjData.marrMessages[i].marrAttachments.length; j++ )
							{
								lobjAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(),
										mobjData.marrMessages[i].marrAttachments[j].mid);
								lobjAttachment.getDefinition().Delete(pdb, lobjAttachment.getKey());
							}
						}
						if ( mobjData.marrMessages[i].marrAddresses != null )
						{
							for ( j = 0; j < mobjData.marrMessages[i].marrAddresses.length; j++ )
							{
								lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(),
										mobjData.marrMessages[i].marrAddresses[j].mid);
								lobjAddr.getDefinition().Delete(pdb, lobjAddr.getKey());
							}
						}
						lobjMsg = Message.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrMessages[i].mid);
						lobjMsg.getDefinition().Delete(pdb, lobjMsg.getKey());
					}
				}
				lobjConv = Conversation.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);
				lobjConv.getDefinition().Delete(pdb, lobjConv.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mstrReviveEmailID != null )
		{
			try
			{
				MailConnector.DoUnprocessItem(mstrReviveEmailID);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}
}
