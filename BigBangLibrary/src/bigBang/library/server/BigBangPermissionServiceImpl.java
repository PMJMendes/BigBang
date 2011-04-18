package bigBang.library.server;

import java.util.UUID;

import Jewel.Engine.Engine;
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
		IProcess lrefProcess;
		IOperation[] larrOps;
		Permission[] larrResult;
		IStep lobjStep;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lrefProcess = (IProcess)PNProcess.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(id));
			larrOps = lrefProcess.GetScript().getOperations();
			larrResult = new Permission[larrOps.length];
			for ( i = 0; i < larrOps.length; i++ )
			{
				larrResult[i] = new Permission();
				larrResult[i].id = larrOps[i].getKey().toString();
				lobjStep = lrefProcess.GetOperation(larrOps[i].getKey());
				larrResult[i].instanceId = (lobjStep == null ? null : lobjStep.getKey().toString());
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResult;
	}
}
