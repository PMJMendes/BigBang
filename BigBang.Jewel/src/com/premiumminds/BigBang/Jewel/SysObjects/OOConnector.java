package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;

import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sdbc.XCloseable;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class OOConnector
{
	public static XTextDocument getDocFromBytes(byte[] parrBytes)
		throws BigBangJewelException
	{
		OOInputStream lobjInp;
		PropertyValue[] larrProps;
		XComponentContext lobjContext;
		XMultiComponentFactory lobjFactory;
		XUnoUrlResolver lobjResolver;
		XPropertySet lobjPropSet;
		XComponentLoader lobjLoader;
		XComponent lobjComponent;

		lobjInp = new OOInputStream(parrBytes);
		larrProps = new PropertyValue[] {new PropertyValue(), new PropertyValue()};
		larrProps[0].Name = "InputStream";
		larrProps[0].Value = lobjInp;
		larrProps[1].Name = "Hidden";
		larrProps[1].Value = new Boolean(true);

		try
		{
			lobjContext = Bootstrap.createInitialComponentContext(null);
			lobjResolver = UnoRuntime.queryInterface(XUnoUrlResolver.class,
					lobjContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", lobjContext));
			lobjFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class,
					lobjResolver.resolve("uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager"));
			lobjPropSet = UnoRuntime.queryInterface(XPropertySet.class, lobjFactory);
			lobjContext = UnoRuntime.queryInterface(XComponentContext.class, lobjPropSet.getPropertyValue("DefaultContext"));
			lobjLoader = UnoRuntime.queryInterface(XComponentLoader.class,
					lobjFactory.createInstanceWithContext("com.sun.star.frame.Desktop", lobjContext));
			lobjComponent = lobjLoader.loadComponentFromURL("private:stream", "_blank", 0, larrProps);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return UnoRuntime.queryInterface(XTextDocument.class, lobjComponent);
	}

	public static FileXfer getFileFromDoc(XTextDocument pobjDoc, String pstrFileName)
		throws BigBangJewelException
	{
		OOOutputStream lobjOut;
		PropertyValue[] larrProps;
        XStorable lobjStore;
        XCloseable lobjClose;
		byte[] larrBytes;
		FileXfer lobjResult;

		lobjOut = new OOOutputStream();
		larrProps = new PropertyValue[] {new PropertyValue()};
		larrProps[0].Name = "OutputStream";
		larrProps[0].Value = lobjOut;

		try
		{
	        lobjStore = UnoRuntime.queryInterface(XStorable.class, pobjDoc);
	        lobjStore.storeToURL("private:stream", larrProps);

	        lobjClose = UnoRuntime.queryInterface(XCloseable.class, pobjDoc);
	        if ( lobjClose == null )
	        	pobjDoc.dispose();
	        else
	        	lobjClose.close();

			larrBytes = lobjOut.toByteArray();
			lobjResult = new FileXfer(larrBytes.length, "application/vnd.oasis.opendocument.text", pstrFileName,
					new ByteArrayInputStream(larrBytes));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}

	public static FileXfer getPDFFromDoc(XTextDocument pobjDoc, String pstrFileName)
		throws BigBangJewelException
	{
		OOOutputStream lobjOut;
		PropertyValue[] larrProps;
        XStorable lobjStore;
        XCloseable lobjClose;
		byte[] larrBytes;
		FileXfer lobjResult;

		lobjOut = new OOOutputStream();
		larrProps = new PropertyValue[] {new PropertyValue(), new PropertyValue()};
		larrProps[0].Name = "OutputStream";
		larrProps[0].Value = lobjOut;
		larrProps[1].Name = "FilterName";
		larrProps[1].Value = "writer_pdf_Export";

		try
		{
	        lobjStore = UnoRuntime.queryInterface(XStorable.class, pobjDoc);
	        lobjStore.storeToURL("private:stream", larrProps);

	        lobjClose = UnoRuntime.queryInterface(XCloseable.class, pobjDoc);
	        if ( lobjClose == null )
	        	pobjDoc.dispose();
	        else
	        	lobjClose.close();

			larrBytes = lobjOut.toByteArray();
			lobjResult = new FileXfer(larrBytes.length, "application/pdf", pstrFileName + ".pdf",
					new ByteArrayInputStream(larrBytes));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
