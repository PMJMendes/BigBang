package bigBang.module.generalSystemModule.server;

import Jewel.Engine.*;
import bigBang.library.server.*;
import bigBang.library.shared.*;
import bigBang.module.generalSystemModule.interfaces.*;
import bigBang.module.generalSystemModule.shared.*;

public class UserServiceImpl
	extends EngineImplementor
	implements UserService
{
	private static final long serialVersionUID = 1L;

	public User[] getUsers()
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public User getUser(String id)
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public String saveUser(User user)
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public String addUser(User user)
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public String deleteUser(User user)
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public User[] getUsersForCostCenterAssignment()
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}
	
	public UserRole[] getUserRoles()
		throws SessionExpiredException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

}
