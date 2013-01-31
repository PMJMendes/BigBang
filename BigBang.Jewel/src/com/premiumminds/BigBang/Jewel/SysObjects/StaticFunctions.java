package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.PetriEngine;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class StaticFunctions
{
	public static void DoLogin(UUID pidNameSpace, UUID pidUser, boolean pbNested)
		throws BigBangJewelException
	{
		String lstrPrinter;
		UserDecoration lobjDeco;
		HashMap<String, String> larrParams;
		UUID lidParams;
        MasterDB ldb;
        ResultSet lrs;
		ObjectBase lobjParam;

		if ( pbNested )
			return;

    	larrParams = new HashMap<String, String>();
    	lobjDeco = null;

    	ldb = null;
    	lrs = null;
		try
		{
			ldb = new MasterDB();

			lidParams = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AppParams);
			lrs = Entity.GetInstance(lidParams).SelectAll(ldb);
			while (lrs.next())
			{
				lobjParam = Engine.GetWorkInstance(lidParams, lrs);
				larrParams.put((String)lobjParam.getAt(2), (String)lobjParam.getAt(1));
			}
			lrs.close();
	    	lrs = null;

	        ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		Engine.getUserData().put("MailServer", larrParams.get("SERVER"));

		Engine.getUserData().put("Printer", larrParams.get("PRINTER"));

    	lobjDeco = UserDecoration.GetByUserID(pidNameSpace, pidUser);
		if ( lobjDeco == null )
			return;

		lstrPrinter = (String)lobjDeco.getAt(UserDecoration.I.PRINTERNAME);
		if(lstrPrinter != null)
		{
			Engine.getUserData().put("Printer", lstrPrinter);
		}
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
