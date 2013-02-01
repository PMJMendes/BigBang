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
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ValidateReceipt
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public ReceiptData mobjData;
	public DocOps mobjDocOps;
	private UUID midReceipt;
	private UUID midPrevManager;
	private Timestamp mdtPrevLimit;

	public ValidateReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ValidateReceipt;
	}

	public String ShortDesc()
	{
		return "Validação Manual";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("O recibo foi validado após conferência manual.").append(pstrLineBreak);

		if ( mobjData != null )
		{
			lstrBuilder.append("Novos dados do recibo:");
			lstrBuilder.append(pstrLineBreak);
			mobjData.Describe(lstrBuilder, pstrLineBreak);
		}

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		Receipt lobjAux;
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;

		lobjProc = GetProcess();

		midReceipt = lobjProc.GetDataKey();
		midPrevManager = lobjProc.GetManagerID();

		lobjProc.SetManagerID(Engine.getCurrentUser(), pdb);

		if ( mobjData != null )
		{
			lobjAux = (Receipt)lobjProc.GetData();

			mobjData.mobjPrevValues = new ReceiptData();
			mobjData.mobjPrevValues.FromObject(lobjAux);

			mobjData.midManager = midPrevManager;

			try
			{
				mobjData.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( mobjDocOps != null )
			mobjDocOps.RunSubOp(pdb, midReceipt);

		mdtPrevLimit = null;
		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {lobjProc.getKey()}, new int[0]);
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
				if ( (mdtPrevLimit == null) || (mdtPrevLimit.getTime() > ((Timestamp)lobjItem.getAt(4)).getTime()) )
					mdtPrevLimit = (Timestamp)lobjItem.getAt(4);
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("A validação será retirada.").append(pstrLineBreak);

		if ( mobjData != null )
		{
			lstrBuilder.append("Os dados anteriores serão repostos:");
			lstrBuilder.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrBuilder, pstrLineBreak);
		}

		if ( mobjDocOps != null )
			mobjDocOps.UndoDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("A validação foi retirada.").append(pstrLineBreak);

		if ( mobjData != null )
		{
			lstrBuilder.append("Os dados anteriores foram repostos:");
			lstrBuilder.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrBuilder, pstrLineBreak);
		}

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjAux;
		IProcess lobjProc;
		AgendaItem lobjItem;
		Timestamp ldtNow;
		Calendar ldtAux2;

		if ( mobjData != null )
		{
			try
			{
				lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);
				mobjData.mobjPrevValues.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);
	    	}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( mobjDocOps != null )
			mobjDocOps.UndoSubOp(pdb, midReceipt);

		lobjProc = GetProcess();

		ldtNow = new Timestamp(new java.util.Date().getTime());

		if ( mdtPrevLimit == null )
		{
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtNow.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);
	    	mdtPrevLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

    	try
    	{
			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Validação de Recibo");
			lobjItem.setAt(1, lobjProc.GetManagerID());
			lobjItem.setAt(2, Constants.ProcID_Receipt);
			lobjItem.setAt(3, ldtNow);
			lobjItem.setAt(4, mdtPrevLimit);
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_Receipt_ValidateReceipt,
					Constants.OPID_Receipt_SetReturnToInsurer}, pdb);
    	}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjProc.SetManagerID(midPrevManager, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = new UUID[]{midReceipt};

		return new UndoSet[]{lobjSet};
	}
}
