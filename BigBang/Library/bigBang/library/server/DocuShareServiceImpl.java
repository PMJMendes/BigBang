package bigBang.library.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.DocuShareItem;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.SysObjects.DocuShareConnector;
import com.xerox.docushare.DSObject;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.object.DSDocument;

public class DocuShareServiceImpl
	extends EngineImplementor
	implements DocuShareService
{
	private static final long serialVersionUID = 1L;

	public static void LogOff()
	{
		DocuShareConnector.LogOff();
	}

	public DocuShareItem[] getItems(String pstrFolder, boolean pbWithFolders)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			return translateItemsAux(DocuShareConnector.getItems(pstrFolder, pbWithFolders));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public DocuShareItem[] getContext(String ownerId, String ownerTypeId)
		throws SessionExpiredException, BigBangException 
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			return translateItemsAux(DocuShareConnector.getContext(ownerId, ownerTypeId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public String getItem(String pstrItem)
		throws SessionExpiredException, BigBangException
	{
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = DocuShareConnector.getItemAsFile(pstrItem);
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
		BufferedImage lobjImage;
		ByteArrayOutputStream lstreamOutput;
		byte[] larrBuffer;
		ByteArrayInputStream lstreamInput;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjImage = DocuShareConnector.getItemAsImage(pstrItem);

			lstreamOutput = new ByteArrayOutputStream();
			ImageIO.write(lobjImage, "png", lstreamOutput);
//			ImageIO.write(lobjImage, "jpg", lstreamOutput);
			larrBuffer = lstreamOutput.toByteArray();
			lstreamInput = new ByteArrayInputStream(larrBuffer);
			lobjFile = new FileXfer(larrBuffer.length, "image/png", "pdfPage.png", lstreamInput);
//			lobjFile = new FileXfer(larrBuffer.length, "image/jpeg", "pdfPage.jpg", lstreamInput);
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
//
//		return lidKey.toString();
//	}

	private DocuShareItem[] translateItemsAux(DSObject[] parrItems)
		throws BigBangException
	{
		DocuShareItem[] larrResult;
		int i;
		
		larrResult = new DocuShareItem[parrItems.length];
		for ( i = 0; i < parrItems.length; i++ )
			larrResult[i] = getItemAux(parrItems[i]);
		return larrResult;
	}

	private DocuShareItem getItemAux(DSObject pobjDSItem)
		throws BigBangException
	{
		DocuShareItem lobjResult;

		lobjResult = new DocuShareItem();
		lobjResult.directory = (pobjDSItem instanceof DSCollection);
		lobjResult.handle = pobjDSItem.getHandle().toString();
		try
		{
			lobjResult.desc = pobjDSItem.getTitle();
			if ( pobjDSItem instanceof DSDocument )
			{
				lobjResult.fileName = ((DSDocument)pobjDSItem).getOriginalFileName();
				lobjResult.mimeType = ((DSDocument)pobjDSItem).getContentType();
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
