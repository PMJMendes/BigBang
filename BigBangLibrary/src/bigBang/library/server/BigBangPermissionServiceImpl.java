package bigBang.library.server;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.User;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;
import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.Permission;
import bigBang.library.shared.SessionExpiredException;

public class BigBangPermissionServiceImpl
	extends EngineImplementor
	implements BigBangPermissionService
{
	private static final long serialVersionUID = 1L;

	public Permission[] getProcessPermissions(String dataObjectId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidUser;
		IProcess lrefProcess;
		UUID lidProfile;
		IOperation[] larrOps;
		ArrayList<Permission> larrResult;
		MasterDB ldb;
		Permission lobjAux;
		IStep lobjStep;
		int i;

		lidUser = Engine.getCurrentUser();
		if ( lidUser == null )
			throw new SessionExpiredException();

		lrefProcess = BigBangProcessServiceImpl.sGetProcessFromDataObject(UUID.fromString(dataObjectId));

		ldb = null;
		try
		{
			lidProfile = User.GetInstance(Engine.getCurrentNameSpace(), lidUser).getProfile().getKey();
			larrOps = lrefProcess.GetScript().getOperations();
			larrResult = new ArrayList<Permission>();
			ldb = new MasterDB();
			for ( i = 0; i < larrOps.length; i++ )
			{
				if ( !larrOps[i].checkPermission(lidProfile) )
					continue;

				lobjAux = new Permission();
				lobjAux.id = larrOps[i].getKey().toString();
				lobjStep = lrefProcess.GetOperation(larrOps[i].getKey(), ldb);
				if ( (lobjStep == null) || (Jewel.Petri.Constants.LevelID_Invalid.equals(lobjStep.GetLevel())) )
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
}
