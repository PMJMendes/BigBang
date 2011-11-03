package bigBang.module.loginModule.interfaces;

import bigBang.library.interfaces.Service;
import bigBang.module.loginModule.shared.LoginResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync
	extends Service
{
	void login(String username, String password, String domain, AsyncCallback<LoginResponse> callback);
	void login(String domain, AsyncCallback<LoginResponse> callback);
	void logout(AsyncCallback<String> callback);
	void changePassword(String oldPassword, String newPassword, AsyncCallback<String> callback);
}
