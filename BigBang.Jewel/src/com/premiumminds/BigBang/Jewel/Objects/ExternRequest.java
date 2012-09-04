package com.premiumminds.BigBang.Jewel.Objects;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class ExternRequest
	extends ProcessData
{
    public static ExternRequest GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (ExternRequest)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ExternRequest), pidKey);
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
    	return (String)getAt(1);
    }

	public UUID GetProcessID()
	{
		return (UUID)getAt(0);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(0, pidProcess);
	}

	public void setText(String pstrText)
		throws BigBangJewelException
	{
		byte[] larrAux;
		FileXfer laux;

		try
		{
			if ( pstrText == null )
			{
				setAt(2, (byte[])null);
				return;
			}

			larrAux = pstrText.getBytes();
			laux = new FileXfer(larrAux.length, "text/plain", "body.txt", new ByteArrayInputStream(larrAux));
			setAt(2, laux.GetVarData());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public String getText()
	{
		FileXfer laux;

		if ( getAt(2) == null )
			return null;

    	if ( getAt(2) instanceof FileXfer )
    		laux = (FileXfer)getAt(2);
    	else
        	laux = new FileXfer((byte[])getAt(2));

    	return new String(laux.getData());
	}
}
