package com.premiumminds.BigBang.Jewel.Objects;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class CostCenter
	extends ObjectBase
{
    public static CostCenter GetInstance(UUID pidNameSpace, UUID pidKey)
		throws InvocationTargetException, JewelEngineException, SQLException
	{
	    return (CostCenter)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_CostCenter), pidKey);
	}

	public void Initialize() throws JewelEngineException
	{
	}
}
