package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Petri.SysObjects.JewelPetriException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateReceiptBase;

public class CreateReceipt
	extends CreateReceiptBase
{
	private static final long serialVersionUID = 1L;

	public CreateReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_CreateReceipt;
	}

	public Timestamp DateCheck()
		throws BigBangJewelException
	{
		return null;
	}

	public UUID GetMediatorID()
		throws BigBangJewelException
	{
		try
		{
			return ((SubCasualty)GetProcess().GetData()).getAbsolutePolicy().getMediator().getKey();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void LinkData()
		throws BigBangJewelException
	{
		mobjData.midPolicy = null;
		mobjData.midSubPolicy = null;

		try
		{
			mobjData.midSubCasualty = GetProcess().GetDataKey();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
