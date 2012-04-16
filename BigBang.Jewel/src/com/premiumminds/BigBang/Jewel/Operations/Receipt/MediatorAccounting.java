package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingDetail;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingSet;

public class MediatorAccounting
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midSet;
	public UUID midMap;
	public UUID midDetail;
	private UUID midMediator;
//	private OutgoingMessageData mobjMessage;

	public MediatorAccounting(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_MediatorAccounting;
	}

	public String ShortDesc()
	{
		return "Retrocessão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi feita a retrocessão de comissões deste recibo.");

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		MediatorAccountingSet lobjSet;
		MediatorAccountingMap lobjSetClient;
		MediatorAccountingDetail lobjSetReceipt;

		midMediator = (UUID)GetProcess().GetData().getAt(12);
		if ( midMediator == null )
		{
			if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			{
				midMediator = (UUID)GetProcess().GetParent().GetData().getAt(11);
				if ( midMediator == null )
					midMediator = (UUID)GetProcess().GetParent().GetParent().GetData().getAt(8);
			}
			else
			{
				midMediator = (UUID)GetProcess().GetParent().GetParent().GetData().getAt(11);
				if ( midMediator == null )
					midMediator = (UUID)GetProcess().GetParent().GetParent().GetParent().GetData().getAt(8);
			}
		}

		try
		{
			if ( midSet == null )
			{
				lobjSet = MediatorAccountingSet.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSet.setAt(0, new Timestamp(new java.util.Date().getTime()));
				lobjSet.setAt(1, Engine.getCurrentUser());
				lobjSet.SaveToDb(pdb);
				midSet = lobjSet.getKey();
			}

			if ( midMap == null )
			{
				lobjSetClient = MediatorAccountingMap.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetClient.setAt(0, midSet);
				lobjSetClient.setAt(1, midMediator);
				lobjSetClient.setAt(2, (Timestamp)null);
				lobjSetClient.SaveToDb(pdb);
				midMap = lobjSetClient.getKey();
			}

			lobjSetReceipt = MediatorAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), null);
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
		return "A retrocessão de comissões deste recibo será desconsiderada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A retrocessão de comissões deste recibo foi desconsiderada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		MediatorAccountingDetail lobjSetReceipt;

		try
		{
			lobjSetReceipt = MediatorAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), midDetail);
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
