package bigBang.module.generalSystemModule.server;

import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.interfaces.MediatorService;
import bigBang.module.generalSystemModule.shared.ComissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;

public class MediatorServiceImpl
	extends EngineImplementor
	implements MediatorService
{
	private static final long serialVersionUID = 1L;

	public Mediator[] getMediators()
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

//	public Mediator getMediator(String id)
//	{
//		return null;
//	}

	public Mediator saveMediator(Mediator mediator)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public Mediator createMediator(Mediator mediator)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public void deleteMediator(String id)
		throws SessionExpiredException, BigBangException
	{
	}

	public ComissionProfile[] getComissionProfiles()
		throws SessionExpiredException, BigBangException
	{
		return null;
	}
}
