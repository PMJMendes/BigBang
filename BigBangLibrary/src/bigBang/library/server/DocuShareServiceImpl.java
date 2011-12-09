package bigBang.library.server;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.DocuShareItem;
import bigBang.library.shared.SessionExpiredException;

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

				if ( pbWithFolders )
				{
					if ( lobjAux instanceof DSCollection )
					{
						lobjTmp = new DocuShareItem();
						lobjTmp.directory = true;
						lobjTmp.handle = lobjAux.getHandle().toString();
						lobjTmp.desc = lobjAux.getTitle();
						larrFolders.add(lobjTmp);
					}
				}

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
			lobjFile = new FileXfer(lobjAux.getSize(), lobjAux.getContentType(), lobjAux.getOriginalFileName(), larrAux[0]);
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

	public String getItemAsImage(String pstrItem)
		throws SessionExpiredException, BigBangException
	{
		DSSession lrefSession;
		DSDocument lobjAux;
		DSContentElement[] larrAux;
		PDDocument ldocOrig;
		PDPage ldocPage;
		int llngRot;
		BufferedImage limgPage;
		AffineTransform lobjXForm;
		AffineTransformOp lobjOp;
		ByteArrayOutputStream lstreamOutput;
		byte[] larrBytes;
		ByteArrayInputStream lstreamInput;
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
			ldocOrig = PDDocument.load(larrAux[0]);
			larrAux[0].close();

			ldocPage = (PDPage)ldocOrig.getDocumentCatalog().getAllPages().get(0);
			llngRot = ldocPage.findRotation();
			limgPage = ldocPage.convertToImage(BufferedImage.TYPE_BYTE_GRAY, 300);
			ldocOrig.close();

			if ( llngRot != 0 )
			{
				lobjXForm = new AffineTransform();
				lobjXForm.translate(0.5*limgPage.getHeight(), 0.5*limgPage.getWidth());
				lobjXForm.quadrantRotate(llngRot/90);
				lobjXForm.translate(-0.5*limgPage.getWidth(), -0.5*limgPage.getHeight());
				lobjOp = new AffineTransformOp(lobjXForm, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				limgPage = lobjOp.filter(limgPage, null);
			}

			lstreamOutput = new ByteArrayOutputStream();
			ImageIO.write(limgPage, "jpg", lstreamOutput);

			larrBytes = lstreamOutput.toByteArray();
			lstreamInput = new ByteArrayInputStream(larrBytes);
			lobjFile = new FileXfer(larrBytes.length, "image/png", "pdfPage.png", lstreamInput);

//			lobjFile = getFirstImage(ldocPage);
//			ldocOrig.close();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}


		lidKey = UUID.randomUUID();
		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);

		return lidKey.toString();
	}

//	@SuppressWarnings("unchecked")
//	private static FileXfer getFirstImage(PDPage prefPage)
//		throws BigBangException
//	{
//		PDResources lobjResources;
//		Map<String, PDXObject> lmapObjects;
//		Iterator<String> i;
//		String lstrKey;
//		PDXObject lobjObject;
//		PDPixelMap lobjPNG;
//		PDJpeg lobjJPG;
//		PDCcitt lobjTIFF;
//		ByteArrayOutputStream lstreamOutput;
//		byte[] larrBytes;
//		ByteArrayInputStream lstreamInput;
//
//		try
//		{
//			lobjResources = prefPage.getResources();
//			lmapObjects = lobjResources.getXObjects();
//			i = lmapObjects.keySet().iterator();
//			while ( i.hasNext() )
//			{
//				lstrKey = i.next();
//				lobjObject = lmapObjects.get(lstrKey);
//				if ( lobjObject instanceof PDPixelMap )
//				{
//					lobjPNG = (PDPixelMap)lobjObject;
//					lstreamOutput = new ByteArrayOutputStream();
//					lobjPNG.write2OutputStream(lstreamOutput);
//					larrBytes = lstreamOutput.toByteArray();
//					lstreamInput = new ByteArrayInputStream(larrBytes);
//					return new FileXfer(larrBytes.length, "image/png", "pdfPage.png", lstreamInput);
//				}
//				if ( lobjObject instanceof PDJpeg)
//				{
//					lobjJPG = (PDJpeg)lobjObject;
//					lstreamOutput = new ByteArrayOutputStream();
//					lobjJPG.write2OutputStream(lstreamOutput);
//					larrBytes = lstreamOutput.toByteArray();
//					lstreamInput = new ByteArrayInputStream(larrBytes);
//					return new FileXfer(larrBytes.length, "image/jpeg", "pdfPage.jpg", lstreamInput);
//				}
//				if ( lobjObject instanceof PDCcitt)
//				{
//					lobjTIFF = (PDCcitt)lobjObject;
//					lstreamOutput = new ByteArrayOutputStream();
//					lobjTIFF.write2OutputStream(lstreamOutput);
//					larrBytes = lstreamOutput.toByteArray();
//					lstreamInput = new ByteArrayInputStream(larrBytes);
//					return new FileXfer(larrBytes.length, "image/tiff", "pdfPage.tif", lstreamInput);
//				}
//			}
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		throw new BigBangException("No image found in page");
//	}
}
