package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.ILog;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.NotPayedIndication;

public class ReactivatePolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private Timestamp mdtEffectDate;

	public ReactivatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ReactivatePolicy;
	}

	public String ShortDesc()
	{
		return "Reactivação de Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice foi manualmente reposta em vigor após ter sido anulada automaticamente por falta de pagamento de um recibo.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;
		NotPayedIndication lopNPI;
		ILog lrefAutoVoid;
		ILog lrefNPI;

		try
		{
			lobjPolicy = (Policy)GetProcess().GetData();
			mdtEffectDate = (Timestamp)lobjPolicy.getAt(9);

			lopNPI = null;
			lrefAutoVoid = GetProcess().GetLiveLog(Constants.OPID_Policy_ExternAutoVoid, pdb);
			if (lrefAutoVoid != null)
			{
				lrefNPI = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lrefAutoVoid.GetOperationData().GetExternalProcess())
						.GetLiveLog(Constants.OPID_Receipt_NotPayedIndication, pdb);
				if (lrefNPI != null)
				{
					lopNPI = (NotPayedIndication)lrefNPI.GetOperationData();
					lobjPolicy.setAt(13, lopNPI.midPrevStatus);
					lobjPolicy.setAt(9, lopNPI.mdtPrevEndDate);
					lobjPolicy.SaveToDb(pdb);
				}
			}

			if (lopNPI == null)
			{
				lobjPolicy.setAt(13, Constants.StatusID_Valid);
				lobjPolicy.setAt(9, null);
			}

			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A reactivação manual da apólice será retirada, e esta será novamente considerada anulada por falta de pagamento de um recibo.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A reactivação manual da apólice foi retirada, considerando-se esta novamente anulada por falta de pagamento de um recibo.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;

		try
		{
			lobjPolicy = (Policy)GetProcess().GetData();
			lobjPolicy.setAt(13, Constants.StatusID_AutoVoided);
			lobjPolicy.setAt(9, mdtEffectDate);
			lobjPolicy.SaveToDb(pdb);
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
