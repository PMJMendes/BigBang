package com.premiumminds.BigBang.Jewel.SysObjects;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

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
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.sun.star.text.XTextDocument;

public class PrintConnector
{
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
			lobjDoc = OOConnector.getDocFromBytes(pobjFile.getData());
			lobjPDFFile = OOConnector.getPDFFromDoc(lobjDoc, pobjFile.getFileName());
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

	public static void printFile(FileXfer pobjFile)
		throws BigBangJewelException
	{
		if ( "application/vnd.oasis.opendocument.text".equals(pobjFile.getContentType()) )
			printODT(pobjFile);

		if ( "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(pobjFile.getContentType()) )
			printDocX(pobjFile);

		if ( "application/pdf".equals(pobjFile.getContentType()) )
			printPDF(pobjFile);
	}

	public static void printSetDocument(PrintSetDocument pobjDoc)
		throws BigBangJewelException
	{
		printFile(pobjDoc.getFile());
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
		PrintService[] larrServices;
		boolean b;
		int i;

		try
		{
			lrefPJob = PrinterJob.getPrinterJob();
			larrServices = PrinterJob.lookupPrintServices();

			b = false;
			for ( i = 0; i < larrServices.length; i++ )
			{
				if ( larrServices[i].getName().indexOf((String)Engine.getUserData().get("Printer")) != -1)
				{
					lrefPJob.setPrintService(larrServices[i]);
					b = true;
					break;
				}
			}

			if ( b )
				pdoc.silentPrint(lrefPJob);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( !b )
			throw new BigBangJewelException("Impressora definida não encontrada.");
	}
}
