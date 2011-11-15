package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class PolicyValueData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrValue;
	public UUID midOwner;
	public UUID midField;
	public UUID midObject;
	public UUID midExercise;
	public int mlngObject;
	public int mlngExercise;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrValue = (String)pobjSource.getAt(0);
		midOwner = (UUID)pobjSource.getAt(1);
		midField = (UUID)pobjSource.getAt(2);
		midObject = (UUID)pobjSource.getAt(3);
		midExercise = (UUID)pobjSource.getAt(4);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrValue);
			pobjDest.setAt(1, midOwner);
			pobjDest.setAt(2, midField);
			pobjDest.setAt(3, midObject);
			pobjDest.setAt(4, midExercise);
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
