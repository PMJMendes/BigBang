package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class PolicyObjectData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrName;
	public UUID midOwner;
	public UUID midType;
	public String mstrAddress1;
	public String mstrAddress2;
	public UUID midZipCode;
	public Timestamp mdtInclusion;
	public Timestamp mdtExclusion;

	public void Clone(PolicyObjectData pobjSource)
	{
		mid = pobjSource.mid;
		mstrName = pobjSource.mstrName;
		midOwner = pobjSource.midOwner;
		midType = pobjSource.midType;
		mstrAddress1 = pobjSource.mstrAddress1;
		mstrAddress2 = pobjSource.mstrAddress2;
		midZipCode = pobjSource.midZipCode;
		mdtInclusion = pobjSource.mdtInclusion;
		mdtExclusion = pobjSource.mdtExclusion;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrName = (String)pobjSource.getAt(0);
		midOwner = (UUID)pobjSource.getAt(1);
		midType = (UUID)pobjSource.getAt(2);
		mstrAddress1 = (String)pobjSource.getAt(3);
		mstrAddress2 = (String)pobjSource.getAt(4);
		midZipCode = (UUID)pobjSource.getAt(5);
		mdtInclusion = (Timestamp)pobjSource.getAt(6);
		mdtExclusion = (Timestamp)pobjSource.getAt(7);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrName);
			pobjDest.setAt(1, midOwner);
			pobjDest.setAt(2, midType);
			pobjDest.setAt(3, mstrAddress1);
			pobjDest.setAt(4, mstrAddress2);
			pobjDest.setAt(5, midZipCode);
			pobjDest.setAt(6, mdtInclusion);
			pobjDest.setAt(7, mdtExclusion);
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
