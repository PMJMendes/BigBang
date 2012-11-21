package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;

public class TransferToClient
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midNewClient;
	private UUID midOldClient;
	private UUID midNewProcess;
	private UUID midOldProcess;
	private String mstrNew;
	private String mstrOld;
	private UUID midPolicy;

	public TransferToClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_TransferToClient;
	}

	public String ShortDesc()
	{
		return "Transferência de Cliente";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice foi transferida do cliente " + mstrOld + " para o cliente " + mstrNew;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Client lobjOld, lobjNew;

		try
		{
			GetProcess().GetData().setAt(17, midNewClient);
			GetProcess().GetData().SaveToDb(pdb);

			lobjNew = Client.GetInstance(Engine.getCurrentNameSpace(), midNewClient);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
		midNewProcess = lobjNew.GetProcessID();
		mstrNew = ((Integer)lobjNew.getAt(1)).toString() + " - " + lobjNew.getLabel();

		midOldProcess = GetProcess().GetParent().getKey();
		lobjOld = (Client)GetProcess().GetParent().GetData();
		midOldClient = lobjOld.getKey();
		mstrOld = ((Integer)lobjOld.getAt(1)).toString() + " - " + lobjOld.getLabel();

		GetProcess().SetParentProcId(midNewProcess, pdb);

		midPolicy = GetProcess().GetDataKey();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A apólice será novamente reposta no cliente " + mstrOld;
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A apólice foi reposta no cliente " + mstrOld;
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		try
		{
			GetProcess().GetData().setAt(17, midOldClient);
		}
		catch (JewelEngineException e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
		GetProcess().SetParentProcId(midOldProcess, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Policy;
		lobjSet.marrChanged = new UUID[]{midPolicy};

		return new UndoSet[]{lobjSet}; 
	}
}
