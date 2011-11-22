package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class PolicyCoverageData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midOwner;
	public UUID midCoverage;
	public Boolean mbPresent;

	public boolean mbNew;

	public PolicyCoverageData mobjPrevValues;

	public void Clone(PolicyCoverageData pobjSource)
	{
		mid = pobjSource.mid;
		midOwner = pobjSource.midOwner;
		midCoverage = pobjSource.midCoverage;
		mbPresent = pobjSource.mbPresent;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midOwner = (UUID)pobjSource.getAt(0);
		midCoverage = (UUID)pobjSource.getAt(1);
		mbPresent = (Boolean)pobjSource.getAt(2);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midOwner);
			pobjDest.setAt(1, midCoverage);
			pobjDest.setAt(2, mbPresent);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
	}
}
