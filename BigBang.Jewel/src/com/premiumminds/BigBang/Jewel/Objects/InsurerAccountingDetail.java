package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.apache.ecs.html.TD;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;

public class InsurerAccountingDetail
	extends TransactionDetailBase
{
    public static InsurerAccountingDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace,
					Constants.ObjID_InsurerAccountingDetail), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

	public TD[] buildRow()
		throws BigBangJewelException
	{
		Receipt lobjReceipt;
		TD[] larrAux;
		TD[] larrCells;
		int i;

		lobjReceipt = getReceipt();

		larrAux = super.buildRow();
		i = larrAux.length;

		larrCells = Arrays.copyOf(larrAux, i + 1);

		larrCells[i] = ReportBuilder.buildCell(lobjReceipt.getAt(Receipt.I.COMMISSIONS), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[i], true, true);

		return larrCells;
	}

	public BigDecimal getPremium()
		throws BigBangJewelException
	{
		return (BigDecimal)getReceipt().getAt(Receipt.I.TOTALPREMIUM);
	}

	public BigDecimal getDirectPremium()
		throws BigBangJewelException
	{
		ILog lobjLog;
		Payment lobjPayment;
		BigDecimal ldblResult;
		int i;

		lobjLog = getReceipt().getPaymentLog();
		try
		{
			lobjPayment = (Payment)lobjLog.GetOperationData();
		}
		catch (Throwable e)
		{
			lobjPayment = null;
		}

		ldblResult = new BigDecimal(0);

		if ( lobjPayment != null )
		{
			for ( i = 0; i < lobjPayment.marrData.length; i++ )
				if ( Constants.PayID_DirectToInsurer.equals(lobjPayment.marrData[i].midPaymentType) ||
						Constants.PayID_FromTheInsurer.equals(lobjPayment.marrData[i].midPaymentType) )
					ldblResult = ldblResult.add(lobjPayment.marrData[i].mdblValue);
		}

		return ldblResult;
	}

	public BigDecimal getCommissions()
		throws BigBangJewelException
	{
		return (BigDecimal)getReceipt().getAt(Receipt.I.COMMISSIONS);
	}

	public BigDecimal getLifeComms()
		throws BigBangJewelException
	{
		Category lobjCat;

		lobjCat = getReceipt().getAbsolutePolicy().GetSubLine().getLine().getCategory();

		if ( "Vida".equals(lobjCat.getLabel()) || "Fundo de Pensões".equals(lobjCat.getLabel()) )
			return getCommissions();

		return new BigDecimal(0.0);
	}
}
