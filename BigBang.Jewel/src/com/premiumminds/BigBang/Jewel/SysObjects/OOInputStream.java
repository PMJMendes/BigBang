package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;

import com.sun.star.io.BufferSizeExceededException;
import com.sun.star.io.IOException;
import com.sun.star.io.NotConnectedException;
import com.sun.star.io.XInputStream;
import com.sun.star.io.XSeekable;
import com.sun.star.lang.IllegalArgumentException;

public class OOInputStream
	extends ByteArrayInputStream
	implements XInputStream, XSeekable
{

	public OOInputStream(byte[] buf)
	{
		super(buf);
	}

	public void closeInput()
		throws NotConnectedException, IOException
	{
        try
        {
            close();
        }
        catch (java.io.IOException e)
        {
            throw new IOException(e.getMessage(), this);
        }
	}

	public int readBytes(byte[][] buffer, int bufferSize)
		throws NotConnectedException, BufferSizeExceededException, IOException
	{
        int llngRead;
        byte[] larrBuffer, larrAux;

        larrBuffer = new byte[bufferSize];
        try
        {
            llngRead = super.read(larrBuffer);
        }
        catch (java.io.IOException e)
        {
            throw new IOException(e.getMessage(),this);
        }
        if(llngRead > 0)
        {
            if(llngRead < bufferSize)
            {
                larrAux = new byte[llngRead];
                System.arraycopy(larrBuffer, 0, larrAux, 0, llngRead);
                larrBuffer = larrAux;
            }
        }
        else
        {
            larrBuffer = new byte[0];
            llngRead = 0;
        }

        buffer[0] = larrBuffer;
        return llngRead;
	}

	public int readSomeBytes(byte[][] buffer, int bufferSize)
		throws NotConnectedException, BufferSizeExceededException, IOException
	{
		return readBytes(buffer, bufferSize);
	}

	public void skipBytes(int skipLength)
		throws NotConnectedException, BufferSizeExceededException, IOException
	{
		skip(skipLength);
	}

	public long getLength()
		throws IOException
	{
		return count;
	}

	public long getPosition()
		throws IOException
	{
		return pos;
	}

	public void seek(long position)
		throws IllegalArgumentException, IOException
	{
		pos = (int) position;
	}
}
