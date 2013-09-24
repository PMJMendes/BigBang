package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.NotPayedIndication;

public class ReactivateSubPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private Timestamp mdtEffectDate;

	public ReactivateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_ReactivateSubPolicy;
	}

	public String ShortDesc()
	{
		return "Reactivação de Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice-adesão foi manualmente reposta em vigor após ter sido anulada automaticamente por falta de pagamento de um recibo.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		SubPolicy lobjSubPol;
		NotPayedIndication lopNPI;

		try
		{
			lobjSubPol = (SubPolicy)GetProcess().GetData();
			mdtEffectDate = (Timestamp)lobjSubPol.getAt(4);

			lopNPI = (NotPayedIndication)PNProcess.GetInstance(Engine.getCurrentNameSpace(),
					GetProcess().GetLiveLog(Constants.OPID_SubPolicy_ExternAutoVoid, pdb).GetOperationData().GetExternalProcess())
					.GetLiveLog(Constants.OPID_Receipt_NotPayedIndication, pdb).GetOperationData();
			lobjSubPol.setAt(7, lopNPI.midPrevStatus);
			lobjSubPol.setAt(4, lopNPI.mdtPrevEndDate);
			lobjSubPol.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A reactivação manual da apólice-adesão será retirada, e esta será novamente considerada anulada por falta de pagamento de um recibo.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A reactivação manual da apólice-adesão foi retirada, considerando-se esta novamente anulada por falta de pagamento de um recibo.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		SubPolicy lobjSubPol;

		try
		{
			lobjSubPol = (SubPolicy)GetProcess().GetData();
			lobjSubPol.setAt(7, Constants.StatusID_AutoVoided);
			lobjSubPol.setAt(4, mdtEffectDate);
			lobjSubPol.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
