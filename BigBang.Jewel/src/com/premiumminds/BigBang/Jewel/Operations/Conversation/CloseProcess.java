package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;

public class CloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midMotive;
	private String mstrMotive;
	private UUID midPrevDir;
	private Timestamp mdtPrevLimit;

	public CloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho Manual do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O pedido foi fechado, pelo seguinte motivo:" + pstrLineBreak + mstrMotive;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		Conversation lobjConv;

		if ( midMotive == null )
			throw new JewelPetriException("Erro: É obrigatório indicar o motivo do encerramento do processo.");
		else
		{
			try
			{
				mstrMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_RequestCancelMotives), midMotive).getLabel();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

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

		lobjConv = (Conversation)GetProcess().GetData();
		mdtPrevLimit = (Timestamp)lobjConv.getAt(Conversation.I.DUEDATE);
		midPrevDir = (UUID)lobjConv.getAt(Conversation.I.PENDINGDIRECTION);
		try
		{
			lobjConv.setAt(Conversation.I.DUEDATE, null);
			lobjConv.setAt(Conversation.I.PENDINGDIRECTION, null);
			lobjConv.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo do pedido será reaberto.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O pedido foi reaberto.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		Conversation lobjConv;
		UUID lidUrgency;
		UUID[] larrUsers;
		int i;
		AgendaItem lobjAgendaItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	try
    	{
    		lobjConv = (Conversation)GetProcess().GetData();
			lobjConv.setAt(Conversation.I.DUEDATE, mdtPrevLimit);
			lobjConv.setAt(Conversation.I.PENDINGDIRECTION, midPrevDir);
			lobjConv.SaveToDb(pdb);

    		if ( Constants.MsgDir_Incoming.equals(midPrevDir) )
    			lidUrgency = Constants.UrgID_Valid;
    		else
    			lidUrgency = Constants.UrgID_Pending;

    		larrUsers = lobjConv.GetUsers(pdb);
			for ( i = 0; i < larrUsers.length; i++ )
			{
				lobjAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAgendaItem.setAt(0, "Troca de Mensagens");
				lobjAgendaItem.setAt(1, larrUsers[i]);
				lobjAgendaItem.setAt(2, Constants.ProcID_Conversation);
				lobjAgendaItem.setAt(3, ldtNow);
				lobjAgendaItem.setAt(4, mdtPrevLimit);
				lobjAgendaItem.setAt(5, lidUrgency);
				lobjAgendaItem.SaveToDb(pdb);
				lobjAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Conversation_SendMessage,
						Constants.OPID_Conversation_ReSendMessage, Constants.OPID_Conversation_ReceiveMessage,
						Constants.OPID_Conversation_CloseProcess}, pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
