package com.premiumminds.BigBang.Jewel.Objects;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class TypifiedText
	extends ObjectBase
{
	String mstrText;

    public static TypifiedText GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (TypifiedText)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_TypifiedText), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static TypifiedText GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (TypifiedText)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_TypifiedText), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize() throws JewelEngineException
	{
		FileXfer laux;

		if ( getAt(3) == null )
			return;

    	if ( getAt(3) instanceof FileXfer )
    		laux = (FileXfer)getAt(3);
    	else
        	laux = new FileXfer((byte[])getAt(3));
    	mstrText = new String(laux.getData());
	}

	public String getText()
	{
		if ( mstrText == null )
		{
			try
			{
				Initialize();
			}
			catch (Throwable e)
			{
			}
		}

		return mstrText;
	}

	public void setText(String pstrText)
		throws BigBangJewelException
	{
		byte[] larrAux;
		FileXfer laux;

		mstrText = pstrText;

		try
		{
			if ( pstrText == null )
			{
				setAt(3, (byte[])null);
				return;
			}

			larrAux = pstrText.getBytes();
			laux = new FileXfer(larrAux.length, "text/plain", "body.txt", new ByteArrayInputStream(larrAux));
			setAt(3, laux.GetVarData());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
