package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class Payment
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public Payment(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_Payment;
	}

	@Override
	public String ShortDesc()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String LongDesc(String pstrLineBreak)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	@Override
	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		// TODO Auto-generated method stub
		
	}

}
