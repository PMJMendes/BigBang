package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;

public class MarkForClosing
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midRevisor;
	private String mstrRevisor;
	private String mstrScheduler;

	public MarkForClosing(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_MarkForClosing;
	}

	public String ShortDesc()
	{
		return "Marcação para Encerramento";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O sub-sinistro foi marcado para revisão e encerramento por " + mstrScheduler + "." + pstrLineBreak +
				"Revisor indicado: " + mstrRevisor + ".";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;

		if ( midRevisor.equals(Engine.getCurrentUser()) )
			throw new JewelPetriException("Erro: Não se pode indicar a si próprio para rever o seu próprio processo.");

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

		try
		{
			mstrScheduler = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser()).getDisplayName();
			mstrRevisor = User.GetInstance(Engine.getCurrentNameSpace(), midRevisor).getDisplayName();

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Revisão de Processo de Sub-Sinistro");
			lobjItem.setAt(1, midRevisor);
			lobjItem.setAt(2, Constants.ProcID_SubCasualty);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()},
					new UUID[] {Constants.OPID_SubCasualty_CloseProcess, Constants.OPID_SubCasualty_RejectClosing}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A marcação para encerramento será retirada sem revisão.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A marcação para encerramento foi retirada sem revisão.";
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;

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
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
