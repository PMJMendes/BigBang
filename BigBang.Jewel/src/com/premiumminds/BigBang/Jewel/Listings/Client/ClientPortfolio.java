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

public class ClientPortfolio
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

		larrAux = getPortfolio(parrParams);

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

		larrResult[0] = buildHeaderSection("Carteira Global de Clientes", larrAux, larrMap.size());

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

	protected Client[] getPortfolio(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Client> larrAux;
		IEntity lrefClients;
		MasterDB ldb;
		ResultSet lrsClients;
		UUID lidAgent;

		larrAux = new ArrayList<Client>();

		try
		{
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefClients.SQLForSelectAll()).append(") [AuxCli] WHERE 1=1");
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
