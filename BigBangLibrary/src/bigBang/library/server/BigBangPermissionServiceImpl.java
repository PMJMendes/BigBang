package bigBang.library.server;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Interfaces.IOperation;
import Jewel.Petri.Interfaces.IProcess;
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
		UUID lidProcess;
		IOperation[] larrOps;
		Permission[] larrResult;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidProcess = UUID.fromString(id);

		try
		{
			larrOps = ((IProcess)PNProcess.GetInstance(Engine.getCurrentNameSpace(), lidProcess)).GetScript().getOperations();
			larrResult = new Permission[larrOps.length];
			for ( i = 0; i < larrOps.length; i++ )
			{
				larrResult[i] = new Permission();
				larrResult[i].id = larrOps[i].getKey().toString();
				larrResult[i].instanceId = larrOps[i].GetStepInProcess(lidProcess).getKey().toString();
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult;
	}
}
