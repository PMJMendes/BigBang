package bigBang.module.generalSystemModule.server;

import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;

import Jewel.Engine.Engine;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.GeneralSystemService;

public class GeneralSystemServiceImpl
	extends EngineImplementor
	implements GeneralSystemService
{
	private static final long serialVersionUID = 1L;

	public String getGeneralSystemProcessId()
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			return GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID().toString();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public String getGeneralSystemId()
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			return GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).getKey().toString();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
