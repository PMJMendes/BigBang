package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

public class TransferToPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public TransferToPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	public UUID midNewProcess;
	private UUID midOldProcess;
	private String mstrNew;
	private String mstrOld;
	private UUID midReceipt;

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TransferToPolicy;
	}

	public String ShortDesc()
	{
		return "Transferência de Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi transferido da apólice " + mstrOld + " para a apólice " + mstrNew;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjOld, lobjNew;

		midOldProcess = GetProcess().getKey();
		lobjOld = (Policy)GetProcess().GetParent().GetData();
		mstrOld = lobjOld.getLabel();

		GetProcess().SetParentProcId(midNewProcess, pdb);

		lobjNew = (Policy)GetProcess().GetParent().GetData();
		mstrNew = lobjNew.getLabel();

		midReceipt = GetProcess().GetDataKey();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O recibo será novamente reposto na apólice " + mstrOld;
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O recibo foi reposto na apólice " + mstrOld;
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().SetParentProcId(midOldProcess, pdb);
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
