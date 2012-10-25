package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Petri.SysObjects.JewelPetriException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateReceiptBase;

public class CreateReceipt
	extends CreateReceiptBase
{
	private static final long serialVersionUID = 1L;

	public ReceiptData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_CreateReceipt;
	}

	public Timestamp DateCheck()
		throws BigBangJewelException
	{
		try
		{
			return (Timestamp)GetProcess().GetData().getAt(4);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public UUID GetMediatorID()
		throws BigBangJewelException
	{
		try
		{
			return (UUID)GetProcess().GetParent().GetData().getAt(11);
		}
		catch (JewelPetriException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
