package bigBang.module.generalSystemModule.interfaces;

import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("UserService")
public interface UserService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static UserServiceAsync instance;
		public static UserServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(UserService.class);
			}
			return instance;
		}
	}
	
	public User[] getUsers();
	
	public User getUser(String id);
	
	public String saveUser(User user);
	
	public String addUser(User user);
	
	public String deleteUser(User user);
	
	public User[] getUsersForCostCenterAssignment();

	public UserRole[] getUserRoles();

}
