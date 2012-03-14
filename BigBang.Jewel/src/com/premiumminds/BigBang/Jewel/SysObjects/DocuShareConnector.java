package com.premiumminds.BigBang.Jewel.SysObjects;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.xerox.docushare.DSContentElement;
import com.xerox.docushare.DSFactory;
import com.xerox.docushare.DSHandle;
import com.xerox.docushare.DSObject;
import com.xerox.docushare.DSObjectIterator;
import com.xerox.docushare.DSSelectSet;
import com.xerox.docushare.DSServer;
import com.xerox.docushare.DSSession;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.object.DSDocument;

public class DocuShareConnector
{
	private static DSServer GetServer()
		throws BigBangJewelException
	{
		DSServer lrefResult;

        if (Engine.getUserData() == null)
            return null;

        lrefResult = (DSServer)Engine.getUserData().get("BigBang_DocuShare_Server");
        if (lrefResult == null)
        {
        	try
        	{
				lrefResult = DSFactory.createServer("192.168.0.17", 1099);
			}
        	catch (Throwable e)
        	{
        		throw new BigBangJewelException(e.getMessage(), e);
			}
        	Engine.getUserData().put("BigBang_DocuShare_Server", lrefResult);
        }

        return lrefResult;
	}

	private static DSSession GetSession()
		throws BigBangJewelException
	{
		DSSession lrefResult;

        if (Engine.getUserData() == null)
            return null;

        lrefResult = (DSSession)Engine.getUserData().get("BigBang_DocuShare_Session");
        if (lrefResult == null)
        {
        	try
        	{
				lrefResult = GetServer().createSession("DocuShare", "scanner", "scanner");
			}
        	catch (BigBangJewelException e)
        	{
        		throw e;
			}
        	catch (Throwable e)
        	{
        		throw new BigBangJewelException(e.getMessage(), e);
			}
        	Engine.getUserData().put("BigBang_DocuShare_Session", lrefResult);
        }

        return lrefResult;
	}

	public static void LogOff()
	{
		DSSession lrefSession;
		DSServer lrefServer;

        if (Engine.getUserData() == null)
            return;

        lrefSession = (DSSession)Engine.getUserData().get("BigBang_DocuShare_Session");
        if (lrefSession != null)
        {
        	Engine.getUserData().remove("BigBang_DocuShare_Session");
	        try { lrefSession.close(); } catch (Throwable e) {}
        }

        lrefServer = (DSServer)Engine.getUserData().get("BigBang_DocuShare_Server");
        if (lrefServer != null)
        {
        	Engine.getUserData().remove("BigBang_DocuShare_Server");
	        try { lrefServer.close(); } catch (Throwable e) {}
		}
	}

	public static DSObject[] getItems(String pstrFolder, boolean pbWithFolders)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSHandle lhFolder;
		DSCollection lobjFolder;
		DSObjectIterator i;
		DSObject lobjAux;
		ArrayList<DSObject> larrFolders;
		ArrayList<DSObject> larrItems;

		if ( pbWithFolders )
			return getItemsContext(pstrFolder, false);

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		larrFolders = new ArrayList<DSObject>();
		larrItems = new ArrayList<DSObject>();

		if ( pstrFolder == null )
			lhFolder = new DSHandle("Collection-59066");
		else
			lhFolder = new DSHandle(pstrFolder);

