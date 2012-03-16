package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class SetReturnToInsurer
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrSubject;
	public String mstrText;
	private UUID midReceipt;

	public SetReturnToInsurer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_SetReturnToInsurer;
	}

	public String ShortDesc()
	{
		return "Indicação de Devolução";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi marcado para devolução pelo seguinte motivo:" + pstrLineBreak + mstrSubject + pstrLineBreak + mstrText;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		String lstrText;
		Receipt lobjReceipt;

		midReceipt = GetProcess().GetDataKey();

		lstrText = (mstrSubject + " | " + mstrText);
		if ( lstrText.length() > 250 )
			lstrText = lstrText.substring(0, 250);

		try
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
			lobjReceipt.setAt(15, lstrText);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A indicação de devolução será retirada. O recibo ficará novamente disponível para conferência.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A indicação de devolução foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
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
