package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class Line
	extends ObjectBase
{
    public static Line GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Line)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Line), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Line GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (Line)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Line), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()	
		throws JewelEngineException
	{
	}

    public SubLine[] GetCurrentSubLines()
    	throws BigBangJewelException
    {
		int[] larrMembers;
		java.lang.Object[] larrParams;
		Entity lrefSubLines;
		MasterDB ldb;
		ArrayList<SubLine> larrAux;
		ResultSet lrsSubLines;

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKLine_In_SubLine;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		larrAux = new ArrayList<SubLine>();

		try
		{
			lrefSubLines = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_SubLine)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}


		try
		{
			lrsSubLines = lrefSubLines.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsSubLines.next() )
				larrAux.add(SubLine.GetInstance(getNameSpace(), lrsSubLines));
		}
		catch (Throwable e)
		{
			try { lrsSubLines.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubLines.close();
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

		return larrAux.toArray(new SubLine[larrAux.size()]);
    }
}
