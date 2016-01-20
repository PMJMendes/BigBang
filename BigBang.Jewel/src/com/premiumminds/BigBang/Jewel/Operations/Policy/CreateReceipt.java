package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
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
		return Constants.OPID_Policy_CreateReceipt;
	}

	public Timestamp DateCheck()
		throws BigBangJewelException
	{
		try
		{
			return (Timestamp)GetProcess().GetData().getAt(9);
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
			return (UUID)GetProcess().GetData().getAt(11);
		}
		catch (JewelPetriException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void LinkData()
		throws BigBangJewelException
	{
		mobjData.midSubPolicy = null;
		mobjData.midSubCasualty = null;

		try
		{
			mobjData.midPolicy = GetProcess().GetDataKey();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	/**
	 * This method calls the CreateReceiptBase's method "Run", and afterwards checks
	 * if the created receipt is a continuing receipt, in which case it updates the policy's
	 * total and sales's premiums.
	 * 
	 * @whotoblame jcamilo
	 */
	protected void Run(SQLServer pdb)
			throws JewelPetriException {
		
		super.Run(pdb);
		
		try {
			ReceiptData receiptData = this.mobjData;
			if ( receiptData.midType.equals(Constants.RecType_Continuing) ) {
				Policy policy = Policy.GetInstance(Engine.getCurrentNameSpace(), mobjData.midPolicy);
				policy.SetSalesPremium(pdb, receiptData.mdblCommercial);
				policy.SetTotalPremium(pdb, receiptData.mdblTotal);
			}
		} catch (Throwable e) {
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
