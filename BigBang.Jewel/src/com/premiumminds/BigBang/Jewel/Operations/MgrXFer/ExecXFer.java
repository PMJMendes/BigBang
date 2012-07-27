package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Object;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;

public class ExecXFer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midNewManager;
	private int mlngCount;
	private UUID[] marrOldManagers;
	private UUID midObjType;

	public ExecXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MgrXFer_ExecXFer;
	}

	public String ShortDesc()
	{
		return "Execução da Transferência";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Foi executado um processo de transferência de gestor em massa.");

		lstrBuffer.append(pstrLineBreak).append("Nº de processos transferidos: ").append(mlngCount);

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
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		ObjectBase lobjData;
		MgrXFer lobjXFer;
		UUID[] larrProcs;
		Hashtable<UUID, ArrayList<String>> larrNotifies;
		int i;
		IProcess lobjProc;
		String lstrUser;
		String lstrObjName;
		AgendaItem lobjItem;

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados da transferência não definidos.");
		if ( !(lobjData instanceof MgrXFer) )
			throw new JewelPetriException("Inesperado: Dados da transferência do tipo errado.");
		lobjXFer = (MgrXFer)lobjData;

		try
		{
			midObjType = lobjXFer.GetOuterObjectType();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midNewManager = lobjXFer.GetNewManagerID();

		larrProcs = lobjXFer.GetProcessIDs();
		mlngCount = larrProcs.length;

		marrOldManagers = new UUID[mlngCount];
		larrNotifies = new Hashtable<UUID, ArrayList<String>>();
		for ( i = 0; i < mlngCount; i++ )
		{
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcs[i]);
			marrOldManagers[i] = lobjProc.GetManagerID();

			if ( midNewManager.equals(marrOldManagers[i]) )
				continue;

			if ( larrNotifies.get(marrOldManagers[i]) == null )
				larrNotifies.put(marrOldManagers[i], new ArrayList<String>());
			larrNotifies.get(marrOldManagers[i]).add(lobjProc.GetData().getLabel());

			GetRunTrigger(midObjType, larrProcs[i], marrOldManagers[i]).Execute(pdb);
		}

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

		try
		{
			lstrUser = User.GetInstance(Engine.getCurrentNameSpace(), midNewManager).getDisplayName();
			lstrObjName = Object.GetInstance(midObjType).getLabel();
		}
		catch (Throwable e)
		{
			lstrUser = "(erro a obter o nome do utilizador)";
			lstrObjName = "(erro a obter o tipo de objecto transferido)";
		}
		for ( UUID lid: larrNotifies.keySet() )
		{
			if ( !lid.equals(Engine.getCurrentUser()) )
			{
	    		try
	    		{
					lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjItem.setAt(0, "Transferência de Gestor de " + lstrObjName);
					lobjItem.setAt(1, lid);
					lobjItem.setAt(2, lobjXFer.GetOuterScript());
					lobjItem.setAt(3, ldtAux);
					lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
					lobjItem.setAt(5, Constants.UrgID_Completed);
					lobjItem.setAt(6, larrNotifies.get(lid).size() + " " + lstrObjName + "(s) foram transferidos para " + lstrUser + ".");
					lobjItem.SaveToDb(pdb);
					lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {}, pdb);
	        	}
	    		catch (Throwable e)
	    		{
	    			throw new JewelPetriException(e.getMessage(), e);
	    		}
			}
		}
		if ( !midNewManager.equals(Engine.getCurrentUser()) )
		{
    		try
    		{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de " + lstrObjName);
				lobjItem.setAt(1, midNewManager);
				lobjItem.setAt(2, lobjXFer.GetOuterScript());
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Completed);
				lobjItem.setAt(6, mlngCount + " " + lstrObjName + "(s) foram transferidos para " + lstrUser + ".");
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {}, pdb);
        	}
    		catch (Throwable e)
    		{
    			throw new JewelPetriException(e.getMessage(), e);
    		}
		}

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "Os gestores antigos serão repostos.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Os gestores anteriores foram repostos.");

		lstrBuffer.append(pstrLineBreak).append("Nº de processos envolvidos: ").append(mlngCount);

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

		lobjData = GetProcess().GetData();
		if ( lobjData == null )
			throw new JewelPetriException("Inesperado: Dados da transferência não definidos.");
		if ( !(lobjData instanceof MgrXFer) )
			throw new JewelPetriException("Inesperado: Dados da transferência do tipo errado.");
		lobjXFer = (MgrXFer)lobjData;

		larrProcs = lobjXFer.GetUndoableProcessIDs();

		for ( i = 0; i < larrProcs.length; i++ )
		{
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcs[i]);
			lobjProc.SetManagerID(marrOldManagers[i], pdb);
			TriggerOp(GetUndoTrigger(midObjType, larrProcs[i], marrOldManagers[i]), pdb);
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
		larrResult[0].midType = midObjType;
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrCreated = new UUID[0];

		larrProcs = lobjData.GetProcessIDs();

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
		ExecMgrXFerBase lopResult;

		lopResult = null;

		if ( Constants.ObjID_Client.equals(pidObjectType) )
			lopResult = new com.premiumminds.BigBang.Jewel.Operations.Client.ExecMgrXFer(pidProc);

//		if ( Constants.ObjID_QuoteRequest.equals(pidObjectType) )
//			lopResult = new com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExternEndMgrXFer(pidProc);
//
//		if ( Constants.ObjID_Policy.equals(pidObjectType) )
//			lopResult = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternEndMgrXFer(pidProc);
//
//		if ( Constants.ObjID_Casualty.equals(pidObjectType) )
//			lopResult = new com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternEndMgrXFer(pidProc);
//
		if ( lopResult != null )
		{
			lopResult.midNewManager = midNewManager;
			lopResult.midMassProcess = GetProcess().getKey();
		}

		return lopResult;
	}

	private Operation GetUndoTrigger(UUID pidObjectType, UUID pidProc, UUID pidOldMgr)
		throws JewelPetriException
	{
		ExternMassUndoMgrXFerBase lopResult;

		lopResult = null;

		if ( Constants.ObjID_Client.equals(pidObjectType) )
			lopResult = new com.premiumminds.BigBang.Jewel.Operations.Client.ExternMassUndoMgrXFer(pidProc);

//		if ( Constants.ObjID_QuoteRequest.equals(pidObjectType) )
//			lopResult = new com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExternUndoEndMgrXFer(pidProc);
//
//		if ( Constants.ObjID_Policy.equals(pidObjectType) )
//			lopResult = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternUndoEndMgrXFer(pidProc);
//
//		if ( Constants.ObjID_Casualty.equals(pidObjectType) )
//			lopResult = new com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternUndoEndMgrXFer(pidProc);

		if ( lopResult != null )
		{
			lopResult.midXFerProcess = GetProcess().getKey();
			lopResult.midOldManager = pidOldMgr;
			lopResult.midNewManager = midNewManager;
		}

		return lopResult;
	}
}
