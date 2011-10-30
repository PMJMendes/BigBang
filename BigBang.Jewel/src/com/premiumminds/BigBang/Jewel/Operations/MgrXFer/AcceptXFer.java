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
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternEndClientMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternUndoEndClientMgrXFer;

public class AcceptXFer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public boolean mbMassTransfer;
	public UUID midParentProc;
	private int mlngCount;
	private UUID midNewManager;
	private UUID[] marrOldManagers;

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

		lstrBuffer.append("O processo de transferência de gestor foi completado.");

		if ( mbMassTransfer)
		{
			lstrBuffer.append(pstrLineBreak).append("Nº de processos transferidos: ").append(mlngCount);
		}
		else
		{
			lstrBuffer.append(pstrLineBreak).append("Gestor anterior: ");
			try
			{
				lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), marrOldManagers[0]).getDisplayName());
			}
			catch (Throwable e)
			{
				lstrBuffer.append("(Erro a obter o nome do gestor anterior.)");
			}
		}

		lstrBuffer.append(pstrLineBreak).append("Novo gestor: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), midNewManager).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do novo gestor.)");
		}

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midParentProc;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ObjectBase lobjData;
		MgrXFer lobjXFer;
		UUID[] larrProcs;
		int i;
		IProcess lobjProc;
		ArrayList<UUID> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		AgendaItem lobjItem;

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados da transferência não definidos.");
		if ( !(lobjData instanceof MgrXFer) )
			throw new JewelPetriException("Inesperado: Dados da transferência do tipo errado.");
		lobjXFer = (MgrXFer)lobjData;
		if ( lobjXFer.IsMassTransfer() && !mbMassTransfer )
			throw new JewelPetriException("Erro: Esta transferência de gestor faz parte de um processo de transferência em massa.");
		if ( mbMassTransfer && !lobjXFer.IsMassTransfer() )
			throw new JewelPetriException("Inesperado: Esta transferência de gestor não faz parte de um processo de transferência em massa.");

		midNewManager = lobjXFer.GetNewManagerID();

		if ( mbMassTransfer )
		{
			midParentProc = null;
			larrProcs = lobjXFer.GetProcessIDs();
			mlngCount = larrProcs.length;
		}
		else
		{
			midParentProc = GetProcess().GetParent().getKey();
			larrProcs = new UUID[] {midParentProc};
			mlngCount = 1;
		}

		marrOldManagers = new UUID[mlngCount];
		for ( i = 0; i < mlngCount; i++ )
		{
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcs[i]);
			marrOldManagers[i] = lobjProc.GetManagerID();
			lobjProc.SetManagerID(midNewManager, pdb);
			TriggerOp(GetRunTrigger(lobjXFer.GetOuterObjectType(), larrProcs[i], marrOldManagers[i]));
		}

		larrItems = new ArrayList<UUID>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
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

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O gestor antigo será reposto. O processo de transferência de gestor ficará novamente pendente.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O gestor anterior foi reposto e o processo de transferência foi reaberto.");

		if ( mbMassTransfer )
		{
			lstrBuffer.append(pstrLineBreak).append("Nº de processos envolvidos: ").append(mlngCount);
		}
		else
		{
			lstrBuffer.append(pstrLineBreak).append("Gestor anterior: ");
			try
			{
				lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(),
						((MgrXFer)GetProcess().GetData()).GetOldManagerID()).getDisplayName());
			}
			catch (Throwable e)
			{
				lstrBuffer.append("(Erro a obter o nome do gestor anterior.)");
			}
		}

		return lstrBuffer.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		ObjectBase lobjData;
		MgrXFer lobjXFer;
		UUID[] larrProcs;
		int i;
		IProcess lobjProc;
		Timestamp ldtAux;
		Calendar ldtAux2;
		AgendaItem lobjItem;

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados da transferência não definidos.");
		if ( !(lobjData instanceof MgrXFer) )
			throw new JewelPetriException("Inesperado: Dados da transferência do tipo errado.");
		lobjXFer = (MgrXFer)lobjData;

		if ( mbMassTransfer )
		{
			larrProcs = lobjXFer.GetProcessIDs();
		}
		else
		{
			larrProcs = new UUID[] {midParentProc};
		}

		for ( i = 0; i < larrProcs.length; i++ )
		{
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcs[i]);
			lobjProc.SetManagerID(marrOldManagers[i], pdb);
			TriggerOp(GetUndoTrigger(lobjXFer.GetOuterObjectType(), larrProcs[i]));
		}

		GetProcess().Restart(pdb);

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	try
    	{
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

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, lobjXFer.GetTag());
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_MgrXFer);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Valid);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_CancelXFer}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		MgrXFer lobjData;
		UUID[] larrProcs;
		int i;
		IProcess lobjProc;

		try
		{
			lobjData = (MgrXFer)GetProcess().GetData();
		}
		catch (Throwable e)
		{
			return null;
		}

		larrResult = new UndoSet[1];
		larrResult[0] = new UndoSet();
		larrResult[0].midType = lobjData.GetOuterObjectType();
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrCreated = new UUID[0];

		if ( mbMassTransfer )
			larrProcs = lobjData.GetProcessIDs();
		else
			larrProcs = new UUID[] {midParentProc};

		larrResult[0].marrChanged = new UUID[larrProcs.length];
		for ( i = 0; i < larrProcs.length; i++ )
		{
			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcs[i]);
				larrResult[0].marrChanged[i] = lobjProc.GetData().getKey();
			}
			catch (Throwable e)
			{
				larrResult[0].marrChanged[i] = null;
			}
		}

		return larrResult;
	}

	private Operation GetRunTrigger(UUID pidObjectType, UUID pidProc, UUID pidOldMgr)
		throws JewelPetriException
	{
		ExternEndMgrXFerBase lopResult;

		lopResult = null;

		if ( Constants.ObjID_Client.equals(pidObjectType) )
			lopResult = new ExternEndClientMgrXFer(pidProc);

		if ( lopResult != null )
		{
			lopResult.midXFerProcess = GetProcess().getKey();
			lopResult.mbAccepted = true;
			lopResult.midOldManager = pidOldMgr;
			lopResult.midNewManager = midNewManager;
		}

		return lopResult;
	}

	private Operation GetUndoTrigger(UUID pidObjectType, UUID pidProc)
		throws JewelPetriException
	{
		ExternUndoEndMgrXFerBase lopResult;

		lopResult = null;

		if ( Constants.ObjID_Client.equals(pidObjectType) )
			lopResult = new ExternUndoEndClientMgrXFer(pidProc);

		if ( lopResult != null )
		{
			lopResult.midProcess = GetProcess().getKey();
			lopResult.midReopener = Engine.getCurrentNameSpace();
		}

		return lopResult;
	}
}
