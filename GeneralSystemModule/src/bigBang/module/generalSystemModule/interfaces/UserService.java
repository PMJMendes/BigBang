package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.shared.User;
import bigBang.definitions.shared.UserProfile;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("UserService")
public interface UserService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util
	{
		private static UserServiceAsync instance;
		public static UserServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(UserService.class);
			}
			return instance;
		}
	}

	public User[] getUsers() throws SessionExpiredException, BigBangException;
//	public User getUser(String id) throws SessionExpiredException, BigBangException;
	public User saveUser(User user) throws SessionExpiredException, BigBangException;
	public User addUser(User user) throws SessionExpiredException, BigBangException;
	public void deleteUser(String id) throws SessionExpiredException, BigBangException;
//	public User[] getUsersForCostCenterAssignment() throws SessionExpiredException, BigBangException;
	public UserProfile[] getUserProfiles() throws SessionExpiredException, BigBangException;
}
