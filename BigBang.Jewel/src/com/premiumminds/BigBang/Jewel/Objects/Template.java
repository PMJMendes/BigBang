package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

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
	{
		return new FileXfer((byte[])getAt(1));
	}
}
