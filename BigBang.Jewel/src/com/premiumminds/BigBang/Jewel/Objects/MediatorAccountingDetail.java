package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.apache.ecs.html.TD;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;

public class MediatorAccountingDetail
	extends TransactionDetailBase
{
    public static MediatorAccountingDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace,
					Constants.ObjID_MediatorAccountingDetail), pidKey);
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
		TD[] larrAux;
		TD[] larrCells;
		int i;

		larrAux = super.buildRow();
		i = larrAux.length;

		larrCells = Arrays.copyOf(larrAux, i + 2);

		larrCells[i] = ReportBuilder.buildCell(getCommission(), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[i], true, true);

		larrCells[i + 1] = ReportBuilder.buildCell(getRetrocession(), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[i + 1], true, true);

		return larrCells;
	}

	public BigDecimal getPremium()
		throws BigBangJewelException
	{
		return (BigDecimal)getReceipt().getAt(Receipt.I.TOTALPREMIUM);
	}

	public BigDecimal getCommission()
		throws BigBangJewelException
	{
		return (BigDecimal)getReceipt().getAt(Receipt.I.COMMISSIONS);
	}

	public BigDecimal getRetrocession()
		throws BigBangJewelException
	{
		return (BigDecimal)getReceipt().getAt(Receipt.I.RETROCESSIONS);
	}
}
