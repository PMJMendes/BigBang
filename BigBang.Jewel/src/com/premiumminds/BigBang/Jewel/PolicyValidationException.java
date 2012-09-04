package com.premiumminds.BigBang.Jewel;

import Jewel.Petri.SysObjects.JewelPetriException;

public class PolicyValidationException
	extends JewelPetriException
{
	private static final long serialVersionUID = 1L;

	public PolicyValidationException()
	{
	}
	
	public PolicyValidationException(String pstrMessage)
	{
		super(pstrMessage);
	}

	public PolicyValidationException(Throwable e)
	{
		super(e);
	}

	public PolicyValidationException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
