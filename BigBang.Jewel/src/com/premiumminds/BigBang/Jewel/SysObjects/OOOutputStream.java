package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayOutputStream;

import com.sun.star.io.BufferSizeExceededException;
import com.sun.star.io.IOException;
import com.sun.star.io.NotConnectedException;
import com.sun.star.io.XOutputStream;

public class OOOutputStream
	extends ByteArrayOutputStream
	implements XOutputStream
{
	public OOOutputStream()
	{
        super(32768);
    }

	public void closeOutput()
		throws NotConnectedException, BufferSizeExceededException, IOException
	{
		try
		{
            super.flush();
            super.close();
        }
        catch (java.io.IOException e)
        {
            throw(new IOException(e.getMessage()));
        }
	}

	public void writeBytes(byte[] values)
		throws NotConnectedException, BufferSizeExceededException, IOException
	{
		try
		{
            write(values);
        }
        catch (java.io.IOException e)
        {
            throw(new IOException(e.getMessage()));
        }
	}

    public void flush()
    {
        try
        {
            super.flush();
        }
        catch (java.io.IOException e)
        {
        }
    }
}
