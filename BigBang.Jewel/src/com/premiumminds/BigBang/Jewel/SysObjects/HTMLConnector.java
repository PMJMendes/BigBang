package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;

import org.apache.ecs.Document;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Style;

import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class HTMLConnector
{
	public static FileXfer buildHTML(String[] parrSource)
		throws BigBangJewelException
	{
		Document ldoc;
		Style lstyle;
		Div ldiv;
		int i;
		byte[] larrBytes;
		FileXfer lobjResult;

		ldoc = new Document();
		ldoc.appendHead("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");

		lstyle = new Style();
		lstyle.addElementToRegistry("body, table, td, div, select {font-family: Arial Unicode MS, Arial, sans-serif; font-size: xx-small;}");
		lstyle.addElementToRegistry("tr {height: 20px !important;}");
		ldoc.appendHead(lstyle);

		for ( i = 0; i < parrSource.length; i++ )
		{
			ldiv = new Div();
			ldiv.setStyle("width: 100%; margin-bottom: 50px;" + (((i > 0) && (parrSource.length > 2)) ? "page-break-before:always;" : ""));
			ldoc.appendBody(ldiv);

			ldiv.addElementToRegistry(parrSource[i]);
		}

		try
		{
			// The next 2 lines became mandatory after a problem with printing from chrome 
			// https://stackoverflow.com/questions/44586986/why-is-google-chrome-not-printing-table-and-cell-borders-and-cell-background-co
			String docString = ldoc.toString();
			docString = docString.replaceFirst("<body>", "<body onload=\"window.print();\">");
			larrBytes = docString.getBytes("UTF-8");
			lobjResult = new FileXfer(larrBytes.length, "text/html", "report.html", new ByteArrayInputStream(larrBytes));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
