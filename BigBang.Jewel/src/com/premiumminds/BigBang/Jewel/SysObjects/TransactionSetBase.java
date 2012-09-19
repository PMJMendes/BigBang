package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;

public abstract class TransactionSetBase
	extends ObjectBase
{
	public static class I
	{
		public static int DATE = 0;
		public static int USER = 1;
	}

	public abstract String getTitle();
	public abstract UUID getSubObjectType();
	public abstract UUID getTemplate();

	protected TransactionMapBase[] marrMaps;

	public TransactionMapBase[] getCurrentMaps()
		throws BigBangJewelException
	{
		ArrayList<TransactionMapBase> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<TransactionMapBase>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), getSubObjectType())); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {TransactionMapBase.I.SET}, new java.lang.Object[] {getKey()},
					new int[] {TransactionMapBase.I.SETTLEDON});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add((TransactionMapBase)Engine.GetWorkInstance(lrefObjects.getKey(), lrsObjects));
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		marrMaps = larrAux.toArray(new TransactionMapBase[larrAux.size()]);

		return marrMaps;
	}

	public int getCount()
		throws BigBangJewelException
	{
		getMaps();

		return marrMaps.length;
	}

	public int getTotalCount()
		throws BigBangJewelException
	{
        int llngTotal;
		int i;

		getMaps();

		llngTotal = 0;
		for ( i = 0; i < marrMaps.length; i++ )
			llngTotal += marrMaps[i].getCurrentDetails().length;

		return llngTotal;
	}

	public boolean isComplete()
		throws BigBangJewelException
	{
		TransactionMapBase[] larrAux;
        boolean b;
		int i;

		larrAux = getCurrentMaps();

		b = true;
		for ( i = 0; i < larrAux.length; i++ )
		{
			if ( !larrAux[i].isSettled() )
			{
				b = false;
				break;
			}
		}

		return b;
	}

	public void SettleAll(SQLServer pdb)
		throws BigBangJewelException
	{
		UUID pidSet;
		int i;

		getMaps();

		pidSet = createPrintSet(pdb);

		for ( i = 0; i < marrMaps.length; i++ )
			if ( !marrMaps[i].isSettled() )
				marrMaps[i].Settle(pdb, pidSet);
	}

	public UUID createPrintSet(SQLServer pdb)
		throws BigBangJewelException
	{
		PrintSet lobjSet;

		if ( getTemplate() == null )
			return null;

		try
		{
			lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjSet.setAt(0, getTemplate());
			lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
			lobjSet.setAt(2, Engine.getCurrentUser());
			lobjSet.setAt(3, (Timestamp)null);
			lobjSet.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjSet.getKey();
	}

    public User getUser()
    {
    	try
    	{
			return User.GetInstance(getNameSpace(), (UUID)getAt(TransactionSetBase.I.USER));
		}
    	catch (JewelEngineException e)
    	{
    		return null;
		}
    }

	public Table buildHeaderSection()
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		getMaps();

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Gestão de Transacções");

		larrRows[1] = ReportBuilder.constructDualRow("Tipo de Transacção", getTitle(), TypeDefGUIDs.T_String);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Transacções", marrMaps.length, TypeDefGUIDs.T_Integer);

		larrRows[3] = ReportBuilder.constructDualRow("Nº de Recibos", getTotalCount(), TypeDefGUIDs.T_Integer);

		larrRows[4] = ReportBuilder.constructDualRow("Gerado em", getAt(I.DATE), TypeDefGUIDs.T_Date);

		larrRows[5] = ReportBuilder.constructDualRow("Gerado por", getUser().getDisplayName(), TypeDefGUIDs.T_String);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	public Table buildDataSection(int plngNumber)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		getMaps();

		larrRows = marrMaps[plngNumber - 1].buildTable(plngNumber);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected void getMaps()
		throws BigBangJewelException
	{
		if ( marrMaps == null )
			getCurrentMaps();
	}
}
