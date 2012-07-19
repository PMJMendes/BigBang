package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.UUID;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;

public class MediatorAccountingMap
	extends TransactionMapBase
{
    public static MediatorAccountingMap GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingMap), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MediatorAccountingMap GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingMap),
					prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

	public UUID getSubObjectType()
	{
		return Constants.ObjID_MediatorAccountingDetail;
	}

	public void appendPreHTML(StringBuilder pstrBuffer)
		throws BigBangJewelException
	{
	}

	public void appendMidHTML(StringBuilder pstrBuffer)
		throws BigBangJewelException
	{
		pstrBuffer.append("<td style=\"white-space:nowrap;padding-left:5px;border-left:1px solid #3f6d9d;border-bottom:1px solid #3f6d9d;\">")
				.append("Comissão")
				.append("</td> ");

		pstrBuffer.append("<td style=\"white-space:nowrap;padding-left:5px;border-left:1px solid #3f6d9d;border-bottom:1px solid #3f6d9d;\">")
				.append("Retrocessão")
				.append("</td> ");
	}

	public void appendPostHTML(StringBuilder pstrBuffer)
		throws BigBangJewelException
	{
	}

	public TR[] buildTable(int plngNumber)
		throws BigBangJewelException
	{
		TR[] larrAux;
		TR[] larrRows;
		int i;

		larrAux = super.buildTable(plngNumber);

		larrRows = new TR[larrAux.length + 1];
		larrRows[0] = larrAux[0];

		larrRows[1] = ReportBuilder.constructDualRow("Agente",
				Mediator.GetInstance(getNameSpace(), (UUID)getAt(TransactionMapBase.I.OWNER)).getLabel(), TypeDefGUIDs.T_String);

		for ( i = 1; i < larrAux.length; i++ )
			larrRows[i + 1] = larrAux[i];

		return larrRows;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrAux;
		TD[] larrCells;
		int i;

		larrAux = super.buildInnerHeaderRow();
		i = larrAux.length;

		larrCells = Arrays.copyOf(larrAux, i + 2);

		larrCells[i] = ReportBuilder.buildHeaderCell("Comissão");
		ReportBuilder.styleCell(larrCells[i], false, true);

		larrCells[i] = ReportBuilder.buildHeaderCell("Retrocessão");
		ReportBuilder.styleCell(larrCells[i], false, true);

		return larrCells;
	}
}
