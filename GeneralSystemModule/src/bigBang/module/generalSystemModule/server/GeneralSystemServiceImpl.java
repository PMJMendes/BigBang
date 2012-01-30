package bigBang.module.generalSystemModule.server;

import Jewel.Engine.Engine;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.GeneralSystemService;
import bigBang.module.generalSystemModule.shared.GeneralSystem;

public class GeneralSystemServiceImpl
	extends EngineImplementor
	implements GeneralSystemService
{
	private static final long serialVersionUID = 1L;

	public String getGeneralSystemProcessId()
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.GeneralSystem lobjGenSys;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjGenSys = com.premiumminds.BigBang.Jewel.Objects.GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()); 
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjGenSys.GetProcessID().toString();
	}

	public String getGeneralSystemId()
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.GeneralSystem lobjGenSys;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjGenSys = com.premiumminds.BigBang.Jewel.Objects.GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()); 
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjGenSys.getKey().toString();
	}

	public GeneralSystem getGeneralSystem()
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.GeneralSystem lobjGenSys;
		GeneralSystem lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjGenSys = com.premiumminds.BigBang.Jewel.Objects.GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()); 
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new GeneralSystem();
		lobjResult.id = lobjGenSys.getKey().toString();
		lobjResult.processId = lobjGenSys.GetProcessID().toString();
		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjGenSys.GetProcessID());

		return lobjResult;
	}
}
