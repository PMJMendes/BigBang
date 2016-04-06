package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateReceiptBase;

public class CreateReceipt
	extends CreateReceiptBase
{
	private static final long serialVersionUID = 1L;

	public boolean premiumChanged;

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
	 */
	protected void Run(SQLServer pdb) throws JewelPetriException {
		super.Run(pdb);

		premiumChanged = false;

		if ( mobjData.midType.equals(Constants.RecType_Continuing) ) {
			Policy policy = (Policy)GetProcess().GetData();
			BigDecimal newPremium = policy.CheckSalesPremium(mobjData.mdblCommercial);
			BigDecimal newTotalPr = policy.CheckTotalPremium(mobjData.mdblTotal);

			if ((newPremium != null) || (newTotalPr != null)) {
				try {
					policy.setAt(Policy.I.PREMIUM, newPremium);
					policy.setAt(Policy.I.TOTALPREMIUM, newTotalPr);
					policy.SaveToDb(pdb);
				} catch (Throwable e) {
					throw new JewelPetriException(e.getMessage(), e);
				}
				premiumChanged = true;
			}
		}
	}

	public String LongDesc(String pstrLineBreak) {
		String str = super.LongDesc(pstrLineBreak);

		if (premiumChanged) {
			str = str + "Foram actualizados os prémios total e comercial da apólice." + pstrLineBreak;
		}

		return str;
	}
}
