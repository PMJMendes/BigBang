package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class GeneralSystem
	extends ProcessData
{
    public static GeneralSystem GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (GeneralSystem)Engine.GetCache(true).getAt(Engine.FindEntity(pidNameSpace, Constants.ObjID_GenSys), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static GeneralSystem GetAnyInstance(UUID pidNameSpace)
		throws BigBangJewelException
	{
		MasterDB ldb;
		ResultSet lrs;
		GeneralSystem lobjAux;

		lobjAux = null;

		try
		{
			ldb = new MasterDB();
			lrs = Entity.GetInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_GenSys)).SelectAll(ldb);
			if ( lrs.next() )
				lobjAux = GeneralSystem.GetInstance(pidNameSpace, lrs);
			lrs.close();
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjAux == null )
			throw new BigBangJewelException("Unexpected: No General System in place.");

		return lobjAux;
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

    public String getLabel()
    {
    	return "(Default)";
    }
}
