package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class AccountingEntry
	extends ObjectBase
{
	public static class I
	{
		public static final int NUMBER      =  0;
		public static final int DATE        =  1;
		public static final int ACCOUNT     =  2;
		public static final int VALUE       =  3;
		public static final int SIGN        =  4;
		public static final int BOOK        =  5;
		public static final int SUPPORT     =  6;
		public static final int DESCRIPTION =  7;
		public static final int DOCTYPE     =  8;
		public static final int YEAR        =  9;
		public static final int FILE        = 10;
	}

    public static AccountingEntry GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (AccountingEntry)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_AccountingEntry), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static AccountingEntry GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (AccountingEntry)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_AccountingEntry), prsObject);
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
}
