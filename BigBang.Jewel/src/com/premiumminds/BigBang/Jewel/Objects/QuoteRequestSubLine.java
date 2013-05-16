package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class QuoteRequestSubLine
	extends ObjectBase
{
    public static QuoteRequestSubLine GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (QuoteRequestSubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestSubLine), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static QuoteRequestSubLine GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (QuoteRequestSubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestSubLine), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private QuoteRequest mrefOwner;
	private SubLine mrefSubLine;

	public void Initialize() 
		throws JewelEngineException
	{
		if ( mrefSubLine == null )
		{
			try
			{
				mrefSubLine = SubLine.GetInstance(getNameSpace(), (UUID)getAt(1));
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}

		if ( mrefOwner == null )
		{
			try
			{
				mrefOwner = QuoteRequest.GetInstance(getNameSpace(), (UUID)getAt(0));
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}
	}

    public String AfterSave() 
    	throws JewelEngineException
    {
    	if ( mrefSubLine == null )
    		Initialize();

        return "";
    }

    public QuoteRequest GetOwner()
    {
    	if ( mrefOwner == null )
    	{
			try
			{
				Initialize();
			}
			catch (Throwable e)
			{
			}
    	}

    	return mrefOwner;
    }

	public SubLine GetSubLine()
	{
		return mrefSubLine;
	}

	public QuoteRequestCoverage[] GetCurrentCoverages(SQLServer pdb)
		throws BigBangJewelException
	{
		ArrayList<QuoteRequestCoverage> larrAux;
		IEntity lrefQuoteRequestCoverages;
        ResultSet lrsCoverages;

		larrAux = new ArrayList<QuoteRequestCoverage>();

		try
		{
			lrefQuoteRequestCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_QuoteRequestCoverage)); 
			lrsCoverages = lrefQuoteRequestCoverages.SelectByMembers(pdb, new int[] {Constants.FKReqSubLine_In_ReqCoverage},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsCoverages.next() )
				larrAux.add(QuoteRequestCoverage.GetInstance(getNameSpace(), lrsCoverages));
		}
		catch (BigBangJewelException e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoverages.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new QuoteRequestCoverage[larrAux.size()]);
	}

	public QuoteRequestValue[] GetCurrentValues(SQLServer pdb)
		throws BigBangJewelException
	{
		ArrayList<QuoteRequestValue> larrAux;
		IEntity lrefContactInfo;
        ResultSet lrsInfo;

		larrAux = new ArrayList<QuoteRequestValue>();

		try
		{
			lrefContactInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_QuoteRequestValue)); 
			lrsInfo = lrefContactInfo.SelectByMembers(pdb, new int[] {Constants.FKReqSubLine_In_ReqValue},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(QuoteRequestValue.GetInstance(getNameSpace(), lrsInfo));
		}
		catch (BigBangJewelException e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new QuoteRequestValue[larrAux.size()]);
	}

	public QuoteRequestCoverage[] GetCurrentCoverages()
		throws BigBangJewelException
	{
        MasterDB ldb;
		QuoteRequestCoverage[] larrAux;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			larrAux = GetCurrentCoverages(ldb);
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

		return larrAux;
	}

	public QuoteRequestValue[] GetCurrentValues()
		throws BigBangJewelException
	{
        MasterDB ldb;
		QuoteRequestValue[] larrAux;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			larrAux = GetCurrentValues(ldb);
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

		return larrAux;
	}

    public QuoteRequestValue[] GetCurrentKeyedValues(UUID pidObject)
    	throws BigBangJewelException
    {
		ArrayList<QuoteRequestValue> larrAux;
		IEntity lrefRequestValues;
        MasterDB ldb;
        ResultSet lrsValues;

		larrAux = new ArrayList<QuoteRequestValue>();

		try
		{
			lrefRequestValues = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_QuoteRequestValue)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsValues = lrefRequestValues.SelectByMembers(ldb, new int[] {Constants.FKReqSubLine_In_ReqValue,
					Constants.FKObject_In_ReqValue}, new java.lang.Object[] {getKey(), pidObject}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsValues.next() )
				larrAux.add(QuoteRequestValue.GetInstance(getNameSpace(), lrsValues));
		}
		catch (BigBangJewelException e)
		{
			try { lrsValues.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsValues.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsValues.close();
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

		return larrAux.toArray(new QuoteRequestValue[larrAux.size()]);
    }
}
