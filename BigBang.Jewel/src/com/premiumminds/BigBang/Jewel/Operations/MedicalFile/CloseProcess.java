package com.premiumminds.BigBang.Jewel.Operations.MedicalFile;

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
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;

public class CloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public Timestamp mdtEndDate;
	public String mstrNotes;
	private Timestamp mdtNextAppt;

	public CloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MedicalFile_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("O processo de acompanhamento da ficha clínica foi encerrado.");

		if ( mdtEndDate != null )
		{
			lstrResult.append(pstrLineBreak);
			lstrResult.append("Data de fim da baixa ou internamento: ");
			lstrResult.append(mdtEndDate.toString().substring(0, 10));
		}

		if ( mstrNotes != null )
		{
			lstrResult.append(pstrLineBreak);
			lstrResult.append("Observações:");
			lstrResult.append(pstrLineBreak);
			lstrResult.append(mstrNotes);
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
		MedicalFile lobjFile;
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;

		lobjFile = (MedicalFile)GetProcess().GetData();
		mdtNextAppt = (Timestamp)lobjFile.getAt(MedicalFile.I.NEXTDATE);

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lobjFile.setAt(MedicalFile.I.NEXTDATE, null);
			lobjFile.SaveToDb(pdb);

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

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("O processo será reaberto.");

		if ( mdtNextAppt != null )
		{
			lstrResult.append(" A data da próxima consulta será preenchida com: ");
			lstrResult.append(mdtNextAppt.toString().substring(0, 10));
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("O processo de acompanhamento da ficha clínica foi reaberto.");

		if ( mdtNextAppt != null )
		{
			lstrResult.append(" A data da próxima consulta foi preenchida com: ");
			lstrResult.append(mdtNextAppt.toString().substring(0, 10));
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		MedicalFile lobjFile;
		AgendaItem lobjItem;

		lobjFile = (MedicalFile)GetProcess().GetData();
		mdtNextAppt = (Timestamp)lobjFile.getAt(MedicalFile.I.NEXTDATE);

		try
		{
			lobjFile.setAt(MedicalFile.I.NEXTDATE, mdtNextAppt);
			lobjFile.SaveToDb(pdb);

			if ( mdtNextAppt != null )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Revisão de Processo de Baixa ou Internamento");
				lobjItem.setAt(1, GetProcess().GetManagerID());
				lobjItem.setAt(2, Constants.ProcID_MedicalFile);
				lobjItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
				lobjItem.setAt(4, mdtNextAppt);
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_MedicalFile_ManageData}, pdb);
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
