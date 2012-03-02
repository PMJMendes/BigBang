package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class QuoteRequest
	extends ProcessData
{
    public static QuoteRequest GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (QuoteRequest)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequest), pidKey);
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

	public UUID GetProcessID()
	{
		return (UUID)getAt(1);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(1, pidProcess);
	}

    public QuoteRequestSubLine[] GetCurrentSubLines()
    	throws BigBangJewelException
    {
		ArrayList<QuoteRequestSubLine> larrAux;
		IEntity lrefQuoteRequestSubLines;
        MasterDB ldb;
        ResultSet lrsSubLines;

		larrAux = new ArrayList<QuoteRequestSubLine>();

		try
		{
			lrefQuoteRequestSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_QuoteRequestSubLine)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubLines = lrefQuoteRequestSubLines.SelectByMembers(ldb, new int[] {Constants.FKRequest_In_ReqSubLine},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsSubLines.next() )
				larrAux.add(QuoteRequestSubLine.GetInstance(getNameSpace(), lrsSubLines));
		}
		catch (BigBangJewelException e)
		{
			try { lrsSubLines.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
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

		return larrAux.toArray(new QuoteRequestSubLine[larrAux.size()]);
    }

    public QuoteRequestSubLine GetSubLineByID(UUID pidSubLine)
    	throws BigBangJewelException
    {
    	QuoteRequestSubLine[] larrSubLines;
    	int i;

    	larrSubLines = GetCurrentSubLines();
    	for( i = 0; i < larrSubLines.length; i++ )
    	{
    		if ( larrSubLines[i].GetSubLine().getKey().equals(pidSubLine) )
    			return larrSubLines[i];
    	}

    	return null;
    }

	public QuoteRequestObject[] GetCurrentObjects()
		throws BigBangJewelException
	{
		ArrayList<QuoteRequestObject> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<QuoteRequestObject>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_QuoteRequestObject)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {Constants.FKRequest_In_ReqObject},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(QuoteRequestObject.GetInstance(getNameSpace(), lrsObjects));
		}
		catch (BigBangJewelException e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
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

		return larrAux.toArray(new QuoteRequestObject[larrAux.size()]);
	}
}
