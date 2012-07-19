package com.premiumminds.BigBang.Jewel.SysObjects;

import java.util.UUID;

import org.apache.ecs.html.TD;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;

public abstract class TransactionDetailBase
	extends ObjectBase
{
	public static class I
	{
		public static int OWNER   = 0;
		public static int RECEIPT = 1;
		public static int VOIDED  = 2;
	}

	public Receipt getReceipt()
		throws BigBangJewelException
	{
		return Receipt.GetInstance(getNameSpace(), (UUID)getAt(I.RECEIPT));
	}

	public String getMeans(ILog pobjLog)
		throws BigBangJewelException
	{
		Payment lobjPayment;
		StringBuilder lstrAux;
		int i;

		try
		{
			lobjPayment = (Payment)pobjLog.GetOperationData();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjPayment == null )
			return "?";
		else
		{
			lstrAux = new StringBuilder();
			for ( i = 0; i < lobjPayment.marrData.length; i++ )
			{
				try
				{
					lstrAux.append(Engine.GetWorkInstance(getNameSpace(), lobjPayment.marrData[i].midPaymentType).getAt(1));
				}
				catch (Throwable e)
				{
					lstrAux.append("*");
				}
			}
			return lstrAux.toString();
		}
	}

	public TD[] buildRow()
		throws BigBangJewelException
	{
		Receipt lobjReceipt;
		Policy lobjPolicy;
		Category lobjCat;
		ILog lobjLog;
		TD[] larrCells;

		lobjReceipt = getReceipt();

		lobjPolicy = lobjReceipt.getDirectPolicy();
		lobjCat = (lobjPolicy == null ? null : lobjPolicy.GetSubLine().getLine().getCategory());

		lobjLog = lobjReceipt.getPaymentLog();

		larrCells = new TD[10];

		larrCells[0] = ReportBuilder.buildCell(lobjReceipt.getAt(Receipt.I.NUMBER), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(lobjReceipt.getReceiptType(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjPolicy == null ? "-" : lobjPolicy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjCat == null ? "-" : lobjCat.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjReceipt.getAt(Receipt.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(lobjReceipt.getAt(Receipt.I.ISSUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(lobjReceipt.getAt(Receipt.I.MATURITYDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(lobjReceipt.getAt(Receipt.I.DUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(lobjLog.GetTimestamp(), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(getMeans(lobjLog), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[9], true, true);

		return larrCells;
	}
}
