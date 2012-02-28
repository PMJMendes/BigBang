package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class QuoteRequest
	extends ProcessData
{
	public void Initialize()
		throws JewelEngineException
	{
	}

	public UUID GetProcessID()
	{
		return (UUID)getAt(1);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(1, pidProcess);
	}
}
