package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public abstract class MediatorBase
{
	protected Receipt mobjReceipt;

	public MediatorBase(Receipt pobjReceipt)
	{
		mobjReceipt = pobjReceipt;
	}

	public abstract BigDecimal getResult() throws BigBangJewelException;
}
