package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.PolicyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class PolicyExternWeekly
	extends PolicyListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Policy[] larrAux;
		GenericElement[] larrResult;

		if ( ((parrParams[0] == null) || "".equals(parrParams[0])) || ((parrParams[1] == null) || "".equals(parrParams[1])) )
			return doNotValid();

		larrAux = getPortfolio(parrParams);

		larrResult = new GenericElement[1];

		try
		{
			larrResult[0] = buildDataSection("ORIGEM DAS DESPESAS - Custos ocorridos no exercício de " + parrParams[1].substring(0, 4), larrAux);
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

	protected Policy[] getPortfolio(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Policy> larrAux;
		IEntity lrefPolicies;
		MasterDB ldb;
		ResultSet lrsPolicies;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.STATUS, Policy.I.DURATION},
					new java.lang.Object[] {Constants.StatusID_Valid, Constants.DurID_Ongoing}, null) + ") [AuxPol] WHERE 1=1");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			filterByClient(lstrSQL, UUID.fromString(parrParams[0]));

		if ( parrParams[1] != null )
			filterByDates(lstrSQL, Timestamp.valueOf(parrParams[1] + " 00:00:00.0"));

		larrAux = new ArrayList<Policy>();

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
			lrsPolicies = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsPolicies.next() )
				larrAux.add(Policy.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies));
		}
		catch (Throwable e)
		{
			try { lrsPolicies.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsPolicies.close();
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

		return larrAux.toArray(new Policy[larrAux.size()]);
	}

	protected TR[] buildDataTable(String pstrHeader, Policy[] parrPolicies)
		throws BigBangJewelException
	{
		TR[] larrRows;
		TD lcell;

		larrRows = new TR[2];

		lcell = ReportBuilder.buildHeaderCell(pstrHeader);
		ReportBuilder.styleCell(lcell, false, false);
		larrRows[0] = ReportBuilder.buildRow(new TD[] {lcell});
		ReportBuilder.styleRow(larrRows[0], true);

		lcell = new TD();
		lcell.addElement(buildInner(parrPolicies));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		return larrRows;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[5];

		larrCells[0] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Prémio Total Anual");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Data de Vencimento");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Forma de Pagamento");
		ReportBuilder.styleCell(larrCells[4], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(Policy pobjPolicy)
		throws BigBangJewelException
	{
		String lstrMatDate;
		ObjectBase lobjAux;
		TD[] larrCells;

		if ( (pobjPolicy.getAt(Policy.I.MATURITYDAY) != null) &&(pobjPolicy.getAt(Policy.I.MATURITYMONTH) != null) )
			lstrMatDate = ((Integer)pobjPolicy.getAt(Policy.I.MATURITYDAY)).toString() + "-" +
					(new String[] {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"})
						[(Integer)pobjPolicy.getAt(Policy.I.MATURITYMONTH) - 1];
		else
			lstrMatDate = null;

		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Fractioning),
					(UUID)pobjPolicy.getAt(Policy.I.FRACTIONING));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrCells = new TD[5];

		larrCells[0] = ReportBuilder.buildCell(pobjPolicy.getAt(Receipt.I.NUMBER), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjPolicy.GetSubLine().getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell((BigDecimal)pobjPolicy.getAt(Policy.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lstrMatDate, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjAux.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(130);
		parrCells[ 1].setWidth(130);
		parrCells[ 2].setWidth(120);
		parrCells[ 3].setWidth(120);
		parrCells[ 4].setWidth(120);
	}

	private GenericElement[] doNotValid()
	{
		TR[] larrRows;
		Table ltbl;

		larrRows = new TR[1];
		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Tem que indicar o cliente e a data de início pretendidos.");

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return new GenericElement[] {ltbl};
	}

	protected void filterByDates(StringBuilder pstrSQL, Timestamp pdtStart)
		throws BigBangJewelException
	{
		pstrSQL.append(" AND ([End Date] IS NULL OR [End Date] >= '")
				.append(pdtStart.toString().substring(0, 10)).append("')")
				.append(" AND [Begin Date] <= DATEADD(year, 1, '")
				.append(pdtStart.toString().substring(0, 10)).append("')");
	}
}
