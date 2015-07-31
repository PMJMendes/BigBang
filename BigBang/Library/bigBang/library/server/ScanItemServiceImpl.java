package bigBang.library.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.definitions.shared.ImageItem;
import bigBang.library.interfaces.ScanItemService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ScanItem;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.SysObjects.FileShareScanConnector;

public class ScanItemServiceImpl
	extends EngineImplementor
	implements ScanItemService
{
	private static final long serialVersionUID = 1L;

	public ScanItem[] getItems(String pstrFolder, boolean pbWithFolders)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			return translateItemsAux(FileShareScanConnector.getItems(pstrFolder, pbWithFolders));
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
			lobjFile = FileShareScanConnector.getItemAsFile(pstrItem);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lidKey = UUID.randomUUID();
		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);

		return lidKey.toString();
	}

	public ImageItem getItemAsImage(String pstrItem, int pageNumber)
		throws SessionExpiredException, BigBangException
	{
		ImageItem lobjResult;
		BufferedImage lobjImage;
		ByteArrayOutputStream lstreamOutput;
		byte[] larrBuffer;
		ByteArrayInputStream lstreamInput;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjResult = new ImageItem();
		lobjResult.id = pstrItem;
		lobjResult.pageNumber = pageNumber;

		try
		{
			lobjResult.pageCount = FileShareScanConnector.getItemPageCount(pstrItem);

			lobjImage = FileShareScanConnector.getItemAsImage(pstrItem, pageNumber);

			lstreamOutput = new ByteArrayOutputStream();
			ImageIO.write(lobjImage, "png", lstreamOutput);
			larrBuffer = lstreamOutput.toByteArray();
			lstreamInput = new ByteArrayInputStream(larrBuffer);
			lobjFile = new FileXfer(larrBuffer.length, "image/png", "pdfPage.png", lstreamInput);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lidKey = UUID.randomUUID();
		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
		lobjResult.imageId = lidKey.toString();

		return lobjResult;
	}

	private ScanItem[] translateItemsAux(File[] parrItems)
		throws BigBangException
	{
		ScanItem[] larrResult;
		int i;
		
		larrResult = new ScanItem[parrItems.length];
		for ( i = 0; i < parrItems.length; i++ )
			larrResult[i] = getItemAux(parrItems[i]);
		return larrResult;
	}

	private ScanItem getItemAux(File phFile)
		throws BigBangException
	{
		ScanItem lobjResult;

		lobjResult = new ScanItem();
		lobjResult.docushare = false;
		lobjResult.directory = phFile.isDirectory();
		lobjResult.handle = phFile.getAbsolutePath();
		try
		{
			if ( !phFile.isDirectory() )
			{
				lobjResult.fileName = phFile.getName();
				lobjResult.mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(phFile);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
