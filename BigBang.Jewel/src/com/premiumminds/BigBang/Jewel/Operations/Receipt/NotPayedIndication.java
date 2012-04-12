package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class NotPayedIndication
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;
	private UUID midPolicy;
	private UUID midSubPolicy;

	public NotPayedIndication(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_NotPayedIndication;
	}

	public String ShortDesc()
	{
		return "Indicação de Não Pagamento";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Foi dada a indicação que o recibo não foi nem será pago." + pstrLineBreak +
				"A apólice correspondente foi automaticamente anulada.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		midReceipt = GetProcess().GetDataKey();

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			midPolicy = GetProcess().GetParent().GetDataKey();
			midSubPolicy = null;
		}
		else
		{
			midSubPolicy = GetProcess().GetParent().GetDataKey();
			midPolicy = null;
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A indicação de não pagamento será retirada e a apólice será novamente posta em vigor.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A indicação de não pagamento foi retirada e a apólice foi novamente posta em vigor.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;

		larrResult = new UndoSet[2];
		larrResult[0] = new UndoSet();
		larrResult[0].midType = Constants.ObjID_Receipt;
		larrResult[0].marrChanged = new UUID[] {midReceipt};
		larrResult[0].marrCreated = new UUID[0];
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[1] = new UndoSet();
		if ( midPolicy != null )
		{
			larrResult[1].midType = Constants.ObjID_Policy;
			larrResult[1].marrChanged = new UUID[] {midPolicy};
		}
		else
		{
			larrResult[1].midType = Constants.ObjID_SubPolicy;
			larrResult[1].marrChanged = new UUID[] {midSubPolicy};
		}
		larrResult[1].marrCreated = new UUID[0];
		larrResult[1].marrDeleted = new UUID[0];

		return larrResult;
	}
}
