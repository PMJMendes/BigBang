package bigBang.module.generalSystemModule.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.shared.CostCenter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.ManageCostCenters;

public class CostCenterServiceImpl
	extends EngineImplementor
	implements CostCenterService
{
	private static final long serialVersionUID = 1L;

	public CostCenter[] getCostCenterList()
		throws SessionExpiredException, BigBangException
	{
		UUID lidStates;
        MasterDB ldb;
        ResultSet lrsStates;
		ArrayList<CostCenter> larrAux;
		ObjectBase lobjAux;
		CostCenter lobjTmp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<CostCenter>();

		try
		{
        	lidStates = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CostCenter);
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsStates = Entity.GetInstance(lidStates).SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsStates.next())
	        {
	        	lobjAux = Engine.GetWorkInstance(lidStates, lrsStates);
	        	lobjTmp = new CostCenter();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.code = (String)lobjAux.getAt(0);
	        	lobjTmp.name = (String)lobjAux.getAt(1);
	        	larrAux.add(lobjTmp);
	        }

        }
        catch (Throwable e)
        {
			try { lrsStates.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsStates.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new CostCenter[larrAux.size()]);
	}

	public String createCostCenter(CostCenter costCenter)
		throws SessionExpiredException, BigBangException
	{
		ManageCostCenters lobjMCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjMCC = new ManageCostCenters();
		lobjMCC.marrCreate = new ManageCostCenters.CostCenterData[1];
		lobjMCC.marrCreate[0] = lobjMCC.new CostCenterData();
		lobjMCC.marrCreate[0].mid = null;
		lobjMCC.marrCreate[0].mstrCode = costCenter.code;
		lobjMCC.marrCreate[0].mstrName = costCenter.name;
		lobjMCC.marrModify = null;
		lobjMCC.marrDelete = null;
		lobjMCC.marrNewIDs = null;

		try
		{
			lobjMCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjMCC.marrNewIDs[0].toString();
	}

	public void saveCostCenter(CostCenter costCenter)
		throws SessionExpiredException, BigBangException
	{
		ManageCostCenters lobjMCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjMCC = new ManageCostCenters();
		lobjMCC.marrCreate = new ManageCostCenters.CostCenterData[1];
		lobjMCC.marrCreate[0] = lobjMCC.new CostCenterData();
		lobjMCC.marrCreate[0].mid = UUID.fromString(costCenter.id);
		lobjMCC.marrCreate[0].mstrCode = costCenter.code;
		lobjMCC.marrCreate[0].mstrName = costCenter.name;
		lobjMCC.marrModify = null;
		lobjMCC.marrDelete = null;
		lobjMCC.marrNewIDs = null;

		try
		{
			lobjMCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public void deleteCostCenter(String id)
		throws SessionExpiredException, BigBangException
	{
		ManageCostCenters lobjMCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjMCC = new ManageCostCenters();
		lobjMCC.marrCreate = new ManageCostCenters.CostCenterData[1];
		lobjMCC.marrCreate[0] = lobjMCC.new CostCenterData();
		lobjMCC.marrCreate[0].mid = UUID.fromString(id);
		lobjMCC.marrCreate[0].mstrCode = null;
		lobjMCC.marrCreate[0].mstrName = null;
		lobjMCC.marrModify = null;
		lobjMCC.marrDelete = null;
		lobjMCC.marrNewIDs = null;

		try
		{
			lobjMCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
