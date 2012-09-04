package com.premiumminds.BigBang.Jewel;

public class BigBangJewelException
	extends Exception
{
	private static final long serialVersionUID = 1L;

	public BigBangJewelException()
	{
	}
	
	public BigBangJewelException(String pstrMessage)
	{
		super(pstrMessage);
	}

	public BigBangJewelException(Throwable e)
	{
		super(e);
	}

	public BigBangJewelException(String pstrMessage, Throwable e)
	{
		super(pstrMessage, e);
	}
}
