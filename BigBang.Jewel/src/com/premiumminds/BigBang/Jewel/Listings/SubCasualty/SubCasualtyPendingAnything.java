package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

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
import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class SubCasualtyPendingAnything
	extends SubCasualtyListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		SubCasualty[] larrAux;
		HashMap<UUID, ArrayList<SubCasualty>> larrMap;
		int i;
		UUID lidManager;
		GenericElement[] larrResult;

		larrAux = getPendingForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<SubCasualty>>();
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
				larrMap.put(lidManager, new ArrayList<SubCasualty>());
			larrMap.get(lidManager).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		larrResult[0] = buildHeaderSection("Sub-Sinistros em Gestão", larrAux, larrMap.size());

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

	protected SubCasualty[] getPendingForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<SubCasualty> larrAux;
		IEntity lrefubCs;
		IEntity lrefProcs;
		IEntity lrefSteps;
		MasterDB ldb;
		ResultSet lrsPolicies;

		if ( Utils.getCurrentAgent() != null )
			return new SubCasualty[0];

		try
		{
			lrefubCs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNProcess));
			lrefSteps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNStep));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefubCs.SQLForSelectAll() + ") [AuxSubC] WHERE [Process] IN (SELECT [PK] FROM (" +
					lrefProcs.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.IsRunning_In_Process}, new java.lang.Object[] {true}, null) +
					") [AuxProcs]) AND [Process] NOT IN (SELECT [Process] FROM (" +
					lrefSteps.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Step, Jewel.Petri.Constants.FKLevel_In_Step},
							new java.lang.Object[] {Constants.OPID_SubCasualty_CloseProcess, Constants.UrgID_Pending}, null) +
					") [AuxSteps])");
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
			filterByCompany(lstrSQL, UUID.fromString(parrParams[2]));

		larrAux = new ArrayList<SubCasualty>();

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
				larrAux.add(SubCasualty.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies));
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

		return larrAux.toArray(new SubCasualty[larrAux.size()]);
	}
}
