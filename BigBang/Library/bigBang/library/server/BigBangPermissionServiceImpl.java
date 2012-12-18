package bigBang.library.server;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.User;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Interfaces.IStep;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import bigBang.definitions.shared.Permission;
import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class BigBangPermissionServiceImpl
	extends EngineImplementor
	implements BigBangPermissionService
{
	private static final long serialVersionUID = 1L;

	public static Permission[] sGetProcessPermissions(UUID pidProcess)
		throws BigBangException
	{
		IProcess lobjProcess;
		UUID lidProfile;
		IOperation[] larrOps;
		ArrayList<Permission> larrResult;
		MasterDB ldb;
		Permission lobjAux;
		IStep lobjStep;
		int i;

		ldb = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pidProcess);
			lidProfile = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser()).getProfile().getKey();
			larrOps = lobjProcess.GetScript().getOperations();
			larrResult = new ArrayList<Permission>();
			ldb = new MasterDB();
			for ( i = 0; i < larrOps.length; i++ )
			{
				if ( !larrOps[i].checkPermission(lidProfile) )
					continue;

				lobjAux = new Permission();
				lobjAux.id = larrOps[i].getKey().toString();
				lobjStep = lobjProcess.GetOperation(larrOps[i].getKey(), ldb);
				if ( (!lobjProcess.IsRunning() && !Jewel.Petri.Constants.LevelID_Override.equals(lobjStep.GetLevel())) ||
						(lobjStep == null) || (Jewel.Petri.Constants.LevelID_Invalid.equals(lobjStep.GetLevel())) )
					lobjAux.instanceId = null;
				else
					lobjAux.instanceId = lobjStep.getKey().toString();
				larrResult.add(lobjAux);
			}
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult.toArray(new Permission[larrResult.size()]);
	}

	public static Permission[] sGetScriptPermissions(UUID pidScript)
		throws BigBangException
	{
		IScript lrefScript;
		UUID lidProfile;
		IOperation[] larrOps;
		ArrayList<Permission> larrResult;
		MasterDB ldb;
		Permission lobjAux;
		int i;

		ldb = null;
		try
		{
			lrefScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), pidScript);
			lidProfile = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser()).getProfile().getKey();
			larrOps = lrefScript.getOperations();
			larrResult = new ArrayList<Permission>();
			ldb = new MasterDB();
			for ( i = 0; i < larrOps.length; i++ )
			{
				if ( !larrOps[i].checkPermission(lidProfile) )
					continue;

				lobjAux = new Permission();
				lobjAux.id = larrOps[i].getKey().toString();
				lobjAux.instanceId = null;
				larrResult.add(lobjAux);
			}
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult.toArray(new Permission[larrResult.size()]);
	}

	public Permission[] getProcessPermissions(String dataObjectId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetProcessPermissions(BigBangProcessServiceImpl.sGetProcessFromDataObject(UUID.fromString(dataObjectId)).getKey());
	}

	public Permission[] getGeneralOpPermissions(String dataTypeId)
		throws SessionExpiredException, BigBangException
	{
			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

			return sGetScriptPermissions(BigBangProcessServiceImpl.sGetScriptFromObjectType(UUID.fromString(dataTypeId)).getKey());
	}
}
