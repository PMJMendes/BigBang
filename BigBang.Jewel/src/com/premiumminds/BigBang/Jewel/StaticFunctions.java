package com.premiumminds.BigBang.Jewel;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.PetriEngine;

public class StaticFunctions
{
	public static void DoLogin(UUID pidNameSpace, UUID pidUser, boolean pbNested)
		throws BigBangJewelException
	{
	}

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
