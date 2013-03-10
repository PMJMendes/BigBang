package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.ILog;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.AccountingData;
import com.premiumminds.BigBang.Jewel.Objects.AccountingEntry;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class DASNotNecessary
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;
	private UUID midPrevStatus;
	private Timestamp mdtLimit;
	private AccountingData[] marrAccounting;

	public DASNotNecessary(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_DASNotNecessary;
	}

	public String ShortDesc()
	{
		return "Indicação de DAS Desnecessária";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Foi dada a indicação que, no caso deste recibo, não seria necessária uma Declaração de Ausência de Sinistro.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		Timestamp ldtAux;
		Calendar ldtAux2;
		Receipt lobjReceipt;
		Calendar ldtToday;
		ILog lobjLog;
		Payment lopP;
		AccountingEntry lobjEntry;
		int i;

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);
    	mdtLimit = new Timestamp(ldtAux2.getTimeInMillis());

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
			lrs = null;

			for ( AgendaItem lobjItem: larrItems.values() )
			{
				if ( ((Timestamp)lobjItem.getAt(4)).getTime() < mdtLimit.getTime() )
					mdtLimit = (Timestamp)lobjItem.getAt(4);

				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		midReceipt = GetProcess().GetDataKey();
		lobjReceipt = (Receipt)GetProcess().GetData();

		ldtToday = Calendar.getInstance();
		ldtToday.set(Calendar.HOUR_OF_DAY, 0);
		ldtToday.set(Calendar.MINUTE, 0);
		ldtToday.set(Calendar.SECOND, 0);
		ldtToday.set(Calendar.MILLISECOND, 0);
		lobjReceipt = (Receipt)GetProcess().GetParent().GetData();
		try
		{
			lobjReceipt.initAccounting(pdb, ldtToday.get(Calendar.YEAR));
			lobjLog = lobjReceipt.getPaymentLog();
			lopP = (Payment)lobjLog.GetOperationData();
			marrAccounting = lopP.getAcctData(ldtToday);

			if ( marrAccounting != null )
			{
				for ( i = 0; i < marrAccounting.length; i++ )
				{
					lobjEntry = AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					marrAccounting[i].ToObject(lobjEntry);
					lobjEntry.SaveToDb(pdb);
				}
			}

			midPrevStatus = (UUID)lobjReceipt.getAt(Receipt.I.STATUS);
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Paid);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A indicação será retirada. Será novamente necessário pedir uma DAS para este recibo.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A indicação foi retirada. É novamente necessário pedir uma DAS para este recibo.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		AgendaItem lobjItem;
		Receipt lobjReceipt;
		int i;
		AccountingEntry lobjEntry;

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

    	try
    	{
			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Pedido de DAS");
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_Receipt);
			lobjItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
			lobjItem.setAt(4, mdtLimit);
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()},
					new UUID[] {Constants.OPID_Receipt_CreateDASRequest, Constants.OPID_Receipt_DASNotNecessary}, pdb);
    	}
    	catch (Throwable e)
    	{
    		throw new JewelPetriException(e.getMessage(), e);
    	}

		if ( marrAccounting != null )
		{
			try
			{
				for ( i = 0; i < marrAccounting.length; i++ )
				{
					if ( marrAccounting[i].mstrSign.equals("D") )
						marrAccounting[i].mstrSign = "C";
					else
						marrAccounting[i].mstrSign = "D";
					marrAccounting[i].mstrDesc = "Reversão de " + marrAccounting[i].mstrDesc;
					lobjEntry = AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					marrAccounting[i].ToObject(lobjEntry);
					lobjEntry.SaveToDb(pdb);
				}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( midPrevStatus != null )
		{
			try
			{
				lobjReceipt.setAt(Receipt.I.STATUS, midPrevStatus);
				lobjReceipt.SaveToDb(pdb);
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
