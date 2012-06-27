package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;

public class InsurerAccountingMap
	extends TransactionMapBase
{
    public static InsurerAccountingMap GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerAccountingMap), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static InsurerAccountingMap GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerAccountingMap), prsObject);
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

	public UUID getSubObjectType()
	{
		return Constants.ObjID_InsurerAccountingDetail;
	}
}
