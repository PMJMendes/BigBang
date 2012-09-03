package bigBang.module.insurancePolicyModule.server;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.Object2;
import bigBang.definitions.shared.Policy2;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.interfaces.Policy2Service;

public class Policy2ServiceImpl
	extends EngineImplementor
	implements Policy2Service
{
	private static final long serialVersionUID = 1L;

	public Policy2 getEmptyPolicy(String subLineId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Object2 getEmptyObject(String subLineId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Policy2 getPolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Object2 getPolicyObject(String objectId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Policy2 editPolicy(Policy2 policy)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}
}
