package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class DocInfoData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midOwner;
	public String mstrType;
	public String mstrValue;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midOwner = (UUID)pobjSource.getAt(0); 
		mstrType = (String)pobjSource.getAt(1); 
		mstrValue = (String)pobjSource.getAt(2); 
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midOwner);
			pobjDest.setAt(1, mstrType);
			pobjDest.setAt(2, mstrValue);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		pstrBuilder.append(mstrType);
		pstrBuilder.append(": ");
		pstrBuilder.append(mstrValue);
		pstrBuilder.append(pstrLineBreak);
	}
}
