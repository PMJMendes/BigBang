package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingDetail;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingSet;

public class InsurerAccounting
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midSet;
	public UUID midMap;
	public UUID midDetail;
	public String mstrExtraText;
	public BigDecimal mdblExtraValue;
	public Boolean mbIsCommissions;
	public Boolean mbHasTax;
	private UUID midInsurer;
//	private OutgoingMessageData mobjMessage;

	public InsurerAccounting(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_InsurerAccounting;
	}

	public String ShortDesc()
	{
		return "Prestação de Contas";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi feita a prestação de contas deste recibo.");

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		InsurerAccountingSet lobjSet;
		InsurerAccountingMap lobjMap;
		InsurerAccountingDetail lobjSetReceipt;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midInsurer = (UUID)GetProcess().GetParent().GetData().getAt(2);
		else
			midInsurer = (UUID)GetProcess().GetParent().GetParent().GetData().getAt(2);

		try
		{
			if ( midSet == null )
			{
				lobjSet = InsurerAccountingSet.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSet.setAt(0, new Timestamp(new java.util.Date().getTime()));
				lobjSet.setAt(1, Engine.getCurrentUser());
				lobjSet.setAt(2, lobjSet.GetNewSetNumber(pdb));
				lobjSet.SaveToDb(pdb);
				midSet = lobjSet.getKey();
			}

			if ( midMap == null )
			{
				lobjMap = InsurerAccountingMap.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjMap.setAt(InsurerAccountingMap.I.SET, midSet);
				lobjMap.setAt(InsurerAccountingMap.I.OWNER, midInsurer);
				lobjMap.setAt(InsurerAccountingMap.I.SETTLEDON, (Timestamp)null);
				lobjMap.setAt(InsurerAccountingMap.I.EXTRATEXT, mstrExtraText);
				lobjMap.setAt(InsurerAccountingMap.I.EXTRAVALUE, mdblExtraValue);
				lobjMap.setAt(InsurerAccountingMap.I.ISCOMMISSION, mbIsCommissions);
				lobjMap.setAt(InsurerAccountingMap.I.HASTAX, mbHasTax);
				lobjMap.SaveToDb(pdb);
				midMap = lobjMap.getKey();
			}

			lobjSetReceipt = InsurerAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), null);
			lobjSetReceipt.setAt(0, midMap);
			lobjSetReceipt.setAt(1, GetProcess().GetDataKey());
			lobjSetReceipt.setAt(2, false);
			lobjSetReceipt.SaveToDb(pdb);
			midDetail = lobjSetReceipt.getKey();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A prestação de contas deste recibo será desconsiderada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A prestação de contas deste recibo foi desconsiderada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		InsurerAccountingMap lobjSetMap;
		InsurerAccountingDetail lobjSetReceipt;

		try
		{
			lobjSetMap = InsurerAccountingMap.GetInstance(Engine.getCurrentNameSpace(), midMap);
			if ( lobjSetMap.isSettled() )
				throw new BigBangJewelException("Não pode reverter a prestação de contas depois de saldar a transacção.");

			lobjSetReceipt = InsurerAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), midDetail);
			lobjSetReceipt.setAt(2, true);
			lobjSetReceipt.SaveToDb(pdb);

			lobjSetMap.clearDetails();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		return null;
	}
}
