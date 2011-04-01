package bigBang.module.generalSystemModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;

public interface UserServiceAsync
	extends Service
{
	void getUsers(AsyncCallback<User[]> callback);
//	void getUser(String id, AsyncCallback<User> callback);
	void saveUser(User user, AsyncCallback<User> callback);
	void addUser(User user, AsyncCallback<User> callback);
	void deleteUser(String id, AsyncCallback<Void> callback);
//	void getUsersForCostCenterAssignment(AsyncCallback<User[]> callback);
	void getUserProfiles(AsyncCallback<UserProfile[]> callback);
}
