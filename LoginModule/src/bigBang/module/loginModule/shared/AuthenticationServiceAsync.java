package bigBang.module.loginModule.shared;

import bigBang.library.shared.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync extends Service {
	void login(String username, String password, AsyncCallback<String> callback);
}
