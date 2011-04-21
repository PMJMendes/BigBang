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

public class Document
	extends ObjectBase
{
    public static Document GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (Document)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Document), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Document GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (Document)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Document), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private ObjectBase mrefOwner;

	public void Initialize()
		throws JewelEngineException
	{
	}

	public ObjectBase getOwner()
		throws BigBangJewelException
	{
		if ( mrefOwner == null )
		{
			try
			{
				mrefOwner = Engine.GetWorkInstance(Engine.FindEntity(getNameSpace(), (UUID)getAt(1)), (UUID)getAt(2));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		return mrefOwner;
	}

	public DocInfo[] getCurrentInfo()
		throws BigBangJewelException
	{
		ArrayList<DocInfo> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefDocInfo;
	    MasterDB ldb;
	    ResultSet lrsInfo;

		larrAux = new ArrayList<DocInfo>();

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKOwner_In_DocInfo;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		try
		{
			lrefDocInfo = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DocInfo)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefDocInfo.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsInfo.next() )
				larrAux.add(DocInfo.GetInstance(getNameSpace(), lrsInfo));
		}
		catch (BigBangJewelException e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsInfo.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo.close();
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

		return larrAux.toArray(new DocInfo[larrAux.size()]);
	}
}
