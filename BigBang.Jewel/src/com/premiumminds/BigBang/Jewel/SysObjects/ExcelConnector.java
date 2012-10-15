package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Enumeration;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
		R<Integer> max;
		int i;

		max = new R<Integer>(0);
		buildTable(pobjSheet, pobjSource, new R<Integer>(0), new R<Integer>(0), max);

		for ( i = 0; i < max.get(); i++ )
			pobjSheet.autoSizeColumn(i);
	}

	private static void buildTable(Sheet pobjSheet, Table pobjSource, R<Integer> row, R<Integer> col, R<Integer> max)
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
			buildRow(lobjRow, ltr, row, col, max);
			row.set(row.get() + 1);
		}
	}

	private static void buildRow(Row pobjRow, TR pobjSource, R<Integer> row, R<Integer> col, R<Integer> max)
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
			buildCell(lobjCell, ltd, row, col, max);
			col.set(col.get() + 1);
		}

		if ( col.get() > max.get() )
			max.set(col.get());

		col.set(llngStart);
	}

	private static void buildCell(Cell pobjCell, TD pobjSource, R<Integer> row, R<Integer> col, R<Integer> max)
	{
		GenericElement lobjAux;
		String lstr;
		Timestamp ldt;
		Double ldbl;
		CellStyle lst;

		lobjAux = pobjSource.getElement((String)pobjSource.keys().nextElement());

		if ( lobjAux instanceof Table )
		{
			buildTable(pobjCell.getSheet(), (Table)lobjAux, row, col, max);
			return;
		}

		lstr = lobjAux.toString();

		try
		{
			ldt = Timestamp.valueOf(lstr + " 00:00:00.0");
		}
		catch (Throwable e)
		{
			ldt = null;
		}
		if ( ldt != null )
		{
			lst = pobjCell.getSheet().getWorkbook().createCellStyle();
			lst.setDataFormat(pobjCell.getSheet().getWorkbook().getCreationHelper().createDataFormat()
					.getFormat("dd-mm-yyyy"));
			pobjCell.setCellValue(ldt);
			pobjCell.setCellStyle(lst);
			return;
		}

		if ( lstr.indexOf(',') >= 0 )
		{
			try
			{
				ldbl = Double.valueOf(lstr.replace(".", "").replace(",", "."));
			}
			catch (Throwable e)
			{
				ldbl = null;
			}
			if ( ldbl != null )
			{
				pobjCell.setCellValue(ldbl.doubleValue());
				return;
			}
		}

		pobjCell.setCellValue(lstr);
	}
}