		try
		{
			lobjFolder = (DSCollection)lrefSession.getObject(lhFolder);
			i = lobjFolder.children(null);
			while ( i.hasNext() )
			{
				lobjAux = i.nextObject();
				if ( !(lobjAux instanceof DSCollection) )
					larrItems.add(lobjAux);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrFolders.addAll(larrItems);

		return larrFolders.toArray(new DSObject[larrFolders.size()]);
	}

	public static DSObject[] getContext(String ownerId, String ownerTypeId)
		throws BigBangJewelException 
	{
		String lstrHandle;

		lstrHandle = null;

		try
		{
			if ( Constants.ObjID_Client.equals(UUID.fromString(ownerTypeId)) )
				lstrHandle = (String)Client.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(ownerId)).getAt(23);
			if ( Constants.ObjID_Policy.equals(UUID.fromString(ownerTypeId)) )
				lstrHandle = (String)Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(ownerId)).getAt(15);
			if ( Constants.ObjID_Receipt.equals(UUID.fromString(ownerTypeId)) )
				lstrHandle = (String)((Policy)(PNProcess.GetInstance(Engine.getCurrentNameSpace(),
						Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(ownerId)).GetProcessID()).GetParent().GetData())).getAt(15);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return ( lstrHandle == null ? null : getItemsContext(lstrHandle, true) );
	}

	public static FileXfer getItem(String pstrItem)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSDocument lobjAux;
		DSContentElement[] larrAux;
		FileXfer lobjFile;

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		try
		{
			lobjAux = (DSDocument)lrefSession.getObject(new DSHandle(pstrItem));
			larrAux = lobjAux.getContentElements();
			larrAux[0].open();
			try
			{
				lobjFile = new FileXfer((int)lobjAux.getSize(), lobjAux.getContentType(), lobjAux.getOriginalFileName(), larrAux[0]);
			}
			catch (Throwable e1)
			{
				try { larrAux[0].close(); } catch (Throwable e2) {}
				throw e1;
			}
			larrAux[0].close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjFile;
	}

	public static void deleteItem(String pstrItem)
		throws BigBangJewelException
	{
		DSSession lrefSession;

		lrefSession = GetSession();
		if ( lrefSession == null )
			return;

		try
		{
			lrefSession.deleteObject(new DSHandle(pstrItem), new DSSelectSet(), true);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

//	public static String getItemAsImage(String pstrItem)
//		throws SessionExpiredException, BigBangJewelException
//	{
//		DSSession lrefSession;
//		DSDocument lobjAux;
//		DSContentElement[] larrAux;
//		PDDocument lobjDocument;
//		PDPage lobjPage;
//		BufferedImage lobjImage;
//		ByteArrayOutputStream lstreamOutput;
//		byte[] larrBuffer;
//		ByteArrayInputStream lstreamInput;
//		FileXfer lobjFile;
//		UUID lidKey;
//
//		lrefSession = GetSession();
//		if ( lrefSession == null )
//			return null;
//
//		try
//		{
//			lobjAux = (DSDocument)lrefSession.getObject(new DSHandle(pstrItem));
//			larrAux = lobjAux.getContentElements();
//			larrAux[0].open();
//			try
//			{
//				lobjDocument = PDDocument.load(larrAux[0]);
//			}
//			catch (Throwable e1)
//			{
//				try { larrAux[0].close(); } catch (Throwable e2) {}
//				throw e1;
//			}
//			try
//			{
//				larrAux[0].close();
//			}
//			catch (Throwable e1)
//			{
//				try { lobjDocument.close(); } catch (Throwable e2) {}
//				throw e1;
//			}
//			lobjPage = (PDPage)lobjDocument.getDocumentCatalog().getAllPages().get(0);
//			try
//			{
//				lobjImage = lobjPage.convertToImage(BufferedImage.TYPE_INT_ARGB, 200);
//			}
//			catch (Throwable e1)
//			{
//				try { lobjDocument.close(); } catch (Throwable e2) {}
//				throw e1;
//			}
//			lobjDocument.close();
//
//			lstreamOutput = new ByteArrayOutputStream();
//			ImageIO.write(lobjImage, "png", lstreamOutput);
////			ImageIO.write(lobjImage, "jpg", lstreamOutput);
//			larrBuffer = lstreamOutput.toByteArray();
//			lstreamInput = new ByteArrayInputStream(larrBuffer);
//			lobjFile = new FileXfer(larrBuffer.length, "image/png", "pdfPage.png", lstreamInput);
////			lobjFile = new FileXfer(larrBuffer.length, "image/jpeg", "pdfPage.jpg", lstreamInput);
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		lidKey = UUID.randomUUID();
//		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
//
//		return lidKey.toString();
//	}

	public static String getItemAsImage(String pstrItem)
		throws BigBangJewelException
	{
//		DSSession lrefSession;
//		DSDocument lobjAux;
//		DSContentElement[] larrAux;
//		PdfDecoder lobjDecoder;
//		BufferedImage lobjImage;
//		ByteArrayOutputStream lstreamOutput;
//		byte[] larrBuffer;
//		ByteArrayInputStream lstreamInput;
//		FileXfer lobjFile;
//		UUID lidKey;
//
//		lrefSession = GetSession();
//		if ( lrefSession == null )
//			return null;
//
//		try
//		{
//			lobjAux = (DSDocument)lrefSession.getObject(new DSHandle(pstrItem));
//			larrAux = lobjAux.getContentElements();
//			larrAux[0].open();
//			lobjDecoder = new PdfDecoder();
//			try
//			{
//				lobjDecoder.openPdfFileFromInputStream(larrAux[0], false);
//			}
//			catch (Throwable e1)
//			{
//				try { larrAux[0].close(); } catch (Throwable e2) {}
//				throw e1;
//			}
//			try
//			{
//				larrAux[0].close();
//				lobjImage = lobjDecoder.getPageAsImage(1);
////				lobjImage = lobjDecoder.getPageAsHiRes(1);
//			}
//			catch (Throwable e1)
//			{
//				lobjDecoder.closePdfFile();
//				throw e1;
//			}
//			lobjDecoder.closePdfFile();
//
//			lstreamOutput = new ByteArrayOutputStream();
//			ImageIO.write(lobjImage, "png", lstreamOutput);
//			larrBuffer = lstreamOutput.toByteArray();
//			lstreamInput = new ByteArrayInputStream(larrBuffer);
//			lobjFile = new FileXfer(larrBuffer.length, "image/png", "pdfPage.png", lstreamInput);
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		lidKey = UUID.randomUUID();
//		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);

		return null;//lidKey.toString();
	}

	private static DSObject[] getItemsContext(String pstrFolder, boolean pbInjectTSR)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSHandle lhFolder;
		DSCollection lobjFolder;
		DSObjectIterator i;
		ArrayList<DSObject> larrFolders;
		ArrayList<DSObject> larrItems;
		DSObject lobjTmp;

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		larrFolders = new ArrayList<DSObject>();
		larrItems = new ArrayList<DSObject>();

		if ( pstrFolder == null )
		{
			lhFolder = new DSHandle("Collection-59066");
			pbInjectTSR = false;
		}
		else
			lhFolder = new DSHandle(pstrFolder);

		try
		{
			if ( pbInjectTSR )
				larrFolders.add(lrefSession.getObject(new DSHandle("Collection-59066")));

			lobjFolder = (DSCollection)lrefSession.getObject(lhFolder);
			i = lobjFolder.children(null);
			while ( i.hasNext() )
			{
				lobjTmp = i.nextObject();

				if ( lobjTmp instanceof DSCollection )
					larrFolders.add(lobjTmp);
				else
					larrItems.add(lobjTmp);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrFolders.addAll(larrItems);

		return larrFolders.toArray(new DSObject[larrFolders.size()]);
	}
}
