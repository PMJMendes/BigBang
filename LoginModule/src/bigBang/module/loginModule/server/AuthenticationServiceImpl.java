package bigBang.module.loginModule.server;

import bigBang.library.server.EngineImplementor;
import bigBang.module.loginModule.interfaces.AuthenticationService;

public class AuthenticationServiceImpl
	extends EngineImplementor
	implements AuthenticationService
{
	private static final long serialVersionUID = 1L;

	public String login(String username, String password)
	{
		return username;
	}

	public String login()
	{
		return getThreadLocalRequest().getRemoteUser();
	}

	public String logout()
	{
		return null;
	}

	public String changePassword(String oldPassword, String newPassword)
	{
		return null;
	}
}
