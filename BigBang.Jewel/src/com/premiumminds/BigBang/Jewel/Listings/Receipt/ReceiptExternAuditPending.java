package com.premiumminds.BigBang.Jewel.Listings.Receipt;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.ecs.GenericElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Strong;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.Template;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ReceiptExternAuditPending
	extends ReceiptListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Receipt[] larrAux;
		GenericElement[] larrResult;

		if ( ((parrParams[0] == null) || "".equals(parrParams[0])) || ((parrParams[1] == null) || "".equals(parrParams[1])) )
			return doNotValid();

		larrAux = getPendingForOperation(parrParams);

		larrResult = new GenericElement[2];

		larrResult[0] = buildHeaderSection(Client.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(parrParams[0])).getLabel(),
				Integer.parseInt(parrParams[1]));

		try
		{
			larrResult[1] = buildDataSection("", larrAux);
		}
		catch (BigBangJewelException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrResult;
	}

	protected Receipt[] getPendingForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Receipt> larrAux;
		IEntity lrefReceipts, lrefLogs, lrefSteps;
		MasterDB ldb;
		ResultSet lrsReceipts;

		larrAux = new ArrayList<Receipt>();

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			lrefSteps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNStep));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefReceipts.SQLForSelectAll()).append(") [AuxRecs] WHERE [Process] IN (SELECT [Process] FROM(")
					.append(lrefLogs.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Log,
							Jewel.Petri.Constants.Undone_In_Log}, new java.lang.Object[] {Constants.OPID_Receipt_CreatePaymentNotice, false}, null))
					.append(") [AuxLogs] WHERE 1=1");

			if ( parrParams[1] != null )
			{
				lstrSQL.append(" AND [Timestamp] >= '").append(parrParams[1]).append("-01-01'");
				lstrSQL.append(" AND [Timestamp] < '").append(Integer.parseInt(parrParams[1]) + 1).append("-01-01'");

				lstrSQL.append(") AND ([Process] IN (SELECT [Process] FROM(")
						.append(lrefLogs.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Log,
								Jewel.Petri.Constants.Undone_In_Log}, new java.lang.Object[] {Constants.OPID_Receipt_Payment, false}, null))
						.append(") [AuxLogs] WHERE 1=1");

				lstrSQL.append(" AND [Timestamp] >= '").append(Integer.parseInt(parrParams[1]) + 1).append("-01-01'");

				lstrSQL.append(") OR ");
			}
			else
				lstrSQL.append(") AND (");

			lstrSQL.append("[Process] IN (SELECT [Process] FROM (")
					.append(lrefSteps.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Step,
							Jewel.Petri.Constants.FKLevel_In_Step}, new java.lang.Object[] {Constants.OPID_Receipt_Payment,
							Constants.UrgID_Pending}, null))
					.append(" UNION ")
					.append(lrefSteps.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Step,
							Jewel.Petri.Constants.FKLevel_In_Step}, new java.lang.Object[] {Constants.OPID_Receipt_ExternAllowSendPayment,
							Constants.UrgID_Valid}, null))
					.append(") [AuxSteps]) )");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			filterByClient(lstrSQL, UUID.fromString(parrParams[0]));

		larrAux = new ArrayList<Receipt>();

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsReceipts = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsReceipts.next() )
				larrAux.add(Receipt.GetInstance(Engine.getCurrentNameSpace(), lrsReceipts));
		}
		catch (Throwable e)
		{
			try { lrsReceipts.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsReceipts.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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

		return larrAux.toArray(new Receipt[larrAux.size()]);
	}

	protected Table buildHeaderSection(String pstrClient, int plngYear)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		TD[] larrCells;
		Template lobjLogo;
		FileXfer lobjFile;
		String lstr64;
		IMG lobjImg;
		Div lobjDiv;

		try
		{
			lobjLogo = Template.GetInstance(Engine.getCurrentNameSpace(), Constants.TID_Logo);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrRows = new TR[1];

		larrCells = new TD[2];

		larrCells[0] = new TD();
		if ( lobjLogo.getAt(1) != null )
		{
			if ( lobjLogo.getAt(1) instanceof FileXfer )
				lobjFile = (FileXfer)lobjLogo.getAt(1);
			else
				lobjFile = new FileXfer((byte[])lobjLogo.getAt(1));
			lstr64 = Base64.encodeBase64String(lobjFile.getData());
			lobjImg = new IMG();
			lobjImg.setSrc("data:" + lobjFile.getContentType() + ";base64," + lstr64);
			larrCells[0].addElementToRegistry(lobjImg);
		}

		larrCells[1] = new TD();
		lobjDiv = new Div();
		lobjDiv.setStyle("font-size: small;");
		larrCells[1].addElementToRegistry(lobjDiv);
		larrCells[1].setAlign("right");
		lobjDiv.addElementToRegistry(new Strong(pstrClient));
		lobjDiv.addElementToRegistry(new BR());
		lobjDiv.addElementToRegistry("Recibos pendentes à data de 31-12-" + plngYear);
		lobjDiv.addElementToRegistry(new BR());
		lobjDiv.addElementToRegistry(ReportBuilder.BuildValue(TypeDefGUIDs.T_Date, new Timestamp(new java.util.Date().getTime())));

		larrRows[0] = ReportBuilder.buildRow(larrCells);

		ltbl = ReportBuilder.buildTable(larrRows);
		ltbl.setWidth("100%");

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		BigDecimal ldblTotal;
		BigDecimal ldblTotalPos;
		BigDecimal ldblTotalNeg;
		BigDecimal ldblPremium;
		int i;
		TR[] larrRows;
		TD lcell;

		ldblTotal = BigDecimal.ZERO;
		ldblTotalPos = BigDecimal.ZERO;
		ldblTotalNeg = BigDecimal.ZERO;
		for ( i = 0; i < parrReceipts.length; i++ )
		{
			if ( parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM) != null )
			{
				ldblPremium = (BigDecimal)parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM);
				ldblTotal = ldblTotal.add(ldblPremium);
				if ( ldblPremium.signum() > 0 )
					ldblTotalPos = ldblTotalPos.add(ldblPremium);
				else
					ldblTotalNeg = ldblTotalNeg.add(ldblPremium);
			}
		}

		Arrays.sort(parrReceipts, new Comparator<Receipt>()
		{
			public int compare(Receipt o1, Receipt o2)
			{
				try
				{
					return o1.getClient().getLabel().compareTo(o2.getClient().getLabel());
				}
				catch (BigBangJewelException e)
				{
					return 0;
				}
			}
		});

		larrRows = new TR[6];

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrReceipts));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[0] = ReportBuilder.buildRow(new TD[] {lcell});

		lcell = ReportBuilder.buildHeaderCell("");
		lcell.setColSpan(2);
		ReportBuilder.styleCell(lcell, true, false);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});
		ReportBuilder.styleRow(larrRows[1], false);

		larrRows[2] = ReportBuilder.constructDualRow("Total de Recibos", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotalPos, TypeDefGUIDs.T_Decimal, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Estornos", ldblTotalNeg, TypeDefGUIDs.T_Decimal, false);

		lcell = ReportBuilder.buildHeaderCell("(*) O pagamento dos prémios de Recibos Novos e Actas é devido no acto da contratação.");
		lcell.setColSpan(2);
		ReportBuilder.styleCell(lcell, true, false);
		larrRows[5] = ReportBuilder.buildRow(new TD[] {lcell});
		ReportBuilder.styleRow(larrRows[5], false);

		return larrRows;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[8];

		larrCells[0] = ReportBuilder.buildHeaderCell("Recibo");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Comp");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Prémio");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Data Início");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Data Fim");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Data Limite");
		ReportBuilder.styleCell(larrCells[7], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(Receipt pobjReceipt)
		throws BigBangJewelException
	{
		Policy lobjPolicy;
		TD[] larrCells;

		lobjPolicy = pobjReceipt.getAbsolutePolicy();

		larrCells = new TD[8];

		larrCells[0] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.NUMBER), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(lobjPolicy.GetSubLine().getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjPolicy.GetCompany().getAt(1), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjPolicy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.MATURITYDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ENDDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(pobjReceipt.getExternalDueDate(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[7], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(170);
		parrCells[ 2].setWidth( 60);
		parrCells[ 3].setWidth(130);
		parrCells[ 4].setWidth( 80);
		parrCells[ 5].setWidth(110);
		parrCells[ 6].setWidth(110);
		parrCells[ 7].setWidth(110);
	}

	private GenericElement[] doNotValid()
	{
		TR[] larrRows;
		Table ltbl;

		larrRows = new TR[1];
		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Tem que indicar o cliente e o ano pretendido.");

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return new GenericElement[] {ltbl};
	}
}
