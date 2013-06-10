package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class TransferToPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public TransferToPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	public UUID midNewProcess;
	private UUID midOldProcess;
	private String mstrNew;
	private String mstrOld;
	private UUID midReceipt;

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TransferToPolicy;
	}

	public String ShortDesc()
	{
		return "Transferência de Pasta";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi transferido de " + mstrOld + " para " + mstrNew;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;
		UUID lidScript;

		midReceipt = GetProcess().GetDataKey();

		midOldProcess = GetProcess().GetParent().getKey();
		mstrOld = GetProcess().GetParent().GetScript().getLabel() + ": " + GetProcess().GetParent().GetData().getLabel();

		GetProcess().SetParentProcId(midNewProcess, pdb);

		mstrNew = GetProcess().GetParent().GetScript().getLabel() + ": " + GetProcess().GetParent().GetData().getLabel();

		lidScript = GetProcess().GetParent().GetScriptID();
		lobjReceipt = (Receipt)GetProcess().GetData();
		try
		{
			if ( Constants.ProcID_Policy.equals(lidScript) )
			{
				lobjReceipt.setAt(Receipt.I.POLICY, GetProcess().GetParent().GetDataKey());
				lobjReceipt.setAt(Receipt.I.SUBPOLICY, null);
				lobjReceipt.setAt(Receipt.I.SUBCASUALTY, null);
			}
			else if ( Constants.ProcID_SubPolicy.equals(lidScript) )
			{
				lobjReceipt.setAt(Receipt.I.SUBPOLICY, GetProcess().GetParent().GetDataKey());
				lobjReceipt.setAt(Receipt.I.POLICY, null);
				lobjReceipt.setAt(Receipt.I.SUBCASUALTY, null);
			}
			else if ( Constants.ProcID_SubCasualty.equals(lidScript) )
			{
				lobjReceipt.setAt(Receipt.I.SUBCASUALTY, GetProcess().GetParent().GetDataKey());
				lobjReceipt.setAt(Receipt.I.POLICY, null);
				lobjReceipt.setAt(Receipt.I.SUBPOLICY, null);
			} 
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O recibo será novamente reposto em " + mstrOld;
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O recibo foi reposto em " + mstrOld;
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;
		UUID lidScript;

		GetProcess().SetParentProcId(midOldProcess, pdb);

		lidScript = GetProcess().GetParent().GetScriptID();
		lobjReceipt = (Receipt)GetProcess().GetData();
		try
		{
			if ( Constants.ProcID_Policy.equals(lidScript) )
			{
				lobjReceipt.setAt(Receipt.I.POLICY, GetProcess().GetParent().GetDataKey());
				lobjReceipt.setAt(Receipt.I.SUBPOLICY, null);
				lobjReceipt.setAt(Receipt.I.SUBCASUALTY, null);
			}
			else if ( Constants.ProcID_SubPolicy.equals(lidScript) )
			{
				lobjReceipt.setAt(Receipt.I.SUBPOLICY, GetProcess().GetParent().GetDataKey());
				lobjReceipt.setAt(Receipt.I.POLICY, null);
				lobjReceipt.setAt(Receipt.I.SUBCASUALTY, null);
			}
			else if ( Constants.ProcID_SubCasualty.equals(lidScript) )
			{
				lobjReceipt.setAt(Receipt.I.SUBCASUALTY, GetProcess().GetParent().GetDataKey());
				lobjReceipt.setAt(Receipt.I.POLICY, null);
				lobjReceipt.setAt(Receipt.I.SUBPOLICY, null);
			} 
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
