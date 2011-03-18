package bigBang.module.loginModule.interfaces;

import bigBang.library.client.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync extends Service {
	void login(String username, String password, AsyncCallback<String> callback);

	void login(AsyncCallback<String> callback);

	void logout(AsyncCallback<String> callback);

	void changePassword(String oldPassword, String newPassword,
			AsyncCallback<String> callback);
}
