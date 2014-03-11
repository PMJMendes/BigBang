package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class TriggerAutoValidate
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;
	private UUID midPrevManager;
	private boolean mbShort;

	public TriggerAutoValidate(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TriggerAutoValidate;
	}

	public String ShortDesc()
	{
		return "Validação Automática";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi validado automaticamente pelo sistema.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		Receipt lobjRec;
		UUID lidProfile;

		lobjProc = GetProcess();
		lobjRec = (Receipt)lobjProc.GetData();
		try
		{
			lidProfile = lobjRec.getProfile();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midReceipt = lobjProc.GetDataKey();
		midPrevManager = lobjProc.GetManagerID();

		lobjProc.SetManagerID(Engine.getCurrentUser(), pdb);

		if ( Constants.ProfID_Simple.equals(lidProfile) && !lobjRec.isReverseCircuit() )
		{
			mbShort = true;
			TriggerOp(new TriggerForceShortCircuit(lobjProc.getKey()), pdb);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A validação foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;

		lobjProc = GetProcess();
		lobjProc.SetManagerID(midPrevManager, pdb);

		if ( mbShort )
			TriggerOp(new TriggerUndoShortCircuit(lobjProc.getKey()), pdb);
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
