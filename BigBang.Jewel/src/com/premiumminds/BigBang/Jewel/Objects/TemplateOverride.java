package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class TemplateOverride
	extends ObjectBase
{
    public static TemplateOverride GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (TemplateOverride)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_TemplateOverride), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static TemplateOverride GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (TemplateOverride)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_TemplateOverride), prsObject);
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
		if ( getAt(1) == null )
			return null;

		if ( getAt(1) instanceof FileXfer )
			return (FileXfer)getAt(1);
		else
			return new FileXfer((byte[])getAt(1));
	}
}
