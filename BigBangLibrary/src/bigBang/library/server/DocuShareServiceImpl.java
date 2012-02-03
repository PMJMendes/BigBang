package bigBang.library.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Objects.PNProcess;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.DocuShareItem;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.xerox.docushare.DSContentElement;
import com.xerox.docushare.DSFactory;
import com.xerox.docushare.DSHandle;
import com.xerox.docushare.DSObject;
import com.xerox.docushare.DSObjectIterator;
import com.xerox.docushare.DSServer;
import com.xerox.docushare.DSSession;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.object.DSDocument;

public class DocuShareServiceImpl
	extends EngineImplementor
	implements DocuShareService
{
	private static final long serialVersionUID = 1L;

	private static DSServer GetServer()
		throws BigBangException
	{
		DSServer lrefResult;

        if (getSession() == null)
            return null;

        lrefResult = (DSServer)getSession().getAttribute("BigBang_DocuShare_Server");
        if (lrefResult == null)
        {
        	try
        	{
				lrefResult = DSFactory.createServer("192.168.0.3", 1099);
			}
        	catch (Throwable e)
        	{
        		throw new BigBangException(e.getMessage(), e);
			}
            getSession().setAttribute("BigBang_DocuShare_Server", lrefResult);
        }

        return lrefResult;
	}

	private static DSSession GetSession()
		throws BigBangException
	{
		DSSession lrefResult;

        if (getSession() == null)
            return null;

        lrefResult = (DSSession)getSession().getAttribute("BigBang_DocuShare_Session");
        if (lrefResult == null)
        {
        	try
        	{
				lrefResult = GetServer().createSession("DocuShare", "scanner", "scanner");
			}
        	catch (BigBangException e)
        	{
        		throw e;
			}
        	catch (Throwable e)
        	{
        		throw new BigBangException(e.getMessage(), e);
			}
            getSession().setAttribute("BigBang_DocuShare_Session", lrefResult);
        }

        return lrefResult;
	}

	public static void LogOff()
	{
		HttpSession lrefHttpSession;
		DSSession lrefSession;
		DSServer lrefServer;

        if (getSession() == null)
            return;

        lrefHttpSession = getSession();
        if ( lrefHttpSession == null )
        	return;

        lrefSession = (DSSession)lrefHttpSession.getAttribute("BigBang_DocuShare_Session");
        if (lrefSession != null)
        {
	        lrefHttpSession.removeAttribute("BigBang_DocuShare_Session");
	        try { lrefSession.close(); } catch (Throwable e) {}
        }

        lrefServer = (DSServer)lrefHttpSession.getAttribute("BigBang_DocuShare_Server");
        if (lrefServer != null)
        {
	        lrefHttpSession.removeAttribute("BigBang_DocuShare_Server");
	        try { lrefServer.close(); } catch (Throwable e) {}
		}
	}

	public DocuShareItem[] getItems(String pstrFolder, boolean pbWithFolders)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSHandle lhFolder;
		DSCollection lobjFolder;
		DSObjectIterator i;
		DSObject lobjAux;
		ArrayList<DocuShareItem> larrFolders;
		ArrayList<DocuShareItem> larrItems;
		DocuShareItem lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( pbWithFolders )
			return getItemsContext(pstrFolder, false);

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		larrFolders = new ArrayList<DocuShareItem>();
		larrItems = new ArrayList<DocuShareItem>();

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
				{
					lobjTmp = new DocuShareItem();
					lobjTmp.directory = false;
					lobjTmp.handle = lobjAux.getHandle().toString();
					lobjTmp.desc = lobjAux.getTitle();
					larrItems.add(lobjTmp);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrFolders.addAll(larrItems);

		return larrFolders.toArray(new DocuShareItem[larrFolders.size()]);
	}

	public DocuShareItem[] getContext(String ownerId, String ownerTypeId)
		throws SessionExpiredException, BigBangException 
	{
		String lstrHandle;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

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
			throw new BigBangException(e.getMessage(), e);
		}

		return getItemsContext(lstrHandle, true);
	}

	public String getItem(String pstrItem)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSDocument lobjAux;
		DSContentElement[] larrAux;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

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
				lobjFile = new FileXfer(lobjAux.getSize(), lobjAux.getContentType(), lobjAux.getOriginalFileName(), larrAux[0]);
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
			throw new BigBangException(e.getMessage(), e);
		}

		lidKey = UUID.randomUUID();
		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);

		return lidKey.toString();
	}

//	public String getItemAsImage(String pstrItem)
//		throws SessionExpiredException, BigBangException
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
//		if ( Engine.getCurrentUser() == null )
//			throw new SessionExpiredException();
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
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		lidKey = UUID.randomUUID();
//		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
//
//		return lidKey.toString();
//	}

	public String getItemAsImage(String pstrItem)
		throws SessionExpiredException, BigBangException
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
//		if ( Engine.getCurrentUser() == null )
//			throw new SessionExpiredException();
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
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		lidKey = UUID.randomUUID();
//		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);

		return null;//lidKey.toString();
	}

	private DocuShareItem[] getItemsContext(String pstrFolder, boolean pbInjectTSR)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSHandle lhFolder;
		DSCollection lobjFolder;
		DSObjectIterator i;
		ArrayList<DocuShareItem> larrFolders;
		ArrayList<DocuShareItem> larrItems;
		DocuShareItem lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefSession = GetSession();
		if ( lrefSession == null )
			return null;

		larrFolders = new ArrayList<DocuShareItem>();
		larrItems = new ArrayList<DocuShareItem>();

		if ( pstrFolder == null )
		{
			lhFolder = new DSHandle("Collection-59066");
			pbInjectTSR = false;
		}
		else
			lhFolder = new DSHandle(pstrFolder);

		if ( pbInjectTSR )
		{
			lobjTmp = new DocuShareItem();
			lobjTmp.directory = true;
			lobjTmp.handle = "Collection-59066";
			lobjTmp.desc = "Temporary Scan Repository";
			larrFolders.add(lobjTmp);
		}

		try
		{
			lobjFolder = (DSCollection)lrefSession.getObject(lhFolder);
			i = lobjFolder.children(null);
			while ( i.hasNext() )
			{
				lobjTmp = getItemAux(i.nextObject());

				if ( lobjTmp.directory )
					larrFolders.add(lobjTmp);
				else
					larrItems.add(lobjTmp);
			}
		}
		catch (BigBangException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrFolders.addAll(larrItems);

		return larrFolders.toArray(new DocuShareItem[larrFolders.size()]);
	}

	private DocuShareItem getItemAux(DSObject lobjAux)
		throws BigBangException
	{
		DocuShareItem lobjTmp;

		lobjTmp = new DocuShareItem();
		lobjTmp.directory = (lobjAux instanceof DSCollection);
		lobjTmp.handle = lobjAux.getHandle().toString();
		try
		{
			lobjTmp.desc = lobjAux.getTitle();
		}
		catch (Throwable e)
		{
			throw new BigBangException("Erro a obter o nome de um item DocuShare.", e);
		}

		return lobjTmp;
	}
}
