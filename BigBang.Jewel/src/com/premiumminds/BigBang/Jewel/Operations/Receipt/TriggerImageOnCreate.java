package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class TriggerImageOnCreate
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;

	public TriggerImageOnCreate(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TriggerImageOnCreate;
	}

	public String ShortDesc()
	{
		return "Associação a Imagem";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi automaticamente associado ao documento da imagem digitalizada aquando da introdução.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		midReceipt = GetProcess().GetDataKey();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A associação será retirada. A imagem digitalizada será disponibilizada para outro recibo.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A imagem automaticamente associada aquando da introdução do recibo foi retirada e disponibilizada novamente."; 
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = new UUID[] {midReceipt};

		return new UndoSet[] {lobjSet}; 
	}
}
