package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
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
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ReceiveAdditionalInfo
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public int mlngDays;
	public String mstrNotes;
	public String mstrEmailID;
	private boolean mbFromEmail;
	private String mstrSubject;
	private String mstrBody;
	private String mstrNewEmailID;
	private Timestamp mdtPrevLimit;

	public ReceiveAdditionalInfo(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternReq_ReceiveAdditionalInfo;
	}

	public String ShortDesc()
	{
		return "Recepção de Informação Adicional";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação recebida foi a seguinte:");

		if ( mstrNotes != null )
			lstrResult.append(pstrLineBreak).append(mstrNotes);

		if ( mbFromEmail )
		{
			lstrResult.append(pstrLineBreak).append(mstrSubject);
			lstrResult.append(pstrLineBreak).append(mstrBody);
		}

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
		Hashtable<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		ExternRequest lobjRequest;
		AgendaItem lobjNewAgendaItem;
		Item lobjItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, mlngDays);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

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

    	try
    	{
    		lobjRequest = (ExternRequest)GetProcess().GetData();
    		mdtPrevLimit = (Timestamp)lobjRequest.getAt(4);
    		lobjRequest.setAt(4, ldtLimit);
    		lobjRequest.SaveToDb(pdb);

			lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjNewAgendaItem.setAt(0, "Pedido Externo de Informação");
			lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjNewAgendaItem.setAt(2, Constants.ProcID_ExternRequest);
			lobjNewAgendaItem.setAt(3, ldtNow);
			lobjNewAgendaItem.setAt(4, ldtLimit);
			lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);
			lobjNewAgendaItem.SaveToDb(pdb);
			lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_ExternReq_SendInformation}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mstrEmailID != null )
		{
			mbFromEmail = true;
			try
			{
				lobjItem = MailConnector.DoGetItem(mstrEmailID);
				mstrSubject = lobjItem.getSubject();
				mstrBody = lobjItem.getBody().toString();
				mstrNewEmailID = MailConnector.DoProcessItem(mstrEmailID).getId().getUniqueId();
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

		lstrResult = new StringBuilder("A informação será retirada.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido será re-disponibilizado para outra utilização.");

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação foi retirada.");

		if ( mbFromEmail )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		Hashtable<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		ExternRequest lobjRequest;
		AgendaItem lobjNewAgendaItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());

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

    	try
    	{
    		lobjRequest = (ExternRequest)GetProcess().GetData();
    		lobjRequest.setAt(4, mdtPrevLimit);
    		lobjRequest.SaveToDb(pdb);

			lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjNewAgendaItem.setAt(0, "Pedido Externo de Informação");
			lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjNewAgendaItem.setAt(2, Constants.ProcID_ExternRequest);
			lobjNewAgendaItem.setAt(3, ldtNow);
			lobjNewAgendaItem.setAt(4, mdtPrevLimit);
			lobjNewAgendaItem.setAt(5, Constants.UrgID_Valid);
			lobjNewAgendaItem.SaveToDb(pdb);
			lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_ExternReq_ReceiveAdditionalInfo,
					Constants.OPID_ExternReq_CloseProcess}, pdb);
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
		return new UndoSet[0];
	}
}
