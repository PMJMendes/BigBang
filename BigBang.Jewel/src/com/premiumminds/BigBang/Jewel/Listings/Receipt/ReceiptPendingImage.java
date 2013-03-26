package com.premiumminds.BigBang.Jewel.Listings.Receipt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.GenericElement;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class ReceiptPendingImage
	extends ReceiptListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Receipt[] larrAux;
		HashMap<UUID, ArrayList<Receipt>> larrMap;
		int i;
		UUID lidManager;
		GenericElement[] larrResult;

		larrAux = getPendingForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Receipt>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			try
			{
				lidManager = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrAux[i].getAbsolutePolicy().GetProcessID()).GetManagerID();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( larrMap.get(lidManager) == null )
				larrMap.put(lidManager, new ArrayList<Receipt>());
			larrMap.get(lidManager).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		larrResult[0] = buildHeaderSection("Recibos Pendentes de Recepção de Recibo Físico", larrAux, larrMap.size());

		i = 1;
		for ( UUID lid: larrMap.keySet() )
		{
			try
			{
				larrResult[i] = buildDataSection(User.GetInstance(Engine.getCurrentNameSpace(), lid).getDisplayName(), larrMap.get(lid));
			}
			catch (BigBangJewelException e)
			{
				throw e;
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			i++;
		}

		return larrResult;
	}

	protected Receipt[] getPendingForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Receipt> larrAux;
		IEntity lrefReceipts, lrefSteps;
		MasterDB ldb;
		ResultSet lrsReceipts;

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefSteps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNStep));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefReceipts.SQLForSelectAll() + ") [AuxRecs] WHERE [Process] IN (SELECT [Process] FROM(" + 
					lrefSteps.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Step, Jewel.Petri.Constants.FKLevel_In_Step},
					new java.lang.Object[] {Constants.OPID_Receipt_ReceiveImage, Constants.UrgID_Pending}, null) + ") [AuxSteps])");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			filterByClient(lstrSQL, UUID.fromString(parrParams[0]));

		if ( parrParams[1] != null )
			filterByClientGroup(lstrSQL, UUID.fromString(parrParams[1]));

		if ( parrParams[2] != null )
			filterByAgent(lstrSQL, UUID.fromString(parrParams[2]));

		if ( parrParams[3] != null )
			filterByCompany(lstrSQL, UUID.fromString(parrParams[3]));

		if ( parrParams[4] != null )
			lstrSQL.append(" AND [Maturity Date] >= '").append(parrParams[4]).append("'");

		if ( parrParams[5] != null )
			lstrSQL.append(" AND [Maturity Date] < DATEADD(d, 1, '").append(parrParams[5]).append("')");

		if ( parrParams[6] != null )
			lstrSQL.append(" AND [Due Date] >= '").append(parrParams[6]).append("'");

		if ( parrParams[7] != null )
			lstrSQL.append(" AND [Due Date] < DATEADD(d, 1, '").append(parrParams[7]).append("')");

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
}
