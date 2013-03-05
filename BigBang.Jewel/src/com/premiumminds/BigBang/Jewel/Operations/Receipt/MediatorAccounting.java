package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingDetail;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.MediatorAccountingSet;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class MediatorAccounting
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midSet;
	public UUID midMap;
	public UUID midDetail;
	private UUID midMediator;
	private UUID midPrevStatus;
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
		Receipt lobjReceipt;
		Mediator lobjMediator;
		MediatorAccountingSet lobjSet;
		MediatorAccountingMap lobjMap;
		MediatorAccountingDetail lobjDetail;
		IProcess lobjProc;

		lobjReceipt = (Receipt)GetProcess().GetData();
		try
		{
			lobjMediator = lobjReceipt.getMediator();
			midMediator = lobjMediator.getKey();

			if ( midSet == null )
			{
				lobjSet = MediatorAccountingSet.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSet.setAt(0, new Timestamp(new java.util.Date().getTime()));
				lobjSet.setAt(1, Engine.getCurrentUser());
				lobjSet.setAt(2, lobjSet.GetNewSetNumber(pdb));
				lobjSet.SaveToDb(pdb);
				midSet = lobjSet.getKey();
			}

			if ( midMap == null )
			{
				lobjMap = MediatorAccountingMap.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjMap.setAt(0, midSet);
				lobjMap.setAt(1, midMediator);
				lobjMap.setAt(2, (Timestamp)null);
				lobjMap.SaveToDb(pdb);
				midMap = lobjMap.getKey();
			}

			lobjDetail = MediatorAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), null);
			lobjDetail.setAt(0, midMap);
			lobjDetail.setAt(1, GetProcess().GetDataKey());
			lobjDetail.setAt(2, false);
			lobjDetail.SaveToDb(pdb);
			midDetail = lobjDetail.getKey();

			if ( lobjReceipt.doCalcRetrocession() )
				lobjReceipt.SaveToDb(pdb);

			lobjProc = GetProcess();

			if ( Jewel.Petri.Constants.LevelID_Invalid.equals(lobjProc.GetOperation(Constants.OPID_Receipt_InsurerAccounting,
							pdb).GetLevel()) &&
					Jewel.Petri.Constants.LevelID_Invalid.equals(lobjProc.GetOperation(Constants.OPID_Receipt_SendReceipt,
							pdb).GetLevel()) &&
					Jewel.Petri.Constants.LevelID_Invalid.equals(lobjProc.GetOperation(Constants.OPID_Receipt_SendPayment,
							pdb).GetLevel()) )
			{
				midPrevStatus = (UUID)lobjReceipt.getAt(Receipt.I.STATUS);
				lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Final);
				lobjReceipt.SaveToDb(pdb);
			}
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
		MediatorAccountingMap lobjSetMap;
		MediatorAccountingDetail lobjSetReceipt;
		Receipt lobjReceipt;

		try
		{
			lobjSetMap = MediatorAccountingMap.GetInstance(Engine.getCurrentNameSpace(), midMap);
			if ( lobjSetMap.isSettled() )
				throw new BigBangJewelException("Não pode reverter a prestação de contas depois de saldar a transacção.");

			lobjSetReceipt = MediatorAccountingDetail.GetInstance(Engine.getCurrentNameSpace(), midDetail);
			lobjSetReceipt.setAt(2, true);
			lobjSetReceipt.SaveToDb(pdb);

			lobjSetMap.clearDetails();

			if ( midPrevStatus != null )
			{
				lobjReceipt = (Receipt)GetProcess().GetData();
				lobjReceipt.setAt(Receipt.I.STATUS, midPrevStatus);
				lobjReceipt.SaveToDb(pdb);
			}
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
