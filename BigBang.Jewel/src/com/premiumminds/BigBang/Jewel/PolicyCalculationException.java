package com.premiumminds.BigBang.Jewel;

import Jewel.Petri.SysObjects.JewelPetriException;

public class PolicyCalculationException
	extends JewelPetriException
{
	private static final long serialVersionUID = 1L;
	
	public PolicyCalculationException()
	{
	}
	
	public PolicyCalculationException(String pstrMessage)
	{
		super(pstrMessage);
	}
	
	public PolicyCalculationException(Throwable e)
	{
		super(e);
	}
	
	public PolicyCalculationException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
