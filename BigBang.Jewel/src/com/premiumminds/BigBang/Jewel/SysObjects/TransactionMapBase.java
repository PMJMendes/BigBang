package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public abstract class TransactionMapBase
	extends ObjectBase
{
	public static class I
	{
		public static int SET       = 0;
		public static int OWNER     = 1;
		public static int SETTLEDON = 2;
	}

	public abstract UUID getParentType();
	public abstract UUID getSubObjectType();
	public abstract DocOps generateDocOp(SQLServer pdb) throws BigBangJewelException;

	protected  TransactionDetailBase[] marrDetails;
	protected TransactionSetBase mrefSet;

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefSet = (TransactionSetBase)Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					getParentType()), (UUID)getAt(0));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

	public TransactionDetailBase[] getCurrentDetails()
		throws BigBangJewelException
	{
		ArrayList<TransactionDetailBase> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<TransactionDetailBase>();

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
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {TransactionDetailBase.I.OWNER, TransactionDetailBase.I.VOIDED},
					new java.lang.Object[] {getKey(), false}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add((TransactionDetailBase)Engine.GetWorkInstance(lrefObjects.getKey(), lrsObjects));
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

		marrDetails = larrAux.toArray(new TransactionDetailBase[larrAux.size()]);

		return marrDetails;
	}

	public void clearDetails()
	{
		marrDetails = null;
	}

	public int getCount()
		throws BigBangJewelException
	{
		getDetails();

		return marrDetails.length;
	}

	public boolean isSettled()
	{
		return (getAt(I.SETTLEDON) != null);
	}

	public void Settle(SQLServer pdb, UUID pidPrintSet)
		throws BigBangJewelException
	{
		PrintSetDocument lobjSetDoc;

		if ( getAt(I.SETTLEDON) != null )
			throw new BigBangJewelException("Esta transacção já foi saldada.");

		if ( pidPrintSet == null )
			pidPrintSet = mrefSet.createPrintSet(pdb);

		try
		{
			if ( pidPrintSet != null )
			{
				lobjSetDoc = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetDoc.setAt(0, pidPrintSet);
				lobjSetDoc.setAt(1, generateDocOp(pdb).marrCreate[0].mobjFile);
				lobjSetDoc.setAt(2, false);
				lobjSetDoc.SaveToDb(pdb);
			}

			setAt(I.SETTLEDON, new Timestamp(new java.util.Date().getTime()));
			SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public TR[] buildTable(int plngNumber)
		throws BigBangJewelException
	{
		TR[] larrRows;
		TD lcell;

		getDetails();

		larrRows = new TR[isSettled() ? 4 : 3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Transacção n. " + plngNumber);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner());
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Recibos", marrDetails.length, TypeDefGUIDs.T_Integer, false);

		if ( isSettled() )
			larrRows[3] = ReportBuilder.constructDualRow("Saldada em", getAt(I.SETTLEDON), TypeDefGUIDs.T_Date, false);

		return larrRows;
	}

	public Table buildInner()
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		getDetails();

		larrRows = new TR[marrDetails.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= marrDetails.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(marrDetails[i - 1].buildRow());
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected void getDetails()
		throws BigBangJewelException
	{
		if ( marrDetails == null )
			getCurrentDetails();
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[10];

		larrCells[0] = ReportBuilder.buildHeaderCell("Recibo");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Tipo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Prémio");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Emissão");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Vencimento");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Data Limite");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Data Cobrança");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Meios");
		ReportBuilder.styleCell(larrCells[9], false, true);

		return larrCells;
	}
}
