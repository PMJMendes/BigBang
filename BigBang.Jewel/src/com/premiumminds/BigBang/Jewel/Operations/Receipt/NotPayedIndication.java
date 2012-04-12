package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

public class NotPayedIndication
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;
	private UUID midPolicy;
	private UUID midSubPolicy;
	private UUID midPrevStatus;
	private Timestamp mdtPrevEndDate;

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
		Operation lop;
		Receipt lobjReceipt;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternAutoVoid lopEAV;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternAutoVoid lopsEAV;

		lobjReceipt = (Receipt)GetProcess().GetData();
		midReceipt = lobjReceipt.getKey();

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			lobjPolicy = (Policy)GetProcess().GetParent().GetData();
			midPolicy = lobjPolicy.getKey();
			midPrevStatus = (UUID)lobjPolicy.getAt(13);
			mdtPrevEndDate = (Timestamp)lobjPolicy.getAt(9);

			lopEAV = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternAutoVoid(GetProcess().GetParent().getKey());
			lopEAV.mdtEffectDate = (Timestamp)lobjReceipt.getAt(10);
			lopEAV.midReceiptProc = GetProcess().getKey();
			lop = lopEAV;

			midSubPolicy = null;
		}
		else
		{
			lobjSubPolicy = (SubPolicy)GetProcess().GetParent().GetData();
			midSubPolicy = lobjSubPolicy.getKey();
			midPrevStatus = (UUID)lobjSubPolicy.getAt(7);
			mdtPrevEndDate = (Timestamp)lobjSubPolicy.getAt(4);

			lopsEAV = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternAutoVoid(GetProcess().GetParent().getKey());
			lopsEAV.mdtEffectDate = (Timestamp)lobjReceipt.getAt(10);
			lopsEAV.midReceiptProc = GetProcess().getKey();
			lop = lopsEAV;

			midPolicy = null;
		}

		TriggerOp(lop, pdb);
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
		Operation lop;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternUndoAutoVoid lopEUAV;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternUndoAutoVoid lopsEUAV;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			lopEUAV = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternUndoAutoVoid(GetProcess().GetParent().getKey());
			lopEUAV.mdtPrevEndDate = mdtPrevEndDate;
			lopEUAV.midPrevStatus = midPrevStatus;
			lopEUAV.midReceiptProc = GetProcess().getKey();
			lop = lopEUAV;
		}
		else
		{
			lopsEUAV = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternUndoAutoVoid(GetProcess().GetParent().getKey());
			lopsEUAV.mdtPrevEndDate = mdtPrevEndDate;
			lopsEUAV.midPrevStatus = midPrevStatus;
			lopsEUAV.midReceiptProc = GetProcess().getKey();
			lop = lopsEUAV;
		}

		TriggerOp(lop, pdb);
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
