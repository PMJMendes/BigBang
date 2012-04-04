package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

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
		InsurerAccountingMap lobjSetClient;
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
				lobjSet.SaveToDb(pdb);
				midSet = lobjSet.getKey();
			}

			if ( midMap == null )
			{
				lobjSetClient = InsurerAccountingMap.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSetClient.setAt(0, midSet);
				lobjSetClient.setAt(1, midInsurer);
				lobjSetClient.setAt(2, (Timestamp)null);
				lobjSetClient.SaveToDb(pdb);
				midMap = lobjSetClient.getKey();
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
		InsurerAccountingDetail lobjSetReceipt;

		try
		{
			lobjSetReceipt = InsurerAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), midDetail);
			lobjSetReceipt.setAt(2, true);
			lobjSetReceipt.SaveToDb(pdb);
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
