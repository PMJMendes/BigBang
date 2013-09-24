package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
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
	public UUID midPrevStatus;
	public Timestamp mdtPrevEndDate;
	private UUID midRefProc;

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
		Receipt lobjReceipt;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternAutoVoid lopEAV;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternAutoVoid lopsEAV;

		lobjReceipt = (Receipt)GetProcess().GetData();
		midReceipt = lobjReceipt.getKey();

		midPolicy = null;
		midSubPolicy = null;

		if ( !lobjReceipt.isReverseCircuit() && !lobjReceipt.isForCasualties() )
		{
			try
			{
				lobjPolicy = lobjReceipt.getDirectPolicy();
				lobjSubPolicy = lobjReceipt.getSubPolicy();
			}
			catch (BigBangJewelException e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}

			if ( lobjPolicy != null )
			{
				midPolicy = lobjPolicy.getKey();
				midRefProc = lobjPolicy.GetProcessID();
				midPrevStatus = (UUID)lobjPolicy.getAt(13);
				mdtPrevEndDate = (Timestamp)lobjPolicy.getAt(9);

				lopEAV = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternAutoVoid(midRefProc);
				lopEAV.mdtEffectDate = (Timestamp)lobjReceipt.getAt(9);
				lopEAV.midReceiptProc = GetProcess().getKey();
				TriggerOp(lopEAV, pdb);
			}
			else if ( lobjSubPolicy != null )
			{
				midSubPolicy = lobjSubPolicy.getKey();
				midRefProc = lobjSubPolicy.GetProcessID();
				midPrevStatus = (UUID)lobjSubPolicy.getAt(7);
				mdtPrevEndDate = (Timestamp)lobjSubPolicy.getAt(4);

				lopsEAV = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternAutoVoid(midRefProc);
				lopsEAV.mdtEffectDate = (Timestamp)lobjReceipt.getAt(9);
				lopsEAV.midReceiptProc = GetProcess().getKey();
				TriggerOp(lopsEAV, pdb);
			}
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
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternUndoAutoVoid lopEUAV;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternUndoAutoVoid lopsEUAV;

		if ( midPolicy != null )
		{
			lopEUAV = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternUndoAutoVoid(midRefProc);
			lopEUAV.mdtPrevEndDate = mdtPrevEndDate;
			lopEUAV.midPrevStatus = midPrevStatus;
			lopEUAV.midReceiptProc = GetProcess().getKey();
			TriggerOp(lopEUAV, pdb);
		}
		else if ( midSubPolicy != null)
		{
			lopsEUAV = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternUndoAutoVoid(midRefProc);
			lopsEUAV.mdtPrevEndDate = mdtPrevEndDate;
			lopsEUAV.midPrevStatus = midPrevStatus;
			lopsEUAV.midReceiptProc = GetProcess().getKey();
			TriggerOp(lopsEUAV, pdb);
		}
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
