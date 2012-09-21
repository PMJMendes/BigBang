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

	public static void printDoc(FileXfer pobjFile)
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
			finalPrint(lobjPDF);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printODT(FileXfer pobjFile)
		throws BigBangJewelException
	{
		XTextDocument lobjDoc;
		FileXfer lobjPDFFile;
		PDDocument lobjPDF;
//		PropertyValue[] larrProps;
//		XPrintable lobjPrint;
//		XCloseable lobjClose;
//		int i;

//		larrProps = new PropertyValue[] {new PropertyValue()};
//		larrProps[0].Name = "Wait";
//		larrProps[0].Value = new Boolean(true);

		try
		{
			lobjDoc = OOConnector.getODTFromBytes(pobjFile.getData());
			lobjPDFFile = OOConnector.getPDFFromOODoc(lobjDoc, pobjFile.getFileName());
			lobjPDF = PDDocument.load(new ByteArrayInputStream(lobjPDFFile.getData()));
			finalPrint(lobjPDF);

//			lobjPrint = UnoRuntime.queryInterface(XPrintable.class, lobjDoc);
//			larrProps = lobjPrint.getPrinter();
//			for ( i = 0; i < larrProps.length; i++ )
//				if ( "Name".equals(larrProps[i].Name) )
//					larrProps[i].Value = "\\\\PM-DC\\4015";
//			lobjPrint.setPrinter(larrProps);
//			larrProps = new PropertyValue[] {new PropertyValue(), new PropertyValue()};
//			larrProps[0].Name = "Wait";
//			larrProps[0].Value = new Boolean(true);
//			larrProps[1].Name = "Pages";
//			larrProps[1].Value = "1-";
//			lobjPrint.print(larrProps);
//
//	        lobjClose = UnoRuntime.queryInterface(XCloseable.class, lobjDoc);
//	        if ( lobjClose == null )
//	        	lobjDoc.dispose();
//	        else
//	        	lobjClose.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printDocX(FileXfer pobjFile)
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
			finalPrint(lobjPDF);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printPDF(FileXfer pobjFile)
		throws BigBangJewelException
	{
		PDDocument lobjPDF;

		try
		{
			lobjPDF = PDDocument.load(new ByteArrayInputStream(pobjFile.getData()));
			finalPrint(lobjPDF);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static void printPNG(FileXfer pobjFile)
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

		finalPrint(limg);
	}

	public static void printFile(FileXfer pobjFile)
		throws BigBangJewelException
	{
		if ( "application/msword".equals(pobjFile.getContentType()) )
			printDoc(pobjFile);

		if ( "application/vnd.oasis.opendocument.text".equals(pobjFile.getContentType()) )
			printODT(pobjFile);

		if ( "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(pobjFile.getContentType()) )
			printDocX(pobjFile);

		if ( "application/pdf".equals(pobjFile.getContentType()) )
			printPDF(pobjFile);

		if ( "image/png".equals(pobjFile.getContentType()) )
			printPNG(pobjFile);
	}

	public static void printSetDetail(PrintSetDetail pobjDetail)
		throws BigBangJewelException
	{
		if ( pobjDetail.getFile() != null )
			printFile(pobjDetail.getFile());
	}

	public static void printSetDocument(PrintSetDocument pobjDoc)
		throws BigBangJewelException
	{
		PrintSetDetail[] larrDetails;
		int i;

		printFile(pobjDoc.getFile());

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
			printSetDocument(larrDocs[i]);

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

	private static void finalPrint(PDDocument pdoc)
		throws BigBangJewelException
	{
		PrinterJob lrefPJob;

		try
		{
			lrefPJob = getPrinter();

			pdoc.silentPrint(lrefPJob);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void finalPrint(final BufferedImage pimg)
		throws BigBangJewelException
	{
		PrinterJob lrefPJob;
		PageFormat lrefPF;
		Paper lobjPaper;

		lrefPJob = getPrinter();
		lrefPF = lrefPJob.defaultPage();
		lobjPaper = (Paper)lrefPF.getPaper().clone();
		lobjPaper.setImageableArea(0, 0, lobjPaper.getWidth(), lobjPaper.getHeight());
		lrefPF.setPaper(lobjPaper);

		lrefPJob.setPrintable(new InnerPrintable(pimg), lrefPF);

		try
		{
			lrefPJob.print();
		}
		catch (PrinterException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static PrinterJob getPrinter()
		throws BigBangJewelException
	{
		String lstrPrinter;
		PrinterJob lrefPJob;
		PrintService[] larrServices;
		boolean b;
		int i;

		try
		{
			lstrPrinter = (String)Engine.getUserData().get("Printer");

			lrefPJob = PrinterJob.getPrinterJob();
			larrServices = PrinterJob.lookupPrintServices();
			larrServices = PrinterJob.lookupPrintServices(); //JMMM: Chamada duplicada para ver se funciona à segunda

			b = false;
			for ( i = 0; i < larrServices.length; i++ )
			{
				if ( larrServices[i].getName().indexOf(lstrPrinter) != -1)
				{
					lrefPJob.setPrintService(larrServices[i]);
					b = true;
					break;
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( !b )
			throw new BigBangJewelException("Impressora definida (" + lstrPrinter + ") não encontrada.");
		
		return lrefPJob;
	}
}
