package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

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
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternEndClientMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternUndoEndClientMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExternEndPolicyMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExternUndoEndPolicyMgrXFer;

public class CancelXFer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public boolean mbMassTransfer;
	public UUID midParentProc;
	private int mlngCount;
	private UUID midNewManager;
	private UUID[] marrOldManagers;

	public CancelXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_CancelXFer;
	}

	public String ShortDesc()
	{
		return "Cancelamento da Transferência";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O processo de transferência de gestor foi cancelado.");

		if ( mbMassTransfer)
		{
			lstrBuffer.append(pstrLineBreak).append("Nº de processos envolvidos: ").append(mlngCount);
		}

		lstrBuffer.append(pstrLineBreak).append("Gestor pretendido: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), midNewManager).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor pretendido.)");
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
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;

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
			marrOldManagers[i] = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcs[i]).GetManagerID();
			TriggerOp(GetRunTrigger(lobjXFer.GetOuterObjectType(), larrProcs[i], marrOldManagers[i]), pdb);
		}

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
			lrs = null;

			for ( AgendaItem lobjItem: larrItems.values() )
			{
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
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
		return "O processo de transferência de gestor será reaberto e ficará novamente pendente.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("O processo de transferência foi reaberto.")
				.append(pstrLineBreak).append("Gestor pedido: ");
		try
		{
			lstrBuffer.append(User.GetInstance(Engine.getCurrentNameSpace(), midNewManager).getDisplayName());
		}
		catch (Throwable e)
		{
			lstrBuffer.append("(Erro a obter o nome do gestor pedido.)");
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
			TriggerOp(GetUndoTrigger(lobjXFer.GetOuterObjectType(), larrProcs[i]), pdb);

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
		return new UndoSet[0];
	}

	private Operation GetRunTrigger(UUID pidObjectType, UUID pidProc, UUID pidOldMgr)
		throws JewelPetriException
	{
		ExternEndMgrXFerBase lopResult;

		lopResult = null;

		if ( Constants.ObjID_Client.equals(pidObjectType) )
			lopResult = new ExternEndClientMgrXFer(pidProc);

		if ( Constants.ObjID_Policy.equals(pidObjectType) )
			lopResult = new ExternEndPolicyMgrXFer(pidProc);

		if ( lopResult != null )
		{
			lopResult.midXFerProcess = GetProcess().getKey();
			lopResult.mbAccepted = false;
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

		if ( Constants.ObjID_Policy.equals(pidObjectType) )
			lopResult = new ExternUndoEndPolicyMgrXFer(pidProc);

		if ( lopResult != null )
		{
			lopResult.midProcess = GetProcess().getKey();
			lopResult.midReopener = Engine.getCurrentUser();
		}

		return lopResult;
	}
}
