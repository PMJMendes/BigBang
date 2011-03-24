package bigBang.module.generalSystemModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserRole;

public interface UserServiceAsync extends Service {

	void getUsers(AsyncCallback<User[]> callback);

	void getUser(String id, AsyncCallback<User> callback);

	void saveUser(User user, AsyncCallback<String> callback);

	void addUser(User user, AsyncCallback<String> callback);

	void deleteUser(User user, AsyncCallback<String> callback);

	void getUsersForCostCenterAssignment(AsyncCallback<User[]> callback);
	
	void getUserRoles(AsyncCallback<UserRole[]> callback);

}
