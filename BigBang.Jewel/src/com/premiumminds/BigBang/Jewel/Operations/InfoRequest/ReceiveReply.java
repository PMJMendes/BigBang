package com.premiumminds.BigBang.Jewel.Operations.InfoRequest;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.IncomingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.InfoRequest;
import com.premiumminds.BigBang.Jewel.Objects.RequestAddress;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ReceiveReply
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public IncomingMessageData mobjMessage;
	private boolean mbFromEmail;
	private String mstrNewEmailID;

	public ReceiveReply(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_InfoReq_ReceiveReply;
	}

	public String ShortDesc()
	{
		return "Recepção da Resposta";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A resposta ao pedido foi a seguinte:");

		if ( mobjMessage.mstrBody != null )
			lstrResult.append(pstrLineBreak).append(mobjMessage.mstrBody);

		if ( mbFromEmail )
		{
			lstrResult.append(pstrLineBreak).append(mobjMessage.mstrSubject);
			lstrResult.append(pstrLineBreak).append(mobjMessage.mstrBody);
		}

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		Item lobjItem;

		larrItems = new Hashtable<UUID, AgendaItem>();
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
			for ( AgendaItem lobjAgendaItem: larrItems.values() )
			{
				lobjAgendaItem.ClearData(pdb);
				lobjAgendaItem.getDefinition().Delete(pdb, lobjAgendaItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.RunSubOp(pdb, GetProcess().GetParent().GetDataKey());

		GetProcess().Stop(pdb);

		if ( mobjMessage.mstrEmailID != null )
		{
			mbFromEmail = true;
			try
			{
				lobjItem = MailConnector.DoGetItem(mobjMessage.mstrEmailID);
				mobjMessage.mstrSubject = lobjItem.getSubject();
				mobjMessage.mstrBody = lobjItem.getBody().toString();
				mstrNewEmailID = MailConnector.DoProcessItem(mobjMessage.mstrEmailID).getId().getUniqueId();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A resposta será retirada e o processo será reaberto.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido será re-disponibilizado para outra utilização.");

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A resposta foi retirada e o processo foi reaberto.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		InfoRequest lobjRequest;
		Timestamp ldtNow;
		RequestAddress[] larrAddresses;
		int i;
		UUID lidUser;
		AgendaItem lobjNewItem;

		GetProcess().Restart(pdb);

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoSubOp(pdb, GetProcess().GetParent().GetDataKey());

		ldtNow = new Timestamp(new java.util.Date().getTime());
		try
		{
			lobjRequest = (InfoRequest)GetProcess().GetData();

			larrAddresses = lobjRequest.GetAddresses(pdb);
			for ( i = 0; i < larrAddresses.length; i++ )
			{
				if ( Constants.UsageID_ReplyTo.equals(larrAddresses[i].getAt(2)) )
				{
					lidUser = (UUID)larrAddresses[i].getAt(3);
					if ( lidUser == null )
						continue;

					lobjNewItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjNewItem.setAt(0, "Pedido de Informação ou Documento");
					lobjNewItem.setAt(1, lidUser);
					lobjNewItem.setAt(2, Constants.ProcID_InfoRequest);
					lobjNewItem.setAt(3, ldtNow);
					lobjNewItem.setAt(4, lobjRequest.getAt(5));
					lobjNewItem.setAt(5, Constants.UrgID_Valid);
					lobjNewItem.SaveToDb(pdb);
					lobjNewItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_InfoReq_ReceiveReply,
							Constants.OPID_InfoReq_RepeatRequest, Constants.OPID_InfoReq_CancelRequest}, pdb);
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mbFromEmail )
		{
			try
			{
				MailConnector.DoUnprocessItem(mstrNewEmailID);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjDocs;

		lobjDocs = GetDocSet();

		if ( lobjDocs != null )
			return new UndoSet[] {lobjDocs};
		else
			return new UndoSet[0];
	}

	private UndoSet GetDocSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjMessage.mobjDocOps != null )
		{
			larrAux = mobjMessage.mobjDocOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Document.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Document;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}
}
