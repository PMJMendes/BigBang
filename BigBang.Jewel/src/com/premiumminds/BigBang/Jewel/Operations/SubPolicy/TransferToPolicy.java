package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

public class TransferToPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midNewProcess;
	private UUID midOldProcess;
	private String mstrNew;
	private String mstrOld;
	private UUID midSubPolicy;

	public TransferToPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_TransferToPolicy;
	}

	public String ShortDesc()
	{
		return "Transferência de Apólice-Mãe";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi transferida da apólice " + mstrOld + " para a apólice " + mstrNew;
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

		midSubPolicy = GetProcess().GetDataKey();

		try
		{
			((SubPolicy)GetProcess().GetData()).ResetOwner();
		}
		catch (BigBangJewelException e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A apólice adesão será novamente reposta na apólice " + mstrOld;
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi reposta na apólice " + mstrOld;
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().SetParentProcId(midOldProcess, pdb);

		try
		{
			((SubPolicy)GetProcess().GetData()).ResetOwner();
		}
		catch (BigBangJewelException e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_SubPolicy;
		lobjSet.marrChanged = new UUID[]{midSubPolicy};

		return new UndoSet[]{lobjSet};
	}
}
