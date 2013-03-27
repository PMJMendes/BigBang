package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.apache.ecs.html.Div;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Constants.TypeDefGUIDs;

public class ReportBuilder
{
	public static String BuildValue(UUID pidType, java.lang.Object pobjValue)
	{
		if ( pobjValue == null )
			return "?";

        if (TypeDefGUIDs.T_String.equals(pidType))
        	return (String)pobjValue;

        if (TypeDefGUIDs.T_Integer.equals(pidType))
        	return ((Integer)pobjValue).toString();

        if (TypeDefGUIDs.T_Decimal.equals(pidType))
        	return String.format("%,.2f", ((BigDecimal)pobjValue));

        if (TypeDefGUIDs.T_Boolean.equals(pidType))
        {
        	if ( (Boolean)pobjValue )
        		return "Sim";
        	else
        		return "Não";
        }

        if (TypeDefGUIDs.T_ObjRef.equals(pidType))
			return "<<referência>>";

        if (TypeDefGUIDs.T_ValueRef.equals(pidType))
        	return "<<referência>>";

        if (TypeDefGUIDs.T_Date.equals(pidType))
        	return ((Timestamp)pobjValue).toString().substring(0, 10);

        if (TypeDefGUIDs.T_Binary.equals(pidType))
        	return "<<dados>>";

        if (TypeDefGUIDs.T_Passwd.equals(pidType))
        	return "";

		return "?";
	}

	public static TD buildCell(java.lang.Object pobjValue, UUID pidType)
	{
		return buildCell(pobjValue, pidType, false);
	}

	public static TD buildCell(java.lang.Object pobjValue, UUID pidType, boolean pbRight)
	{
		Div ldiv;
		TD ltd;

		ldiv = new Div(BuildValue(pidType, pobjValue));
		ldiv.setStyle("width:inherit;" + (pbRight ? "text-align:right;" : "") + 
				((TypeDefGUIDs.T_Decimal.equals(pidType) && (pobjValue != null) && (((BigDecimal)pobjValue).signum() < 0)) ?
						"color:red;" : "") );

		ltd = new TD();
		ltd.addElementToRegistry(ldiv);
		if ( pbRight )
			ltd.setAlign("right");

		return ltd;
	}

	public static TD buildHeaderCell(String pstrTitle)
	{
		return new TD(pstrTitle);
	}

	public static TR buildRow(TD[] parrCells)
	{
		TR lrow;
		int i;

		lrow = new TR();

		for ( i = 0; i < parrCells.length; i++ )
			lrow.addElementToRegistry(parrCells[i]);

		return lrow;
	}

	public static Table buildTable(TR[] parrRows)
	{
		Table ltbl;
		int i;

		ltbl = new Table();
		ltbl.setCellPadding(0);
		ltbl.setCellSpacing(0);

		for ( i = 0; i < parrRows.length; i++ )
			ltbl.addElementToRegistry(parrRows[i]);

		return ltbl;
	}

	public static void styleCell(TD pcell, boolean pbAddTop, boolean pbAddLeft)
	{
		pcell.setStyle("overflow:hidden;white-space:nowrap;padding-left:5px;padding-right:5px;" +
				(pbAddTop ? "border-top:1px solid #3f6d9d;" : "") +
				(pbAddLeft ? "border-left:1px solid #3f6d9d;" : ""));

	}

	public static void styleInnerContainer(TD pcell)
	{
		pcell.setStyle("border-top:1px solid #3f6d9d;");
	}

	public static void styleRow(TR prow, boolean pbIsHeader)
	{
		prow.setStyle((pbIsHeader ? "height:35px;background:#8bb4de; font-weight:bold;" : "height:30px;"));
	}

	public static void styleTable(Table ptbl, boolean pbIsInner)
	{
		ptbl.setStyle("background-color:white;" + (pbIsInner ? "" : "border:1px solid #3f6d9d;"));
	}

	public static TR constructDualHeaderRowCell(String pstrHeader)
	{
		TD lcell;
		TR lrow;

		lcell = buildHeaderCell(pstrHeader);
		lcell.setColSpan(2);
		styleCell(lcell, false, false);
		lrow = ReportBuilder.buildRow(new TD[] {lcell});
		styleRow(lrow, true);

		return lrow;
	}

	public static TR constructDualRow(String pstrHeader, java.lang.Object pobjValue, UUID pidType, boolean pbRightAlign)
	{
		TD[] larrCells;
		TR lrow;

		larrCells = new TD[2];
		larrCells[0] = buildHeaderCell(pstrHeader);
		larrCells[0].setWidth("1px");
		styleCell(larrCells[0], true, false);
		larrCells[1] = buildCell(pobjValue, pidType);
		styleCell(larrCells[1], true, true);
		if ( pbRightAlign )
			larrCells[1].setAlign("right");
		lrow = buildRow(larrCells);
		styleRow(lrow, false);

		return lrow;
	}
}
