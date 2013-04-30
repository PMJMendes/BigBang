package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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
	private static class EntryData
	{
		public String mstrPolicy;
		public BigDecimal mdblValue;
		public Timestamp mdtDate;
	}

	@SuppressWarnings("deprecation")
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Timestamp ldtStart;
		Policy[] larrAux;
		GenericElement[] larrResult;

		if ( ((parrParams[0] == null) || "".equals(parrParams[0])) || ((parrParams[1] == null) || "".equals(parrParams[1])) )
			return doNotValid();

		ldtStart = Timestamp.valueOf(parrParams[1] + " 00:00:00.0");
		ldtStart.setDate(1);

		larrAux = getPortfolio(parrParams);

		larrResult = new GenericElement[2];

		try
		{
			larrResult[0] = buildDataSection("ORIGEM DAS DESPESAS - Custos ocorridos no exercício de " + parrParams[1].substring(0, 4), larrAux);

			larrResult[1] = buildDetailedTable(larrAux, ldtStart);
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

	private void filterByDates(StringBuilder pstrSQL, Timestamp pdtStart)
		throws BigBangJewelException
	{
		pstrSQL.append(" AND ([End Date] IS NULL OR [End Date] >= '")
				.append(pdtStart.toString().substring(0, 10)).append("')")
				.append(" AND [Begin Date] <= DATEADD(year, 1, '")
				.append(pdtStart.toString().substring(0, 10)).append("')");
	}

	private Table buildDetailedTable(Policy[] parrPolicies, Timestamp pdtStart)
	{
		Table ltbl;
		TR[] larrRows;
		TD[] larrCells;

		larrRows = new TR[1];
		larrCells = new TD[1];
		larrCells[0] = new TD();
		larrCells[0].addElement(buildSuperTable(splitDataArray(buildDataArray(parrPolicies, pdtStart), pdtStart)));
		ReportBuilder.styleInnerContainer(larrCells[0]);
		larrRows[0] = ReportBuilder.buildRow(larrCells);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	private EntryData[] buildDataArray(Policy[] parrPolicies, Timestamp pdtStart)
	{
		ArrayList<EntryData> larrAux;
		UUID lidFrac;
		Calendar ldtAux;
		EntryData lobjAux;
		EntryData[] larrResult;
		int i, j, n;

		if ( parrPolicies == null )
			return null;

		larrAux = new ArrayList<EntryData>();

		for ( i = 0; i < parrPolicies.length; i++ )
		{
			if ( (parrPolicies[i].getAt(Policy.I.MATURITYDAY) == null) &&(parrPolicies[i].getAt(Policy.I.MATURITYMONTH) == null) )
				continue;

			lidFrac = (UUID)parrPolicies[i].getAt(Policy.I.FRACTIONING);
			n = ( Constants.FracID_Year.equals(lidFrac) ? 1 : 0 ) +
					( Constants.FracID_Semester.equals(lidFrac) ? 2 : 0 ) +
					( Constants.FracID_Quarter.equals(lidFrac) ? 4 : 0 ) +
					( Constants.FracID_Month.equals(lidFrac) ? 12 : 0 );
			if ( n == 0 )
				continue;

			ldtAux = Calendar.getInstance();
			ldtAux.setTimeInMillis(pdtStart.getTime());
			ldtAux.set(Calendar.MONTH, (Integer)parrPolicies[i].getAt(Policy.I.MATURITYMONTH) - 1);
			ldtAux.set(Calendar.DAY_OF_MONTH, (Integer)parrPolicies[i].getAt(Policy.I.MATURITYDAY));
			if ( ldtAux.get(Calendar.DAY_OF_MONTH) < 8 )
			{
				ldtAux.add(Calendar.MONTH, -1);
				ldtAux.set(Calendar.DAY_OF_MONTH, 23);
			}
			else if ( ldtAux.get(Calendar.DAY_OF_MONTH) < 16 )
				ldtAux.set(Calendar.DAY_OF_MONTH, 1);
			else if ( ldtAux.get(Calendar.DAY_OF_MONTH) < 23 )
				ldtAux.set(Calendar.DAY_OF_MONTH, 16);
			else
				ldtAux.set(Calendar.DAY_OF_MONTH, 23);
			while ( ldtAux.getTime().getTime() < pdtStart.getTime() )
				ldtAux.add(Calendar.MONTH, 12/n);

			for ( j = 0; j < n; j ++ )
			{
				lobjAux = new EntryData();
				lobjAux.mstrPolicy = parrPolicies[i].getLabel();
				lobjAux.mdblValue = (BigDecimal)parrPolicies[i].getAt(Policy.I.TOTALPREMIUM);
				lobjAux.mdtDate = new Timestamp(ldtAux.getTimeInMillis());
				larrAux.add(lobjAux);
				ldtAux.add(Calendar.MONTH, 12/n);
			}
		}

		larrResult = larrAux.toArray(new EntryData[larrAux.size()]);

		Arrays.sort(larrResult, new Comparator<EntryData>()
		{
			public int compare(EntryData o1, EntryData o2)
			{
				if ( o1.mdtDate.equals(o2.mdtDate) )
					return o1.mstrPolicy.compareTo(o2.mstrPolicy);
				return o1.mdtDate.compareTo(o2.mdtDate);
			}
		});

		return larrResult;
	}

	private EntryData[][] splitDataArray(EntryData[] parrSource, Timestamp pdtStart)
	{
		EntryData[][] larrResult;
		Calendar ldtNext;
		int n, i, j;

		if ( parrSource == null )
			return null;

		larrResult = new EntryData[48][];

		ldtNext = Calendar.getInstance();
		ldtNext.setTimeInMillis(pdtStart.getTime());

		i = 0;
		for ( n = 0; n < 48; n++ )
		{
			if ( i >= parrSource.length )
			{
				larrResult[n] = new EntryData[0];
				continue;
			}

			if ( ldtNext.get(Calendar.DAY_OF_MONTH) == 1 )
				ldtNext.set(Calendar.DAY_OF_MONTH, 8);
			else if ( ldtNext.get(Calendar.DAY_OF_MONTH) == 8 )
				ldtNext.set(Calendar.DAY_OF_MONTH, 16);
			else if ( ldtNext.get(Calendar.DAY_OF_MONTH) == 16 )
				ldtNext.set(Calendar.DAY_OF_MONTH, 23);
			else
			{
				ldtNext.set(Calendar.DAY_OF_MONTH, 1);
				ldtNext.add(Calendar.MONTH, 1);
			}

			j = i;
			while ( (j < parrSource.length) && (parrSource[j].mdtDate.getTime() < ldtNext.getTime().getTime()) )
				j++;
			larrResult[n] = Arrays.copyOfRange(parrSource, i, j);
			i = j;
		}

		return larrResult;
	}

	private Table buildSuperTable(EntryData[][] parrSource)
	{
		int i, j, s;
		Table ltbl;
		TR[] larrRows;
		TD[] larrCells;

		larrRows = new TR[4];

		for ( i = 0; i < 4; i++ )
		{
			larrCells = new TD[12];
			s = 0;
			for ( j = 0; j < 12; j++ )
			{
				if ( s < parrSource[4 * j + i].length )
					s = parrSource[4 * j + i].length;
			}
			for ( j = 0; j < 12; j++ )
			{
				larrCells[j] = new TD();
				larrCells[j].addElement(buildWeeklyTable(parrSource[4 * j + i], s));
				ReportBuilder.styleInnerContainer(larrCells[j]);
			}
			larrRows[i] = ReportBuilder.buildRow(larrCells);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	private Table buildWeeklyTable(EntryData[] parrWeek, int plngSize)
	{
		Table ltbl;
		TR[] larrRows;
		BigDecimal ldblTotal;
		int i;

		larrRows = new TR[plngSize + 2];

		larrRows[0] = ReportBuilder.buildRow(buildWeeklyHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		ldblTotal = BigDecimal.ZERO;
		for ( i = 1; i <= plngSize; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildWeeklyRow(i <= parrWeek.length ? parrWeek[i - 1] : null));
			ReportBuilder.styleRow(larrRows[i], false);
			if ( (i <= parrWeek.length) && (parrWeek[i - 1].mdblValue != null) )
				ldblTotal = ldblTotal.add(parrWeek[i - 1].mdblValue);
		}

		larrRows[plngSize + 1] = ReportBuilder.constructDualHeaderRowCell("€ " + ldblTotal.toPlainString());

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildWeeklyHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[2];

		larrCells[0] = ReportBuilder.buildHeaderCell("Origem");
		ReportBuilder.styleCell(larrCells[0], false, true);

		larrCells[1] = ReportBuilder.buildHeaderCell("Despesa");
		ReportBuilder.styleCell(larrCells[1], false, true);

		setWeeklyWidths(larrCells);

		return larrCells;
	}

	private TD[] buildWeeklyRow(EntryData pobjEntry)
	{
		TD[] larrCells;

		larrCells = new TD[2];

		larrCells[0] = ReportBuilder.buildCell(pobjEntry == null ? "" : pobjEntry.mstrPolicy, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, true);

		larrCells[1] = ReportBuilder.buildCell(pobjEntry == null || pobjEntry.mdblValue == null ? "" :
				String.format("%,.2f", ((BigDecimal)pobjEntry.mdblValue)), TypeDefGUIDs.T_String, true);
		ReportBuilder.styleCell(larrCells[1], true, true);

		setWeeklyWidths(larrCells);

		return larrCells;
	}

	protected void setWeeklyWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(130);
		parrCells[ 1].setWidth(130);
	}
}
