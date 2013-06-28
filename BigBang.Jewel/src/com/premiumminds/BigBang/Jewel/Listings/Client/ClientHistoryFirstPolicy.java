package com.premiumminds.BigBang.Jewel.Listings.Client;

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
import com.premiumminds.BigBang.Jewel.Listings.ClientListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class ClientHistoryFirstPolicy
	extends ClientListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Client[] larrAux;
		HashMap<UUID, ArrayList<Client>> larrMap;
		int i;
		UUID lidManager;
		GenericElement[] larrResult;

		larrAux = getHistoryForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Client>>();
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
				larrMap.put(lidManager, new ArrayList<Client>());
			larrMap.get(lidManager).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		larrResult[0] = buildHeaderSection("Histórico de Criação de Primeira Apólice", larrAux, larrMap.size());

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

	protected Client[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Client> larrAux;
		IEntity lrefClients, lrefLogs;
		MasterDB ldb;
		ResultSet lrsClients;
		UUID lidAgent;

		larrAux = new ArrayList<Client>();

		try
		{
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefClients.SQLForSelectAll()).append(") [AuxCli] WHERE [Process] IN (SELECT [Process] FROM(")
					.append(lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Client_CreatePolicy, false},
							null))
					.append(") [AuxLogs])");

			if ( parrParams[2] != null )
				lstrSQL.append(" AND (SELECT MIN([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
						new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
						new java.lang.Object[] {Constants.OPID_Client_CreatePolicy, false}, null) + ") [AuxLogs2] " +
						"WHERE [Process] = [AuxCli].[Process]) >= '").append(parrParams[2]).append("'");

			if ( parrParams[3] != null )
				lstrSQL.append(" AND (SELECT MIN([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
						new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
						new java.lang.Object[] {Constants.OPID_Client_CreatePolicy, false}, null) + ") [AuxLogs2] " +
						"WHERE [Process] = [AuxCli].[Process]) < DATEADD(d, 1, '").append(parrParams[3]).append("')");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			filterByClientGroup(lstrSQL, UUID.fromString(parrParams[0]));

		if ( parrParams[1] != null )
			filterByAgent(lstrSQL, UUID.fromString(parrParams[1]));

		lidAgent = Utils.getCurrentAgent();
		if ( lidAgent != null )
			filterByAgent(lstrSQL, lidAgent);

		larrAux = new ArrayList<Client>();

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
			lrsClients = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsClients.next() )
				larrAux.add(Client.GetInstance(Engine.getCurrentNameSpace(), lrsClients));
		}
		catch (Throwable e)
		{
			try { lrsClients.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsClients.close();
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

		return larrAux.toArray(new Client[larrAux.size()]);
	}
}
