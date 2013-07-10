package com.premiumminds.BigBang.Jewel.Data;

import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class DocDataLight
	extends DocDataBase
{
	private static final long serialVersionUID = 1L;

	public transient byte[] mobjFile;

	public void FromObject(ObjectBase pobjSource)
	{
		super.FromObject(pobjSource);

		if ( pobjSource.getAt(5) instanceof FileXfer )
			mobjFile = ((FileXfer)pobjSource.getAt(5)).GetVarData();
		else
			mobjFile = (byte [])pobjSource.getAt(5);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		super.ToObject(pobjDest);

		try
		{
			pobjDest.setAt(5, mobjFile);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
