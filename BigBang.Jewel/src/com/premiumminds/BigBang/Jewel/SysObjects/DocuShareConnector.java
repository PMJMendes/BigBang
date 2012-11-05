package com.premiumminds.BigBang.Jewel.SysObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.xerox.docushare.DSClass;
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
import com.xerox.docushare.property.DSLinkDesc;
import com.xerox.docushare.property.DSProperties;

public class DocuShareConnector
{
	private static DSHandle TEMPORARY_SCAN_REPOSITORY = new DSHandle("Collection-59066");
	private static DSHandle REMOVED_ITEM_REPOSITORY = new DSHandle("Collection-116558");

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
			lhFolder = TEMPORARY_SCAN_REPOSITORY;
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
		IProcess lobjProcess;

		lstrHandle = null;

		try
		{
			if ( Constants.ObjID_Client.equals(UUID.fromString(ownerTypeId)) )
				lstrHandle = (String)Client.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(ownerId)).getAt(23);
			if ( Constants.ObjID_Policy.equals(UUID.fromString(ownerTypeId)) )
				lstrHandle = (String)Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(ownerId)).getAt(15);
			if ( Constants.ObjID_SubPolicy.equals(UUID.fromString(ownerTypeId)) )
				lstrHandle = (String)SubPolicy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(ownerId)).getAt(9);
			if ( Constants.ObjID_Receipt.equals(UUID.fromString(ownerTypeId)) )
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), Receipt.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(ownerId)).GetProcessID()).GetParent();
				if ( Constants.ProcID_Policy.equals(lobjProcess.GetScriptID()) )
					lstrHandle = (String)((Policy)(lobjProcess.GetData())).getAt(15);
				else
					lstrHandle = (String)((SubPolicy)(lobjProcess.GetData())).getAt(9);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return ( lstrHandle == null ? getItemsContext(null, false) : getItemsContext(lstrHandle, true) );
	}

	public static String getItemTitle(String pstrItem)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		
		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		try
		{
			return lrefSession.getObject(new DSHandle(pstrItem)).getTitle();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileXfer getItemAsFile(String pstrItem)
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

	public static int getItemPageCount(String pstrItem)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSDocument lobjAux;
		DSContentElement[] larrAux;
		PDDocument lobjDocument;
		int llngResult;

		lrefSession = GetSession();
		if ( lrefSession == null )
			return -1;

		try
		{
			lobjAux = (DSDocument)lrefSession.getObject(new DSHandle(pstrItem));
			larrAux = lobjAux.getContentElements();
			larrAux[0].open();
			try
			{
				lobjDocument = PDDocument.load(larrAux[0]);
			}
			catch (Throwable e1)
			{
				try { larrAux[0].close(); } catch (Throwable e2) {}
				throw e1;
			}
			try
			{
				larrAux[0].close();
			}
			catch (Throwable e1)
			{
				try { lobjDocument.close(); } catch (Throwable e2) {}
				throw e1;
			}
			llngResult = lobjDocument.getDocumentCatalog().getAllPages().size();
			lobjDocument.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return llngResult;
	}

	public static BufferedImage getItemAsImage(String pstrItem, int plngPage)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSDocument lobjAux;
		DSContentElement[] larrAux;
		PDDocument lobjDocument;
		PDPage lobjPage;
		int llngRot;
		BufferedImage lobjImage;
		AffineTransform lobjXForm;
		AffineTransformOp lobjOp;

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
				lobjDocument = PDDocument.load(larrAux[0]);
			}
			catch (Throwable e1)
			{
				try { larrAux[0].close(); } catch (Throwable e2) {}
				throw e1;
			}
			try
			{
				larrAux[0].close();
			}
			catch (Throwable e1)
			{
				try { lobjDocument.close(); } catch (Throwable e2) {}
				throw e1;
			}
			lobjPage = (PDPage)lobjDocument.getDocumentCatalog().getAllPages().get(plngPage);
			llngRot = lobjPage.findRotation();
			try
			{
				lobjImage = lobjPage.convertToImage(BufferedImage.TYPE_INT_ARGB, 200);
			}
			catch (Throwable e1)
			{
				try { lobjDocument.close(); } catch (Throwable e2) {}
				throw e1;
			}
			lobjDocument.close();

			if ( llngRot != 0 )
			{
				lobjXForm = new AffineTransform();
				lobjXForm.translate(0.5*lobjImage.getHeight(), 0.5*lobjImage.getWidth());
				lobjXForm.quadrantRotate(llngRot/90);
				lobjXForm.translate(-0.5*lobjImage.getWidth(), -0.5*lobjImage.getHeight());
				lobjOp = new AffineTransformOp(lobjXForm, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				lobjImage = lobjOp.filter(lobjImage, null);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjImage;
	}

	public static void createItem(FileXfer pobjFile, String pstrTitle, String pstrLocation)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSCollection lobjTarget;
		DSClass lobjDocClass;
		DSProperties lobjDocProps;
		DSContentElement[] larrContent;

		lrefSession = GetSession();
		if ( lrefSession == null )
			return;

		larrContent = new DSContentElement[] {new MemoryContentElement(pobjFile.getFileName(), pobjFile.getData())};

		try
		{
			lobjTarget = (DSCollection)lrefSession.getObject((pstrLocation == null ? TEMPORARY_SCAN_REPOSITORY :
					new DSHandle(pstrLocation)));

			lobjDocClass = lrefSession.getDSClass(DSDocument.classname);
			lobjDocProps = lobjDocClass.createPrototype();
			lobjDocProps.setPropValue("title", pstrTitle);

			lrefSession.createDocument(lobjDocProps, null, null, larrContent, pobjFile.getContentType(),
					DSLinkDesc.containment, lobjTarget, null, null);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void moveItem(String pstrItem, String pstrFrom, String pstrTo)
		throws BigBangJewelException
	{
		DSSession lrefSession;
		DSHandle lhFrom, lhTo;
		DSObject lobjObject;
		
		lrefSession = GetSession();
		if ( lrefSession == null )
			return;

		lhFrom = ( pstrFrom == null ? REMOVED_ITEM_REPOSITORY : new DSHandle(pstrFrom) );
		lhTo = ( pstrTo == null ? REMOVED_ITEM_REPOSITORY : new DSHandle(pstrTo) );

		try
		{
			lobjObject = lrefSession.getObject(new DSHandle(pstrItem));
			lobjObject.addSource(DSLinkDesc.containment, lhTo);
			lobjObject.removeSource(DSLinkDesc.containment, lhFrom);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
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
			lhFolder = TEMPORARY_SCAN_REPOSITORY;
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
