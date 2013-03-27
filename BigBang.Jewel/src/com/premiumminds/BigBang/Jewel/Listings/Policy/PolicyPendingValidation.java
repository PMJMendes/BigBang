package com.premiumminds.BigBang.Jewel.Listings.Policy;

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
import com.premiumminds.BigBang.Jewel.Listings.PolicyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

public class PolicyPendingValidation
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

		larrResult[0] = buildHeaderSection("Apólices Pendentes de Validação", larrAux, larrMap.size());

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
		IEntity lrefPolicies, lrefSteps;
		MasterDB ldb;
		ResultSet lrsPolicies;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSteps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNStep));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefPolicies.SQLForSelectAll() + ") [AuxPol] WHERE [Process] IN (SELECT [Process] FROM(" +
					lrefSteps.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Step, Jewel.Petri.Constants.FKLevel_In_Step},
					new java.lang.Object[] {Constants.OPID_Policy_ValidatePolicy, Constants.UrgID_Pending}, null) + ") [AuxSteps])");
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
