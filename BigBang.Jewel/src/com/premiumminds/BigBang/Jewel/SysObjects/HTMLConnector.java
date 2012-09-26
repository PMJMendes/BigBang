package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;

import org.apache.ecs.Document;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class HTMLConnector
{
	public static FileXfer buildHTML(String[] parrSource)
		throws BigBangJewelException
	{
		Document ldoc;
		Table ltbl;
		TR ltr;
		TD ltd;
		Div ldiv;
		int i;
		String lstrResult;
		FileXfer lobjResult;

		ldoc = new Document();

		ltbl = new Table();
		ltbl.setCellPadding(0);
		ltbl.setCellSpacing(0);
		ldoc.appendBody(ltbl);

		for ( i = 0; i < parrSource.length; i++ )
		{
			ltr = new TR();
			ltbl.addElementToRegistry(ltr);

			ltd = new TD();
			ltd.setAlign("left");
			ltd.setVAlign("top");
			ltr.addElementToRegistry(ltd);

			ldiv = new Div();
			ldiv.setStyle("width: 100%; margin-bottom: 50px;");
			ltd.addElementToRegistry(ldiv);

			ldiv.addElementToRegistry(parrSource[i]);
		}

		lstrResult = ldoc.toString();

		try
		{
			lobjResult = new FileXfer(lstrResult.length(), "text/html", "report.html", new ByteArrayInputStream(lstrResult.getBytes("UTF-8")));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}
}
