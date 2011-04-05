package bigBang.module.generalSystemModule.server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Operations.ManageCostCenters;

public class CostCenterServiceImpl
	extends EngineImplementor
	implements CostCenterService
{
	private static final long serialVersionUID = 1L;

	public CostCenter[] getCostCenterList()
		throws SessionExpiredException, BigBangException
	{
        MasterDB ldb;
        ResultSet lrsCenters;
		ArrayList<CostCenter> larrAux;
		com.premiumminds.BigBang.Jewel.Objects.CostCenter lobjAux;
		CostCenter lobjTmp;
		CostCenter[] larrResult;
		int i;
        ResultSet lrsUsers;
        ArrayList<User> larrAuxUsers;
		IEntity lrefUserDecs;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		UserDecoration lobjUser;
		User lobjTmpUser;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<CostCenter>();

		try
		{
        	lrefUserDecs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsCenters = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
	        		Constants.ObjID_CostCenter)).SelectAll(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
	        while (lrsCenters.next())
	        {
	        	lobjAux = com.premiumminds.BigBang.Jewel.Objects.CostCenter.GetInstance(Engine.getCurrentNameSpace(),
	        			lrsCenters);
	        	lobjTmp = new CostCenter();
	        	lobjTmp.id = lobjAux.getKey().toString();
	        	lobjTmp.code = (String)lobjAux.getAt(0);
	        	lobjTmp.name = (String)lobjAux.getAt(1);
	        	lobjTmp.members = null;
	        	larrAux.add(lobjTmp);
	        }

        }
        catch (Throwable e)
        {
			try { lrsCenters.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsCenters.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		larrResult = larrAux.toArray(new CostCenter[larrAux.size()]);
		
		larrMembers = new int[1];
		larrMembers[0] = Constants.FKCostCenter_In_UserDecoration;
		larrParams = new java.lang.Object[1];

		for ( i = 0; i < larrResult.length; i++ )
		{
			larrAuxUsers = new ArrayList<User>();

			larrParams[0] = UUID.fromString(larrResult[i].id);

			try
			{
				lrsUsers = lrefUserDecs.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

			try
			{
				while ( lrsUsers.next() )
				{
					lobjUser = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrsUsers);
					lobjTmpUser = new User();
					lobjTmpUser.id = lobjUser.getKey().toString();
					lobjTmpUser.name = lobjUser.getBaseUser().getDisplayName();
					lobjTmpUser.username = lobjUser.getBaseUser().getUserName();
					lobjTmpUser.password = null; //JMMM: No way!
					lobjTmpUser.profile = new UserProfile();
					lobjTmpUser.profile.id = lobjUser.getBaseUser().getProfile().getKey().toString();
					lobjTmpUser.profile.name = lobjUser.getBaseUser().getProfile().getLabel();
					lobjTmpUser.costCenterId = ((UUID)lobjUser.getAt(2)).toString();
					lobjTmpUser.email = (String)lobjUser.getAt(1);
					larrAuxUsers.add(lobjTmpUser);
				}
			}
	        catch (Throwable e)
	        {
				try { lrsUsers.close(); } catch (Throwable e1) {}
				try { ldb.Disconnect(); } catch (Throwable e1) {}
	        	throw new BigBangException(e.getMessage(), e);
	        }

	        try
	        {
	        	lrsUsers.close();
	        }
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangException(e.getMessage(), e);
			}

			larrResult[i].members = larrAuxUsers.toArray(new User[larrAuxUsers.size()]);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult;
	}

	public CostCenter createCostCenter(CostCenter costCenter)
		throws SessionExpiredException, BigBangException
	{
		ManageCostCenters lopMCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMCC = new ManageCostCenters(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMCC.marrCreate = new ManageCostCenters.CostCenterData[1];
			lopMCC.marrCreate[0] = lopMCC.new CostCenterData();
			lopMCC.marrCreate[0].mid = null;
			lopMCC.marrCreate[0].mstrCode = costCenter.code;
			lopMCC.marrCreate[0].mstrName = costCenter.name;
			lopMCC.marrModify = null;
			lopMCC.marrDelete = null;
			lopMCC.marrNewIDs = null;

			lopMCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		costCenter.id = lopMCC.marrNewIDs[0].toString();
		costCenter.code = lopMCC.marrCreate[0].mstrCode;
		costCenter.name = lopMCC.marrCreate[0].mstrName;
		costCenter.members = new User[0];

		return costCenter;
	}

	public CostCenter saveCostCenter(CostCenter costCenter)
		throws SessionExpiredException, BigBangException
	{
		ManageCostCenters lopMCC;
        ResultSet lrsUsers;
        ArrayList<User> larrAuxUsers;
        MasterDB ldb;
		IEntity lrefUserDecs;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		UserDecoration lobjUser;
		User lobjTmpUser;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMCC = new ManageCostCenters(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMCC.marrModify = new ManageCostCenters.CostCenterData[1];
			lopMCC.marrModify[0] = lopMCC.new CostCenterData();
			lopMCC.marrModify[0].mid = UUID.fromString(costCenter.id);
			lopMCC.marrModify[0].mstrCode = costCenter.code;
			lopMCC.marrModify[0].mstrName = costCenter.name;
			lopMCC.marrCreate = null;
			lopMCC.marrDelete = null;
			lopMCC.marrNewIDs = null;

			lopMCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKCostCenter_In_UserDecoration;
		larrParams = new java.lang.Object[1];
		larrParams[0] = UUID.fromString(costCenter.id);

		larrAuxUsers = new ArrayList<User>();

		try
		{
        	lrefUserDecs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsUsers = lrefUserDecs.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsUsers.next() )
			{
				lobjUser = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrsUsers);
				lobjTmpUser = new User();
				lobjTmpUser.id = lobjUser.getKey().toString();
				lobjTmpUser.name = lobjUser.getBaseUser().getDisplayName();
				lobjTmpUser.username = lobjUser.getBaseUser().getUserName();
				lobjTmpUser.password = null; //JMMM: No way!
				lobjTmpUser.profile = new UserProfile();
				lobjTmpUser.profile.id = lobjUser.getBaseUser().getProfile().getKey().toString();
				lobjTmpUser.profile.name = lobjUser.getBaseUser().getProfile().getLabel();
				lobjTmpUser.costCenterId = ((UUID)lobjUser.getAt(2)).toString();
				lobjTmpUser.email = (String)lobjUser.getAt(1);
				larrAuxUsers.add(lobjTmpUser);
			}
		}
        catch (Throwable e)
        {
			try { lrsUsers.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsUsers.close();
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

		costCenter.id = lopMCC.marrModify[0].mid.toString();
		costCenter.code = lopMCC.marrModify[0].mstrCode;
		costCenter.name = lopMCC.marrModify[0].mstrName;
		costCenter.members = larrAuxUsers.toArray(new User[larrAuxUsers.size()]);

		return costCenter;
	}

	public void deleteCostCenter(String id)
		throws SessionExpiredException, BigBangException
	{
		ManageCostCenters lopMCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMCC = new ManageCostCenters(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopMCC.marrDelete = new ManageCostCenters.CostCenterData[1];
			lopMCC.marrDelete[0] = lopMCC.new CostCenterData();
			lopMCC.marrDelete[0].mid = UUID.fromString(id);
			lopMCC.marrDelete[0].mstrCode = null;
			lopMCC.marrDelete[0].mstrName = null;
			lopMCC.marrCreate = null;
			lopMCC.marrModify = null;
			lopMCC.marrNewIDs = null;

			lopMCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
