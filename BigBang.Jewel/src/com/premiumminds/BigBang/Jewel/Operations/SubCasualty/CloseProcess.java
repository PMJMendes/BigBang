package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
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
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternCloseSubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternUndoCloseSubCasualty;

public class CloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReviewer;
	private Timestamp mdtReviewDate;

	public CloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo foi fechado após revisão.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;
		SubCasualty lobjSubCasualty;

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
			for ( AgendaItem lobjAg: larrItems.values() )
			{
				lobjAg.ClearData(pdb);
				lobjAg.getDefinition().Delete(pdb, lobjAg.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

		try
		{
			lobjSubCasualty = (SubCasualty)GetProcess().GetData();
			midReviewer = (UUID)lobjSubCasualty.getAt(SubCasualty.I.REVIEWER);
			mdtReviewDate = (Timestamp)lobjSubCasualty.getAt(SubCasualty.I.REVIEWDATE);
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWER, null);
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWDATE, null);
			lobjSubCasualty.SaveToDb(pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Revisão de Processo de Sub-Sinistro: Fecho do Processo");
			lobjItem.setAt(1, GetProcess().GetManagerID());
			lobjItem.setAt(2, Constants.ProcID_SubCasualty);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Completed);
			lobjItem.setAt(6, "O processo foi fechado após revisão.");
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Stop(pdb);

		TriggerOp(new ExternCloseSubCasualty(GetProcess().GetParent().getKey()), pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo será reaberto e remarcado para revisão.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo foi reaberto e remarcado para revisão.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjItem;
		SubCasualty lobjSubCasualty;

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
			for ( AgendaItem lobjAg: larrItems.values() )
			{
				lobjAg.ClearData(pdb);
				lobjAg.getDefinition().Delete(pdb, lobjAg.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			lobjSubCasualty = (SubCasualty)GetProcess().GetData();
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWER, midReviewer);
			lobjSubCasualty.setAt(SubCasualty.I.REVIEWDATE, mdtReviewDate);
			lobjSubCasualty.SaveToDb(pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Revisão de Processo de Sub-Sinistro");
			lobjItem.setAt(1, midReviewer);
			lobjItem.setAt(2, Constants.ProcID_SubCasualty);
			lobjItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
			lobjItem.setAt(4, mdtReviewDate);
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()},
					new UUID[] {Constants.OPID_SubCasualty_CloseProcess, Constants.OPID_SubCasualty_RejectClosing}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Restart(pdb);

		TriggerOp(new ExternUndoCloseSubCasualty(GetProcess().GetParent().getKey()), pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
