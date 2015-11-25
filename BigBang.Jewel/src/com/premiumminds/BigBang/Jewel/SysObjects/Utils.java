package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class Utils
{
	public static UUID getCurrentAgent()
		throws BigBangJewelException
	{
		try
		{
			return (UUID)User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser()).getAt(4);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static String getCurrency()
	{
		return (String)Engine.getUserData().get("CURRENCY");
	}

	public static BigDecimal getCommissionsTax()
	{
		try
		{
			return new BigDecimal((String)Engine.getUserData().get("TAX"));
		}
		catch (Throwable e) {
			return new BigDecimal("2");
		}
	}
}
