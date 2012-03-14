package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.xerox.docushare.DSContentElement;
import com.xerox.docushare.DSContentElementException;
import com.xerox.docushare.DSException;

public class MemoryContentElement
	extends DSContentElement
{
	private static final long serialVersionUID = 1L;

	private String mstrPrefName;
	private byte[] marrData;

	private transient ByteArrayInputStream lstream;

	public MemoryContentElement(String pstrPrefName, byte[] parrData)
	{
		mstrPrefName = pstrPrefName;
		marrData = parrData;
	}

	public void setPreferredFileName(String arg0)
	{
		mstrPrefName = arg0;
	}

	public String getLocalFileName()
		throws DSContentElementException
	{
		return null;
	}

	public String getPreferredFileName()
		throws DSException
	{
		return mstrPrefName;
	}

	public void open()
		throws DSContentElementException
	{
		lstream = new ByteArrayInputStream(marrData);
	}

	public boolean isOpen()
	{
		return (lstream != null);
	}

	public void destroy()
		throws SecurityException, DSContentElementException
	{
		if ( isOpen() )
			try { lstream.close(); } catch (IOException e) {}

		lstream = null;
		marrData = null;
	}

	public long getContentLength()
		throws DSContentElementException
	{
		return marrData.length;
	}

	public int read(byte[] arg0, int arg1, int arg2)
		throws IOException, DSContentElementException
	{
		return lstream.read(arg0, arg1, arg2);
	}

	public int read(byte[] arg0)
		throws IOException, DSContentElementException
	{
		return lstream.read(arg0);
	}

	public byte[] read(int arg0)
		throws IOException, DSContentElementException
	{
		int len;
		byte[] larrResult;

		len = available();
		len = ( len > arg0 ? arg0 : len );
		larrResult = new byte[len];

		lstream.read(larrResult);

		return larrResult;
	}

	public int available()
		throws IOException, DSContentElementException
	{
		return lstream.available();
	}

	public void close()
		throws IOException, DSContentElementException
	{
		lstream.close();
		lstream = null;
		marrData = null;
	}

	public void close(boolean arg0)
		throws IOException, DSContentElementException
	{
		lstream.close();
		lstream = null;
		if ( arg0 )
			marrData = null;
	}

	public int read()
		throws IOException, DSContentElementException
	{
		return lstream.read();
	}

	public void reset()
		throws IOException, DSContentElementException
	{
		lstream.reset();
	}

	public long skip(long arg0)
		throws IOException, DSContentElementException
	{
		return lstream.skip(arg0);
	}
}
