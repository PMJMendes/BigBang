package com.premiumminds.BigBang.Jewel.SysObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.activation.MimetypesFileTypeMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class FileShareScanConnector
{
	private static String SCAN_ROOT_NAME = "Collection-59066";
	private static String REMOVED_ROOT_NAME = "Collection-116558";
	private static File SCAN_ROOT = new File(SCAN_ROOT_NAME);

	public static File[] getItems(String pstrFolder, boolean pbWithFolders)
		throws BigBangJewelException
	{
		File lhFolder;
		File[] larrResult;

		if ( pbWithFolders )
			return getItemsContext(pstrFolder, false);

		if ( pstrFolder == null )
			lhFolder = SCAN_ROOT;
		else
			lhFolder = new File(pstrFolder);

		larrResult = lhFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.isDirectory();
			}
		});

		Arrays.sort(larrResult, new Comparator<File>()
		{
			public int compare(File o1, File o2)
			{
				try
				{
					return o1.getName().compareTo(o2.getName());
				}
				catch (Throwable e)
				{
					return 0;
				}
			}
		});

		return larrResult;
	}

	public static String getItemTitle(String pstrItem)
		throws BigBangJewelException
	{
		try
		{
			return new File(pstrItem).getCanonicalFile().getName();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileXfer getItemAsFile(String pstrItem)
		throws BigBangJewelException
	{
		File lhFile;
		String lstrMime;
		long llngLen;
		FileInputStream lstream;
		FileXfer lobjFile;

		try
		{
			lhFile = new File(pstrItem).getCanonicalFile();
			lstrMime = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(lhFile);
			llngLen = lhFile.length();

			lstream = new FileInputStream(lhFile);
			try
			{
				lobjFile = new FileXfer((int)llngLen, lstrMime, lhFile.getName(), lstream);
			}
			catch (Throwable e1)
			{
				try { lstream.close(); } catch (Throwable e2) {}
				throw e1;
			}
			lstream.close();
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
		FileInputStream lstream;
		PDDocument lobjDocument;
		int llngResult;

		try
		{
			lstream = new FileInputStream(new File(pstrItem).getCanonicalFile());
			try
			{
				lobjDocument = PDDocument.load(lstream);
			}
			catch (Throwable e1)
			{
				try { lstream.close(); } catch (Throwable e2) {}
				throw e1;
			}
			try
			{
				lstream.close();
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
		FileInputStream lstream;
		PDDocument lobjDocument;
		PDPage lobjPage;
		int llngRot;
		BufferedImage lobjImage;
		AffineTransform lobjXForm;
		AffineTransformOp lobjOp;

		try
		{
			lstream = new FileInputStream(new File(pstrItem).getCanonicalFile());
			try
			{
				lobjDocument = PDDocument.load(lstream);
			}
			catch (Throwable e1)
			{
				try { lstream.close(); } catch (Throwable e2) {}
				throw e1;
			}
			try
			{
				lstream.close();
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
        FileOutputStream lstream;

		try
		{
			lstream = new FileOutputStream(pstrLocation + "\\" + new File(pstrTitle).getName()/*, FileMode.Create, FileAccess.Write*/);
	        lstream.write(pobjFile.getData());
	        lstream.flush();
	        lstream.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void moveItem(String pstrItem, String pstrFrom, String pstrTo, boolean pbIsUndo)
		throws BigBangJewelException
	{
		String lstrName;
		String lstrFrom;
		String lstrTo;

		lstrName = new File(pstrItem).getName();
		lstrFrom = ( pstrFrom != null ? pstrFrom :
				( pbIsUndo ? REMOVED_ROOT_NAME : SCAN_ROOT_NAME )) + "\\" + lstrName;
		lstrTo = ( pstrTo != null ? pstrTo :
				( pbIsUndo ? SCAN_ROOT_NAME : REMOVED_ROOT_NAME )) + "\\" + lstrName;

		try
		{
			new File(lstrFrom).renameTo(new File(lstrTo));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void deleteItem(String pstrItem)
		throws BigBangJewelException
	{
		try
		{
			new File(pstrItem).delete();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static File[] getItemsContext(String pstrFolder, boolean pbInjectTSR)
		throws BigBangJewelException
	{
		File lhFolder;
		File[] larrResult;
		ArrayList<File> larrItems;

		if ( pstrFolder == null )
		{
			lhFolder = SCAN_ROOT;
			pbInjectTSR = false;
		}
		else
			lhFolder = new File(pstrFolder);

		larrResult = lhFolder.listFiles();

		if ( pbInjectTSR )
		{
			larrItems = new ArrayList<File>(Arrays.asList(larrResult));
			larrItems.add(SCAN_ROOT);
			larrResult = larrItems.toArray(new File[larrItems.size()]);
		}

		Arrays.sort(larrResult, new Comparator<File>()
		{
			public int compare(File o1, File o2)
			{
				try {
					if ( SCAN_ROOT.equals(o1) )
					{
						if ( SCAN_ROOT.equals(o2) )
							return 0;
						return -1;
					}
					if ( SCAN_ROOT.equals(o2) )
						return 1;

					if ( o1.isDirectory() )
					{
						if ( o2.isDirectory() )
							return o1.getName().compareTo(o2.getName());
						return -1;
					}
					if ( o2.isDirectory() )
						return 1;

					return o1.getName().compareTo(o2.getName());
				}
				catch (Throwable e)
				{
					return 0;
				}
			}
		});

		return larrResult;
	}
}
