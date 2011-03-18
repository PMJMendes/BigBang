package bigBang.module.loginModule.server;

import bigBang.module.loginModule.interfaces.AuthenticationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {
	
	private static final long serialVersionUID = 1L;

	public String login(String username, String password) {
		return "client login ok";
	}

	@Override
	public String login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		return null;
	}
}
