package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.ResultSet;
import java.sql.Timestamp;
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
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class SetReturnToInsurer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midMotive;
	private UUID midReceipt;
	private String mstrMotive;
	private Timestamp mdtPrevLimit;
	private boolean mbWithAgenda;

	public SetReturnToInsurer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_SetReturnToInsurer;
	}

	public String ShortDesc()
	{
		return "Indicação de Devolução";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi marcado para devolução pelo seguinte motivo:" + pstrLineBreak + mstrMotive;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		ObjectBase lobjMotive;
		Receipt lobjReceipt;
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;

		lobjProc = GetProcess();
		midReceipt = lobjProc.GetDataKey();

		try
		{
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptReturnMotives),
					midMotive);

			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
			lobjReceipt.setAt(15, lobjMotive.getLabel());
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mdtPrevLimit = null;
		mbWithAgenda = false;
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
				mbWithAgenda = true;
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
		return "A indicação de devolução será retirada. O recibo ficará novamente disponível para conferência.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A indicação de devolução foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		AgendaItem lobjItem;
		Timestamp ldtNow;
		Receipt lobjReceipt;

		if ( mbWithAgenda )
		{
			lobjProc = GetProcess();

			ldtNow = new Timestamp(new java.util.Date().getTime());

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
		}

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
			lobjReceipt.setAt(15, null);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
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
