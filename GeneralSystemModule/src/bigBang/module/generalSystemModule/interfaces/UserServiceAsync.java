package bigBang.module.generalSystemModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.User;
import bigBang.definitions.shared.UserProfile;
import bigBang.library.interfaces.Service;

public interface UserServiceAsync
	extends Service
{
	void getUsers(AsyncCallback<User[]> callback);
//	void getUser(String id, AsyncCallback<User> callback);
	void saveUser(User user, AsyncCallback<User> callback);
	void addUser(User user, AsyncCallback<User> callback);
	void deleteUser(User user, AsyncCallback<Void> callback);
//	void getUsersForCostCenterAssignment(AsyncCallback<User[]> callback);
	void getUserProfiles(AsyncCallback<UserProfile[]> callback);
}
