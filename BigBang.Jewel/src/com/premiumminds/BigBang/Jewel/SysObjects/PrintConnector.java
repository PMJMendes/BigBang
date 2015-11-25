package com.premiumminds.BigBang.Jewel.SysObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Media;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.docx4j.convert.out.pdf.viaXSLFO.Conversion;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.sun.star.text.XTextDocument;

public class PrintConnector
{
	private static class InnerPrintable
		implements Printable
	{
		private BufferedImage mimg;

		public InnerPrintable(BufferedImage pimg)
		{
			mimg = pimg;
		}

		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException
		{
			Graphics2D lrefGraphics;

			if ( pageIndex > 0 )
				return Printable.NO_SUCH_PAGE;

			lrefGraphics = (Graphics2D) graphics;
            lrefGraphics.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            lrefGraphics.drawImage(mimg, 0, 0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight(), null);
			return Printable.PAGE_EXISTS;
		}
	}

	public static void printDoc(FileXfer pobjFile, boolean pbStationary)
		throws BigBangJewelException
	{
		XTextDocument lobjDoc;
		FileXfer lobjPDFFile;
		PDDocument lobjPDF;

		try
		{
			lobjDoc = OOConnector.getDocFromBytes(pobjFile.getData());
			lobjPDFFile = OOConnector.getPDFFromOODoc(lobjDoc, pobjFile.getFileName());
			lobjPDF = PDDocument.load(new ByteArrayInputStream(lobjPDFFile.getData()));
			finalPrint(lobjPDF, pbStationary);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printODT(FileXfer pobjFile, boolean pbStationary)
		throws BigBangJewelException
	{
		XTextDocument lobjDoc;
		FileXfer lobjPDFFile;
		PDDocument lobjPDF;

		try
		{
			lobjDoc = OOConnector.getODTFromBytes(pobjFile.getData());
			lobjPDFFile = OOConnector.getPDFFromOODoc(lobjDoc, pobjFile.getFileName());
			lobjPDF = PDDocument.load(new ByteArrayInputStream(lobjPDFFile.getData()));
			finalPrint(lobjPDF, pbStationary);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printDocX(FileXfer pobjFile, boolean pbStationary)
		throws BigBangJewelException
	{
		WordprocessingMLPackage lobjDoc;
		ByteArrayOutputStream lobjOut;
		PDDocument lobjPDF;

		lobjOut = new ByteArrayOutputStream();
		try
		{
			lobjDoc =  (WordprocessingMLPackage)(new LoadFromZipNG()).get(new ByteArrayInputStream(pobjFile.getData()));
			lobjDoc.setFontMapper(new IdentityPlusMapper());
			(new Conversion(lobjDoc)).output(lobjOut, null);
			lobjPDF = PDDocument.load(new ByteArrayInputStream(lobjOut.toByteArray()));
			finalPrint(lobjPDF, pbStationary);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printPDF(FileXfer pobjFile, boolean pbStationary)
		throws BigBangJewelException
	{
		PDDocument lobjPDF;

		try
		{
			lobjPDF = PDDocument.load(new ByteArrayInputStream(pobjFile.getData()));
			finalPrint(lobjPDF, pbStationary);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printPNG(FileXfer pobjFile, boolean pbStationary)
		throws BigBangJewelException
	{
		ByteArrayInputStream lstream;
		BufferedImage limg;

		lstream = new ByteArrayInputStream(pobjFile.getData());

		try
		{
			limg = ImageIO.read(lstream);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		finalPrint(limg, pbStationary);
	}

	public static void printFile(FileXfer pobjFile, boolean pbStationary)
		throws BigBangJewelException
	{
		if ( "application/msword".equals(pobjFile.getContentType()) )
			printDoc(pobjFile, pbStationary);

		if ( "application/vnd.oasis.opendocument.text".equals(pobjFile.getContentType()) )
			printODT(pobjFile, pbStationary);

		if ( "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(pobjFile.getContentType()) )
			printDocX(pobjFile, pbStationary);

		if ( "application/pdf".equals(pobjFile.getContentType()) )
			printPDF(pobjFile, pbStationary);

		if ( "image/png".equals(pobjFile.getContentType()) )
			printPNG(pobjFile, pbStationary);
	}

	public static void printSetDetail(PrintSetDetail pobjDetail)
		throws BigBangJewelException
	{
		if ( pobjDetail.getFile() != null )
			printFile(pobjDetail.getFile(), false);
	}

	public static void printSetDocument(PrintSetDocument pobjDoc)
		throws BigBangJewelException
	{
		PrintSetDetail[] larrDetails;
		int i;

		printFile(pobjDoc.getFile(), true);

		larrDetails = pobjDoc.getCurrentDetails();
		for ( i = 0; i < larrDetails.length; i++ )
			printSetDetail(larrDetails[i]);
	}

	public static void printSet(PrintSet pobjSet)
		throws BigBangJewelException
	{
		PrintSetDocument[] larrDocs;
		int i;
		MasterDB ldb;

		larrDocs = pobjSet.getCurrentDocs();
		for ( i = 0; i < larrDocs.length; i++ )
		{
			if ( (pobjSet.getAt(PrintSet.I.PRINTEDON) == null) && (Boolean)larrDocs[i].getAt(PrintSetDocument.I.EXCLUDE) )
				continue;

			printSetDocument(larrDocs[i]);
		}

		if ( pobjSet.getAt(PrintSet.I.PRINTEDON) == null )
		{
			try
			{
				pobjSet.setAt(PrintSet.I.PRINTEDON, new Timestamp(new java.util.Date().getTime()));
				ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			try
			{
				pobjSet.SaveToDb(ldb);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
			try
			{
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private static void finalPrint(PDDocument pdoc, boolean pbStationary)
		throws BigBangJewelException
	{
		PrintService lrefSvc;
		PrinterJob lrefPJob;
		Media lrefMedia;
		HashPrintRequestAttributeSet lobjSet;

		lrefSvc = getPrinter();

		lrefPJob = PrinterJob.getPrinterJob();

		try
		{
			lrefPJob.setPrintService(lrefSvc);
			lrefPJob.setPageable(pdoc);

			lrefMedia = null;
			if ( pbStationary )
				lrefMedia = getTray(lrefSvc);
			if ( lrefMedia != null )
			{
				lobjSet = new HashPrintRequestAttributeSet();
				lobjSet.add(lrefMedia);
				lrefPJob.print(lobjSet);
			}
			else
				lrefPJob.print();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void finalPrint(final BufferedImage pimg, boolean pbStationary)
		throws BigBangJewelException
	{
		PrintService lrefSvc;
		PrinterJob lrefPJob;
		PageFormat lrefPF;
		Paper lobjPaper;
		Media lrefMedia;
		HashPrintRequestAttributeSet lobjSet;

		lrefSvc = getPrinter();

		lrefPJob = PrinterJob.getPrinterJob();

		try
		{
			lrefPJob.setPrintService(lrefSvc);
			lrefPF = lrefPJob.defaultPage();
			lobjPaper = (Paper)lrefPF.getPaper().clone();
			lobjPaper.setImageableArea(0, 0, lobjPaper.getWidth(), lobjPaper.getHeight());
			lrefPF.setPaper(lobjPaper);

			lrefPJob.setPrintable(new InnerPrintable(pimg), lrefPF);

			lrefMedia = null;
			if ( pbStationary )
				lrefMedia = getTray(lrefSvc);
			if ( lrefMedia != null )
			{
				lobjSet = new HashPrintRequestAttributeSet();
				lobjSet.add(lrefMedia);
				lrefPJob.print(lobjSet);
			}
			else
				lrefPJob.print();
		}
		catch (PrinterException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static PrintService getPrinter()
		throws BigBangJewelException
	{
		String lstrPrinter;
		PrintService[] larrServices;
		int i;

		try
		{
			lstrPrinter = (String)Engine.getUserData().get("Printer");

			larrServices = PrinterJob.lookupPrintServices();

			for ( i = 0; i < larrServices.length; i++ )
			{
				if (larrServices[i].getName().indexOf(lstrPrinter) != -1)
					return larrServices[i];
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		throw new BigBangJewelException("Impressora definida (" + lstrPrinter + ") não encontrada.");
	}

	private static Media getTray(PrintService prefSvc)
	{
		Media[] larrMedia;
		String lstrAux;
		int i;

		larrMedia = (Media[])prefSvc.getSupportedAttributeValues(Media.class, null, null);

		if ( larrMedia == null )
			return null;

		for ( i = 0; i < larrMedia.length; i++ )
		{
			lstrAux = larrMedia[i].toString().toLowerCase();
			if (lstrAux.contains("tray") && lstrAux.contains("3"))
			{
				return larrMedia[i];
			}
		}

		return null;
	}
}
