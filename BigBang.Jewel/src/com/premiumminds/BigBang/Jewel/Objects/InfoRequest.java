package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class InfoRequest
	extends ProcessData
{
	public void Initialize()
		throws JewelEngineException
	{
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
