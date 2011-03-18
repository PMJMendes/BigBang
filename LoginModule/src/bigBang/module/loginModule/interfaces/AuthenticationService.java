package bigBang.module.loginModule.interfaces;

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
	
	public String login(String username, String password);
	
	public String login();
	
	public String logout();
	
	public String changePassword(String oldPassword, String newPassword);
	
}
