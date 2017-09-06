package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import javax.mail.Message;

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
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ReceiveQuote
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public IncomingMessageData mobjMessage;
	private boolean mbFromEmail;
	private Timestamp mdtPrevLimit;
	private String mstrNewEmailID;

	public ReceiveQuote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_ReceiveQuote;
	}

	public String ShortDesc()
	{
		return "Recepção de Cotação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A cotação recebida foi a seguinte:");

		if ( mbFromEmail )
			lstrResult.append(pstrLineBreak).append(mobjMessage.mstrSubject);

		if ( mobjMessage.mstrBody != null )
			lstrResult.append(pstrLineBreak).append(mobjMessage.mstrBody);

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
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjNewAgendaItem;
		Message lobjItem;

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

		mdtPrevLimit = null;
		try
		{
			for ( AgendaItem lobjAgendaItem: larrItems.values() )
			{
				if ( (mdtPrevLimit == null) || (mdtPrevLimit.getTime() > ((Timestamp)lobjAgendaItem.getAt(4)).getTime()) )
					mdtPrevLimit = (Timestamp)lobjAgendaItem.getAt(4);
				lobjAgendaItem.ClearData(pdb);
				lobjAgendaItem.getDefinition().Delete(pdb, lobjAgendaItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, 7);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

    	try
    	{
			lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjNewAgendaItem.setAt(0, "Cotação Recebida");
			lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjNewAgendaItem.setAt(2, Constants.ProcID_Negotiation);
			lobjNewAgendaItem.setAt(3, ldtNow);
			lobjNewAgendaItem.setAt(4, ldtLimit);
			lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);
			lobjNewAgendaItem.SaveToDb(pdb);
			lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Negotiation_SendGrant,
					Constants.OPID_Negotiation_CancelNegotiation}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

    	if ( mobjMessage != null )
    	{
			if ( mobjMessage.mobjDocOps != null )
				mobjMessage.mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());

			if ( mobjMessage.mstrEmailID != null )
			{
				mbFromEmail = true;
				try
				{
					lobjItem = MailConnector.conditionalGetMessage(mobjMessage.mstrFolderID, mobjMessage.mstrEmailID);
					mobjMessage.mstrSubject = lobjItem.getSubject();
					mobjMessage.mstrBody = lobjItem.getContent().toString();
					mstrNewEmailID = MailConnector.processItem(mobjMessage.mstrEmailID, mobjMessage.mstrFolderID, lobjItem, null).get("_");
				}
				catch (Throwable e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				}
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação será retirada.");

		if ( mstrNewEmailID != null )
//		if ( mbIsEmail )
			lstrResult.append(" O email recebido será re-disponibilizado para outra utilização.");

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação foi retirada.");

		if ( mstrNewEmailID != null )
//		if ( mbIsEmail )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjNewAgendaItem;

		if ( mobjMessage.mobjDocOps != null )
			mobjMessage.mobjDocOps.UndoSubOp(pdb, GetProcess().GetDataKey());

		ldtNow = new Timestamp(new java.util.Date().getTime());

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

    	try
    	{
			lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjNewAgendaItem.setAt(0, "Resposta a Pedido de Cotação");
			lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjNewAgendaItem.setAt(2, Constants.ProcID_Negotiation);
			lobjNewAgendaItem.setAt(3, ldtNow);
			lobjNewAgendaItem.setAt(4, mdtPrevLimit);
			lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);
			lobjNewAgendaItem.SaveToDb(pdb);
			lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Negotiation_ReceiveQuote,
					Constants.OPID_Negotiation_RepeatQuoteRequest, Constants.OPID_Negotiation_CancelNegotiation}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mstrNewEmailID != null )
//		if ( mbFromEmail )
		{
			try
			{
				MailConnector.DoUnprocessItem(/*mobjMessage.mstrEmailID*/mstrNewEmailID/*, mobjMessage.mbFromSent*/);
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
