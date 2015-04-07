package bigBang.module.loginModule.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.loginModule.shared.LoginDomain;
import bigBang.module.loginModule.shared.LoginResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("AuthenticationService")
public interface AuthenticationService extends RemoteService {

	public static class Util {
		private static AuthenticationServiceAsync instance;
		public static AuthenticationServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(AuthenticationService.class);
			}
			return instance;
		}
	}

	public LoginDomain[] getDomains() throws BigBangException;
	public LoginResponse login(String domain) throws BigBangException;
	public LoginResponse login(String username, String password, String domain) throws BigBangException;
	public String logout() throws BigBangException;
	public String changePassword(String oldPassword, String newPassword) throws BigBangException, SessionExpiredException;
	public LoginResponse getCurrentLoginData() throws BigBangException;
}
