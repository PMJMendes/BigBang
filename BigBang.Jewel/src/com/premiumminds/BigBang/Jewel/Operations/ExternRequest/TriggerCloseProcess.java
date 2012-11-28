package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

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
import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;

public class TriggerCloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerCloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternReq_TriggerCloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho por Resposta";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O pedido foi fechado, tendo sido respondido.";
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
		ExternRequest lobjRequest;
		AgendaItem lobjAgendaItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());

    	try
    	{
    		lobjRequest = (ExternRequest)GetProcess().GetData();

			lobjAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjAgendaItem.setAt(0, "Pedido Externo de Informação");
			lobjAgendaItem.setAt(1, Engine.getCurrentUser().toString());
			lobjAgendaItem.setAt(2, Constants.ProcID_ExternRequest);
			lobjAgendaItem.setAt(3, ldtNow);
			lobjAgendaItem.setAt(4, lobjRequest.getAt(4));
			lobjAgendaItem.setAt(5, Constants.UrgID_Valid);
			lobjAgendaItem.SaveToDb(pdb);
			lobjAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_ExternReq_ReceiveAdditionalInfo,
					Constants.OPID_ExternReq_CloseProcess}, pdb);
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
