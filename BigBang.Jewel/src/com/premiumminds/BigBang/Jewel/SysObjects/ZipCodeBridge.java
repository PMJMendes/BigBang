package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;

public class ZipCodeBridge
{
	public static UUID GetZipCode(String pstrCode, String pstrCity, String pstrCounty, String pstrDistrict, String pstrCountry)
		throws BigBangJewelException
	{
		int[] larrMembers;
		java.lang.Object[] larrParams;
		ObjectBase lobjAux;
		UUID lidZipCodes;
		MasterDB ldb;
		ResultSet lrsCodes;

		larrMembers = new int[1];
		larrMembers[0] = Constants.ZipCode_In_PostalCode;
		larrParams = new java.lang.Object[1];
		larrParams[0] = "!" + pstrCode;

		lobjAux = null;

		try
		{
			lidZipCodes = Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCodes = Entity.GetInstance(lidZipCodes).SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			if ( lrsCodes.next() )
				lobjAux = Engine.GetWorkInstance(lidZipCodes, lrsCodes);
		}
		catch (Throwable e)
		{
			try { lrsCodes.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCodes.close();

			if ( lobjAux == null )
			{
				lobjAux = Engine.GetWorkInstance(lidZipCodes, (UUID)null);
				lobjAux.setAt(0, pstrCode);
				lobjAux.setAt(1, pstrCity);
				lobjAux.setAt(2, pstrCounty);
				lobjAux.setAt(3, pstrDistrict);
				lobjAux.setAt(4, pstrCountry);
				lobjAux.SaveToDb(ldb);
			}
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjAux.getKey();
	}
}
