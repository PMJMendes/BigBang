package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class VoidInternal
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midMotive;
	private UUID midReceipt;
	private String mstrMotive;

	public VoidInternal(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_VoidInternal;
	}

	public String ShortDesc()
	{
		return "Anulação de Nota de Débito";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A nota de débito foi anuldada pelo seguinte motivo:" + pstrLineBreak + mstrMotive;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
		IProcess lobjProc;
		ObjectBase lobjMotive;
		Receipt lobjReceipt;

		lobjProc = GetProcess();
		midReceipt = lobjProc.GetDataKey();

		try
		{
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReceiptReturnMotives),
					midMotive);

			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
			lobjReceipt.setAt(15, lobjMotive.getLabel());
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A indicação de anulação será retirada. A nota de débito será colocada novamente à cobrança.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A indicação de anulação foi retirada.";
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		Receipt lobjReceipt;

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
			lobjReceipt.setAt(15, null);
			lobjReceipt.SaveToDb(pdb);
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
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = new UUID[]{midReceipt};

		return new UndoSet[]{lobjSet};
	}

}
