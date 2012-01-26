package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Session;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Security.Password;
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
		User lobjUser;
		Password lobjPass;
		String lstrUser;
		String lstrPwd;
		UserDecoration lobjDeco;
		Hashtable<String, String> larrParams;
		UUID lidParams;
        MasterDB ldb;
        ResultSet lrs;
        ResultSet lrsParams;
		ObjectBase lobjParam;
		Properties lprops;

		if ( pbNested )
			return;

    	larrParams = new Hashtable<String, String>();
    	lobjDeco = null;

		try
		{
			ldb = new MasterDB();

			lidParams = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AppParams);
			lrsParams = Entity.GetInstance(lidParams).SelectAll(ldb);
			while (lrsParams.next())
			{
				lobjParam = Engine.GetWorkInstance(lidParams, lrsParams);
				larrParams.put((String)lobjParam.getAt(2), (String)lobjParam.getAt(1));
			}
			lrsParams.close();

			lrs = Entity.GetInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Decorations))
					.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {pidUser}, new int[0]);
		    if (lrs.next())
		    	lobjDeco = UserDecoration.GetInstance(pidNameSpace, lrs);
		    lrs.close();

	        ldb.Disconnect();

			lobjUser = User.GetInstance(pidNameSpace, pidUser);
			if ( lobjUser.getAt(2) instanceof Password )
				lobjPass = (Password)lobjUser.getAt(2);
			else
				lobjPass = new Password((String)lobjUser.getAt(2), true);
			lstrUser = lobjUser.getUserName();
			lstrPwd = lobjPass.GetClear();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lprops = new Properties();
		lprops.put("mail.host", larrParams.get("SERVER"));
		lprops.put("mail.from", (String)lobjDeco.getAt(1));
		Engine.getUserData().put("MailSession", Session.getInstance(lprops, new JewelAuthenticator(lstrUser, lstrPwd)));
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
