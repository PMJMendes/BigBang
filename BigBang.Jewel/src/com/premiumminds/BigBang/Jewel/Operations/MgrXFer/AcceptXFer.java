package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternEndManagerTransfer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternUndoEndManagerTransfer;

public class AcceptXFer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public AcceptXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_AcceptXFer;
	}

	public String ShortDesc()
	{
		return "Aceitação da Transferência";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O processo de transferência de gestor foi completado.").append(pstrLineBreak).append("Gestor anterior: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(),
					((MgrXFer)GetProcess().GetData()).GetOldManagerID()).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor anterior.)");
		}

		lstrBuffer.append(pstrLineBreak).append("Novo gestor: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(),
					((MgrXFer)GetProcess().GetData()).GetNewManagerID()).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do novo gestor.)");
		}

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ObjectBase lobjData;
		MgrXFer lobjXFer;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefAux;
		ArrayList<UUID> larrItems;
		ResultSet lrs;
		int i;
		AgendaItem lobjItem;
		ExternEndManagerTransfer lobjTrigger;

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados da transferência não definidos.");
		if ( !(lobjData instanceof MgrXFer) )
			throw new JewelPetriException("Inesperado: Dados da transferência do tipo errado.");
		lobjXFer = (MgrXFer)lobjData;

		GetProcess().GetParent().SetManagerID(lobjXFer.GetNewManagerID(), pdb);
		GetProcess().Stop(pdb);

		larrMembers = new int[] {1};
		larrParams = new java.lang.Object[] {GetProcess().getKey()};

		larrItems = new ArrayList<UUID>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, larrMembers, larrParams, new int[0]);
			while ( lrs.next() )
			{
				larrItems.add(UUID.fromString(lrs.getString(1)));
			}
			lrs.close();
			lrs = null;

			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaItem));
			for ( i = 0; i < larrItems.size(); i++ )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), larrItems.get(i));
				lobjItem.ClearData(pdb);
				lrefAux.Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjTrigger = new ExternEndManagerTransfer(GetProcess().GetParent().getKey());
		lobjTrigger.midProcess = GetProcess().getKey();
		lobjTrigger.mbCancelled = false;
		TriggerOp(lobjTrigger);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O gestor antigo será reposto. O processo de transferência de gestor ficará novamente pendente.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O gestor anterior foi reposto e o processo de transferência foi reaberto.")
				.append(pstrLineBreak).append("Gestor anterior: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(),
					((MgrXFer)GetProcess().GetData()).GetOldManagerID()).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor anterior.)");
		}

		return lstrBuffer.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		ObjectBase lobjData;
		MgrXFer lobjXFer;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;
		ExternUndoEndManagerTransfer lobjTrigger;

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados da transferência não definidos.");
		if ( !(lobjData instanceof MgrXFer) )
			throw new JewelPetriException("Inesperado: Dados da transferência do tipo errado.");
		lobjXFer = (MgrXFer)lobjData;

		GetProcess().GetParent().SetManagerID(lobjXFer.GetOldManagerID(), pdb);
		GetProcess().Restart(pdb);

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	try
    	{
			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, lobjXFer.GetTag());
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_MgrXFer);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Valid);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_CancelXFer}, pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, lobjXFer.GetTag());
			lobjItem.setAt(1, lobjXFer.GetNewManagerID());
			lobjItem.setAt(2, Constants.ProcID_MgrXFer);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()},
					new UUID[] {Constants.OPID_AcceptXFer, Constants.OPID_CancelXFer}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjTrigger = new ExternUndoEndManagerTransfer(GetProcess().GetParent().getKey());
		lobjTrigger.midProcess = GetProcess().getKey();
		lobjTrigger.midReopener = Engine.getCurrentUser();
		TriggerOp(lobjTrigger);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		ObjectBase lobjData;

		try
		{
			lobjData = GetProcess().GetData();
		}
		catch (JewelPetriException e)
		{
			return null;
		}

		larrResult = new UndoSet[1];
		larrResult[0] = new UndoSet();
		larrResult[0].midType = lobjData.getDefinition().getDefObject().getKey();
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrChanged = new UUID[] {lobjData.getKey()};
		larrResult[0].marrCreated = new UUID[0];

		return larrResult;
	}
}
