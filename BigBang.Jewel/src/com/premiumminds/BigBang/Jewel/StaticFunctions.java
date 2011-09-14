package com.premiumminds.BigBang.Jewel;

import java.util.UUID;

import Jewel.Petri.SysObjects.PetriEngine;

public class StaticFunctions
{
	public static void DoStartup(UUID pidNameSpace)
		throws BigBangJewelException
	{
		try
		{
			PetriEngine.StartupAllProcesses(pidNameSpace);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
