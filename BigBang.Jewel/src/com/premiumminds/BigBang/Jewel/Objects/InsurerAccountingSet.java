package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionSetBase;

public class InsurerAccountingSet
	extends TransactionSetBase
{
    public static InsurerAccountingSet GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerAccountingSet), pidKey);
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

	public String getTitle()
	{
		return "Prestações de Contas";
	}

	public UUID getSubObjectType()
	{
		return Constants.ObjID_InsurerAccountingMap;
	}
}
