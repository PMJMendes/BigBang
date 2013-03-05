package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.DebitNote;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class AssociateWithDebitNote
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midDebitNote;
	private UUID midReceipt;
	private String mstrDebitNote;
	private UUID midPrevStatus;

	public AssociateWithDebitNote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_AssociateWithDebitNote;
	}

	public String ShortDesc()
	{
		return "Cobrança via Nota de Débito";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi considerado pago por associação com a Nota de Débito n. " + mstrDebitNote +
				", paga aquando da criação da apólice.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		DebitNote lobjNote;
		Receipt lobjReceipt;

		midReceipt = GetProcess().GetDataKey();

		try
		{
			lobjNote = DebitNote.GetInstance(Engine.getCurrentNameSpace(), midDebitNote);
			lobjNote.setAt(5, new Timestamp(new java.util.Date().getTime()));
			lobjNote.setAt(6, midReceipt);
			lobjNote.SaveToDb(pdb);

			lobjReceipt = (Receipt)GetProcess().GetData();
			midPrevStatus = (UUID)lobjReceipt.getAt(Receipt.I.STATUS);
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Paid);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A cobrança será retirada. A nota de débito será novamente disponibilizada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A cobrança foi retirada. A nota de débito foi disponibilizada para associação com outro recibo.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		DebitNote lobjNote;
		Receipt lobjReceipt;

		try
		{
			lobjNote = DebitNote.GetInstance(Engine.getCurrentNameSpace(), midDebitNote);
			mstrDebitNote = lobjNote.getLabel();
			lobjNote.setAt(5, null);
			lobjNote.setAt(6, null);
			lobjNote.SaveToDb(pdb);

			lobjReceipt = (Receipt)GetProcess().GetData();
			lobjReceipt.setAt(Receipt.I.STATUS, midPrevStatus);
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
