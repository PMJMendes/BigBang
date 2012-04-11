package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class DASRequest
	extends ProcessData
{
    public static DASRequest GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (DASRequest)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_DASRequest), pidKey);
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

    public String getLabel()
    {
    	return "Pedido de Declaração de Ausência de Sinistros";
    }

	public UUID GetProcessID()
	{
		return (UUID)getAt(0);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(0, pidProcess);
	}
}
