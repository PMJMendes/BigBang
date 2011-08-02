package bigBang.library.server;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;
import Jewel.Petri.Objects.PNProcess;
import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.Permission;
import bigBang.library.shared.SessionExpiredException;

public class BigBangPermissionServiceImpl
	extends EngineImplementor
	implements BigBangPermissionService
{
	private static final long serialVersionUID = 1L;

	public Permission[] getProcessPermissions(String id)
		throws SessionExpiredException, BigBangException
	{
		UUID lidUser;
		UUID lidProfile;
		IProcess lrefProcess;
		IOperation[] larrOps;
		ArrayList<Permission> larrResult;
		Permission lobjAux;
		IStep lobjStep;
		int i;

		lidUser = Engine.getCurrentUser();
		if ( lidUser == null )
			throw new SessionExpiredException();

		try
		{
			lidProfile = User.GetInstance(Engine.getCurrentNameSpace(), lidUser).getProfile().getKey();
			lrefProcess = (IProcess)PNProcess.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(id));
			larrOps = lrefProcess.GetScript().getOperations();
			larrResult = new ArrayList<Permission>();
			for ( i = 0; i < larrOps.length; i++ )
			{
				if ( !larrOps[i].checkPermission(lidProfile) )
					continue;

				lobjAux = new Permission();
				lobjAux.id = larrOps[i].getKey().toString();
				lobjStep = lrefProcess.GetOperation(larrOps[i].getKey());
				lobjAux.instanceId = (lobjStep == null ? null : lobjStep.getKey().toString());
				larrResult.add(lobjAux);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult.toArray(new Permission[larrResult.size()]);
	}
}
