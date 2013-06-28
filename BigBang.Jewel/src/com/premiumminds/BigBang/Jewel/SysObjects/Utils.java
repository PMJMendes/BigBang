package com.premiumminds.BigBang.Jewel.SysObjects;

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
}
