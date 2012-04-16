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
import Jewel.Engine.SysObjects.ObjectBase;

public class ReportDef
	extends ObjectBase
{
	public static class I
	{
		public static int NAME            = 0;
		public static int ORDER           = 1;
		public static int OBJECT          = 2;
		public static int PARENT          = 3;
		public static int TEMPLATE        = 4;
		public static int TRANSACTIONTYPE = 5;
		public static int METHOD          = 6;
		public static int TYPE            = 7;
	}
    public static ReportDef GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (ReportDef)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ReportDef), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static ReportDef GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (ReportDef)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ReportDef), prsObject);
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

    public ReportDef[] GetChildren()
    	throws BigBangJewelException
    {
		ArrayList<ReportDef> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<ReportDef>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReportDef)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {ReportDef.I.PARENT}, new java.lang.Object[] {getKey()},
					new int[] {ReportDef.I.ORDER});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(ReportDef.GetInstance(getNameSpace(), lrsObjects));
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

		return larrAux.toArray(new ReportDef[larrAux.size()]);
    }

    public ReportParam[] GetParams()
    	throws BigBangJewelException
    {
		ArrayList<ReportParam> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<ReportParam>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ReportParam)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {ReportParam.I.OWNER}, new java.lang.Object[] {getKey()},
					new int[] {ReportParam.I.ORDER});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(ReportParam.GetInstance(getNameSpace(), lrsObjects));
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

		return larrAux.toArray(new ReportParam[larrAux.size()]);
    }
}
