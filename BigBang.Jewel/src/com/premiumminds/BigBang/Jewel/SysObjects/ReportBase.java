package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Template;
import com.sun.star.container.XIndexAccess;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTablesSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

public abstract class ReportBase
{
	protected abstract UUID GetTemplateID();
	public abstract FileXfer Generate() throws BigBangJewelException;

/*
Este código assume bué coisas:
1. As tabelas que existem têm já e apenas uma linha de header e uma linha de dados. As células de dados têm um ? dentro.
2. Os parágrafos com parâmetros para substituir têm que ser escritos todos de uma vez, para caberem dentro de uma "run".
 */

	protected FileXfer Generate(HashMap<String, String> parrContents, String[][][] parrTables)
		throws BigBangJewelException
	{
		Template lobjTemplate;
		FileXfer lobjFile;

		lobjTemplate = Template.GetInstance(Engine.getCurrentNameSpace(), GetTemplateID());
		lobjFile = lobjTemplate.getFile();
		
		if ( "application/msword".equals(lobjFile.getContentType()) )
			return GenerateAsDoc(lobjFile, parrContents, parrTables);

		if ( "application/vnd.oasis.opendocument.text".equals(lobjFile.getContentType()) )
			return GenerateAsODT(lobjFile, parrContents, parrTables);

		if ( "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(lobjFile.getContentType()) )
			return GenerateAsDocX(lobjFile, parrContents, parrTables);

		throw new BigBangJewelException("Unrecognized template format.");
	}

	private FileXfer GenerateAsDoc(FileXfer lobjFile, HashMap<String, String> parrContents, String[][][] parrTables)
		throws BigBangJewelException
	{
		XTextDocument lobjDoc;
		XIndexAccess larrTables;
		XTextTable lobjTable;
		XTableRows larrRows;
		XCellRange larrCells;
		XCell lobjCell;
		XText lobjText;
		XReplaceable lobjReplacer;
		XReplaceDescriptor lobjDescriptor;
		int i, j, k;

		lobjDoc = OOConnector.getDocFromBytes(lobjFile.getData());
		try
		{
			if ( parrTables != null )
			{
				larrTables = UnoRuntime.queryInterface(XIndexAccess.class,
						UnoRuntime.queryInterface(XTextTablesSupplier.class, lobjDoc).getTextTables());
	
				for ( i = 0; (i < parrTables.length) && (i < larrTables.getCount()); i++ )
				{
					lobjTable = UnoRuntime.queryInterface(XTextTable.class, larrTables.getByIndex(i));
					larrRows = lobjTable.getRows();
					if ( parrTables[i].length >= larrRows.getCount() )
						larrRows.insertByIndex(larrRows.getCount(), parrTables[i].length - larrRows.getCount() + 1);
					larrCells = UnoRuntime.queryInterface(XCellRange.class, lobjTable);
					for ( j = 0; j < parrTables[i].length; j++ )
					{
						for ( k = 0; k < parrTables[i][j].length; k++ )
						{
							lobjCell = larrCells.getCellByPosition(k, j + 1);
							lobjText = UnoRuntime.queryInterface(XText.class, lobjCell);
							lobjText.setString(parrTables[i][j][k]);
						}
					}
				}
			}

			if ( parrContents != null )
			{
				lobjReplacer = UnoRuntime.queryInterface(XReplaceable.class, lobjDoc);
				lobjDescriptor = lobjReplacer.createReplaceDescriptor();

				for ( String lstrKey : parrContents.keySet() )
				{
					lobjDescriptor.setSearchString("{{" + lstrKey + "}}");
					lobjDescriptor.setReplaceString(parrContents.get(lstrKey));
					lobjReplacer.replaceAll(lobjDescriptor);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return OOConnector.getDocFromOODoc(lobjDoc, lobjFile.getFileName());
	}

	private FileXfer GenerateAsODT(FileXfer lobjFile, HashMap<String, String> parrContents, String[][][] parrTables)
		throws BigBangJewelException
	{
		XTextDocument lobjDoc;
		XIndexAccess larrTables;
		XTextTable lobjTable;
		XTableRows larrRows;
		XCellRange larrCells;
		XCell lobjCell;
		XText lobjText;
		XReplaceable lobjReplacer;
		XReplaceDescriptor lobjDescriptor;
		int i, j, k;

		lobjDoc = OOConnector.getODTFromBytes(lobjFile.getData());
		try
		{
			if ( parrTables != null )
			{
				larrTables = UnoRuntime.queryInterface(XIndexAccess.class,
						UnoRuntime.queryInterface(XTextTablesSupplier.class, lobjDoc).getTextTables());
	
				for ( i = 0; (i < parrTables.length) && (i < larrTables.getCount()); i++ )
				{
					lobjTable = UnoRuntime.queryInterface(XTextTable.class, larrTables.getByIndex(i));
					larrRows = lobjTable.getRows();
					if ( parrTables[i].length >= larrRows.getCount() )
						larrRows.insertByIndex(larrRows.getCount(), parrTables[i].length - larrRows.getCount() + 1);
					larrCells = UnoRuntime.queryInterface(XCellRange.class, lobjTable);
					for ( j = 0; j < parrTables[i].length; j++ )
					{
						for ( k = 0; k < parrTables[i][j].length; k++ )
						{
							lobjCell = larrCells.getCellByPosition(k, j + 1);
							lobjText = UnoRuntime.queryInterface(XText.class, lobjCell);
							lobjText.setString(parrTables[i][j][k]);
						}
					}
				}
			}

			if ( parrContents != null )
			{
				lobjReplacer = UnoRuntime.queryInterface(XReplaceable.class, lobjDoc);
				lobjDescriptor = lobjReplacer.createReplaceDescriptor();

				for ( String lstrKey : parrContents.keySet() )
				{
					lobjDescriptor.setSearchString("{{" + lstrKey + "}}");
					lobjDescriptor.setReplaceString(parrContents.get(lstrKey));
					lobjReplacer.replaceAll(lobjDescriptor);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return OOConnector.getPDFFromOODoc(lobjDoc, lobjFile.getFileName());
	}

	private FileXfer GenerateAsDocX(FileXfer pobjFile, HashMap<String, String> parrContents, String[][][] parrTables)
		throws BigBangJewelException
	{
		WordprocessingMLPackage lobjDoc;
		MainDocumentPart lobjMain;
		List<java.lang.Object> larrTexts;
		Text lobjText;
		List<java.lang.Object> larrTables;
		Tbl lobjTable;
		List<java.lang.Object> larrRows;
		Tr lobjRow;
		List<java.lang.Object> larrCells;
		Tc lobjCell;
		P lobjParagraph;
		R lobjRun;
		ByteArrayOutputStream lobjOut;
		byte[] larrBytes;
		FileXfer lobjResult;
		boolean b;
		int i, j, k;

		try
		{
			lobjDoc =  (WordprocessingMLPackage)(new LoadFromZipNG()).get(new ByteArrayInputStream(pobjFile.getData()));
			lobjMain = lobjDoc.getMainDocumentPart();

			if ( parrTables != null )
			{
				larrTables = lobjMain.getJAXBNodesViaXPath("//w:tbl", false);
				for ( i = 0; (i < parrTables.length) && (i < larrTables.size()); i++ )
				{
					if ( parrTables[i] == null )
						continue;

					lobjTable = (Tbl)((JAXBElement<?>)larrTables.get(i)).getValue();
					larrRows = lobjTable.getContent();
					b = true;
					for ( j = 0; j < parrTables[i].length; j++ )
					{
						if ( parrTables[i][j] == null )
							continue;
						if ( b )
						{
							b = false;
							continue;
						}

						lobjRow = (Tr)XmlUtils.deepCopy(larrRows.get(1));
						larrRows.add(lobjRow);
					}
					for ( j = 0; j < parrTables[i].length; j++ )
					{
						if ( parrTables[i][j] == null )
							continue;

						lobjRow = (Tr)larrRows.get(j + 1);
						larrCells = lobjRow.getContent();
						for ( k = 0; (k < parrTables[i][j].length) && (k < larrCells.size()); k++ )
						{
							lobjCell = (Tc)((JAXBElement<?>)larrCells.get(k)).getValue();
							lobjParagraph = (P)lobjCell.getContent().get(0);
							lobjRun = (R)lobjParagraph.getContent().get(0);
							lobjText = (Text)((JAXBElement<?>)lobjRun.getContent().get(0)).getValue();
							lobjText.setValue(parrTables[i][j][k] == null ? "" : parrTables[i][j][k]);
						}
					}
				}
			}

			if ( parrContents != null )
			{
				larrTexts = lobjMain.getJAXBNodesViaXPath("//w:t", true);
				for ( i = 0; i < larrTexts.size(); i++ )
				{
					lobjText = (Text)((JAXBElement<?>)larrTexts.get(i)).getValue();
					lobjText.setValue(doReplace(lobjText.getValue(), parrContents));
				}
			}

			lobjOut = new ByteArrayOutputStream();
			(new SaveToZipFile(lobjDoc)).save(lobjOut);

			larrBytes = lobjOut.toByteArray();
			lobjResult = new FileXfer(larrBytes.length, pobjFile.getContentType(), pobjFile.getFileName(),
					new ByteArrayInputStream(larrBytes));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}

	private String doReplace(String pstrText, HashMap<String, String> parrContents)
	{
		int i, j;

		for ( i = pstrText.indexOf("{{"); i >= 0; i = pstrText.indexOf("{{") )
		{
			j = pstrText.indexOf("}}");
			if ( j < 0 )
			{
				pstrText = pstrText.substring(0, i) + pstrText.substring(i + 2);
				continue;
			}
			if ( j < i )
			{
				pstrText = pstrText.substring(0, j) + pstrText.substring(j + 2);
				continue;
			}
			pstrText = pstrText.substring(0, i) + parrContents.get(pstrText.substring(i + 2, j)) + pstrText.substring(j + 2);
		}

		return pstrText;
	}
}
