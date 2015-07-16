package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class Template
	extends ObjectBase
{
    public static Template GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Template)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Template), pidKey);
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

	public FileXfer getFile()
		throws BigBangJewelException
	{
		TemplateOverride lobjOverride;

		lobjOverride = GetOverride();
		if ( lobjOverride != null )
			return lobjOverride.getFile();

		if ( getAt(1) == null )
			return null;

		if ( getAt(1) instanceof FileXfer )
			return (FileXfer)getAt(1);
		else
			return new FileXfer((byte[])getAt(1));
	}

    private TemplateOverride GetOverride()
    	throws BigBangJewelException
    {
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefOverrides;
        MasterDB ldb;
        ResultSet lrsInfo;
        TemplateOverride lobjResult;

		lobjResult = null;

		larrMembers = new int[1];
		larrMembers[0] = 0;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		try
		{
			lrefOverrides = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_TemplateOverride)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsInfo = lrefOverrides.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			if ( lrsInfo.next() )
				lobjResult = TemplateOverride.GetInstance(getNameSpace(), lrsInfo);
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

		return lobjResult;
    }
}
