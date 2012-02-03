package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

public class ForceValidatePolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midPolicy;

	public ForceValidatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ForceValidatePolicy;
	}

	public String ShortDesc()
	{
		return "Validação em Migração";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Esta apólice foi migrada do Gescar e a sua validação foi forçada.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;

		midPolicy = GetProcess().GetDataKey();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
			lobjPolicy.setAt(13, Constants.StatusID_Valid);
			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada e passará a ser possível fazer alterações em modo de trabalho.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A validação forçada foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;

		midPolicy = GetProcess().GetDataKey();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
			lobjPolicy.setAt(13, Constants.StatusID_InProgress);
			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Policy;
		lobjSet.marrChanged = new UUID[] {midPolicy};
		lobjSet.marrCreated = new UUID[] {};
		lobjSet.marrDeleted = new UUID[] {};

		return new UndoSet[] {lobjSet};
	}
}
