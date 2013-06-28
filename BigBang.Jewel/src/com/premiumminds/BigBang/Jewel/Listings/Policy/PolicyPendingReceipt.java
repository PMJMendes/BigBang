package com.premiumminds.BigBang.Jewel.Listings.Policy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.premiumminds.BigBang.Jewel.Listings.PolicyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class PolicyPendingReceipt
	extends PolicyListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Policy[] larrAux;
		HashMap<UUID, ArrayList<Policy>> larrMap;
		int i;
		UUID lidManager;
		GenericElement[] larrResult;

		if ( (parrParams[4] == null) || "".equals(parrParams[4]) )
			parrParams[4] = new Timestamp(new java.util.Date().getTime()).toString().substring(0, 10);

		larrAux = getPendingForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Policy>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			try
			{
				lidManager = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrAux[i].GetProcessID()).GetManagerID();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( larrMap.get(lidManager) == null )
				larrMap.put(lidManager, new ArrayList<Policy>());
			larrMap.get(lidManager).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		larrResult[0] = buildHeaderSection("Apólices Pendentes de Continuado", larrAux, larrMap.size());

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

	protected Policy[] getPendingForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Policy> larrAux;
		IEntity lrefPolicies;
		IEntity lrefReceipts;
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrsPolicies;
		UUID lidAgent;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.STATUS},
					new java.lang.Object[] {Constants.StatusID_Valid}, null) + ") [AuxPol] " +
					"WHERE DATEADD(month, -CASE Fractioning " +
							"WHEN 'B8234D73-4432-45A0-B670-9F8101580CB5' THEN 12 " +
							"WHEN '9EDAE32A-29DF-46F1-B37B-9F8101581416' THEN 6 " +
							"WHEN '6165B7A7-1768-4A3E-A482-9F81015819BA' THEN 3 " +
							"WHEN '4D5AAE28-4CC6-4B50-A478-9F8101582002' THEN 1 " +
							"ELSE NULL END, '" + parrParams[4] + "') > (SELECT MAX([Maturity Date]) FROM (" +
					lrefReceipts.SQLForSelectAll() +
					") [AuxRecs] WHERE [Type] IN ('" + Constants.RecType_New + "', '" + Constants.RecType_Continuing + "'" +
//					", '" + Constants.RecType_Adjustment + "'" +
					") AND [Process] NOT IN (SELECT [Process] FROM (" + lrefLogs.SQLForSelectByMembers(
					new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_ReturnToInsurer, false}, null) + ") [AuxLogs1]) " +
					"AND [Policy] = [AuxPol].[PK])");
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

		lidAgent = Utils.getCurrentAgent();
		if ( lidAgent != null )
			filterByAgent(lstrSQL, lidAgent);

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
}
