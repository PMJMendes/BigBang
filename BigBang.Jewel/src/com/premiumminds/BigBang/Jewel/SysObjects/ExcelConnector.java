package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Enumeration;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.R;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class ExcelConnector
{
	public static FileXfer buildExcel(GenericElement[] parrSource)
		throws BigBangJewelException
	{
		Workbook lobjWBook;
    	ByteArrayOutputStream lstream;

		if ( parrSource == null )
			return null;

        lobjWBook = new XSSFWorkbook();
        buildBook(lobjWBook, parrSource);
        lstream = new ByteArrayOutputStream();
        try
        {
			lobjWBook.write(lstream);
            return new FileXfer((int)lstream.size(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            		"report.xlsx", new ByteArrayInputStream(lstream.toByteArray()));
		}
        catch (Exception e)
        {
        	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static void buildBook(Workbook pobjWBook, GenericElement[] parrSource)
	{
		int i;
		Table ltbl;
		TR ltr;
		TD ltd;
		GenericElement lobjAux;

		for ( i = 0; i < parrSource.length; i++ )
		{
			ltbl = (Table)parrSource[i];
			ltr = (TR)ltbl.getElement((String)ltbl.keys().nextElement());
			ltd = (TD)ltr.getElement((String)ltr.keys().nextElement());
			lobjAux = ltd.getElement((String)ltd.keys().nextElement());
            buildSheet(pobjWBook.createSheet(lobjAux.toString()), ltbl);
		}
		
	}

	private static void buildSheet(Sheet pobjSheet, Table pobjSource)
	{
		buildTable(pobjSheet, pobjSource, new R<Integer>(0), new R<Integer>(0));
	}

	private static void buildTable(Sheet pobjSheet, Table pobjSource, R<Integer> row, R<Integer> col)
	{
		Enumeration<?> i;
		Row lobjRow;
		TR ltr;

		i = pobjSource.keys();
		while ( i.hasMoreElements() )
		{
            lobjRow = pobjSheet.getRow(row.get());
            if ( lobjRow == null )
                lobjRow = pobjSheet.createRow(row.get());
			ltr = (TR)pobjSource.getElement((String)i.nextElement());
			buildRow(lobjRow, ltr, row, col);
			row.set(row.get() + 1);
		}
	}

	private static void buildRow(Row pobjRow, TR pobjSource, R<Integer> row, R<Integer> col)
	{
		int llngStart;
		Enumeration<?> i;
		Cell lobjCell;
		TD ltd;

		llngStart = col.get();

		i = pobjSource.keys();
		while ( i.hasMoreElements() )
		{
			lobjCell = pobjRow.getCell(col.get());
            if ( lobjCell == null )
            	lobjCell = pobjRow.createCell(col.get());
			ltd = (TD)pobjSource.getElement((String)i.nextElement());
			buildCell(lobjCell, ltd, row, col);
			col.set(col.get() + 1);
		}

		col.set(llngStart);
	}

	private static void buildCell(Cell pobjCell, TD pobjSource, R<Integer> row, R<Integer> col)
	{
		GenericElement lobjAux;

		lobjAux = pobjSource.getElement((String)pobjSource.keys().nextElement());

		if ( lobjAux instanceof Table )
			buildTable(pobjCell.getSheet(), (Table)lobjAux, row, col);
		else
			pobjCell.setCellValue(lobjAux.toString());
	}
}
