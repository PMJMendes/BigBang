package bigBang.module.loginModule.server;

import bigBang.module.loginModule.shared.AuthenticationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {
	
	private static final long serialVersionUID = 1L;

	public String login(String username, String password) {
		return "client login ok";
	}
}
