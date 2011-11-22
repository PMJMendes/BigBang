package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class PolicyExerciseData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrLabel;
	public UUID midOwner;
	public Timestamp mdtStart;
	public Timestamp mdtEnd;

	public boolean mbNew;
	public boolean mbDeleted;

	public PolicyExerciseData mobjPrevValues;

	public void Clone(PolicyExerciseData pobjSource)
	{
		mid = pobjSource.mid;
		mstrLabel = pobjSource.mstrLabel;
		midOwner = pobjSource.midOwner;
		mdtStart = pobjSource.mdtStart;
		mdtEnd = pobjSource.mdtEnd;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrLabel = (String)pobjSource.getAt(0);
		midOwner = (UUID)pobjSource.getAt(1);
		mdtStart = (Timestamp)pobjSource.getAt(2);
		mdtEnd = (Timestamp)pobjSource.getAt(3);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrLabel);
			pobjDest.setAt(1, midOwner);
			pobjDest.setAt(2, mdtStart);
			pobjDest.setAt(3, mdtEnd);
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
