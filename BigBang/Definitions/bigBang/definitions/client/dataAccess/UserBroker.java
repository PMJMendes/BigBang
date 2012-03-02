package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.User;

/**
 * The interface for a User DataBroker
 */
public interface UserBroker extends DataBrokerInterface<User> {

	/**
	 * Fetches all available Users
	 * @param handler The handler to be notified on response
	 */
	public void getUsers(ResponseHandler<User[]> handler);
	
	/**
	 * Fetches the User for a given id
	 * @param userId The User id
	 * @param handler The handler to be notified on response
	 */
	public void getUser(String userId, ResponseHandler<User> handler);

	/**
	 * Creates a new User
	 * @param user The user to be created
	 * @param handler The handler to be notified on response
	 */
	public void addUser(User user, ResponseHandler<User> handler);
	
	/**
	 * Updates the User for a given id
	 * @param user The User
	 * @param handler The handler to be notified on response
	 */
	public void updateUser(User user, ResponseHandler<User> handler);

	/**
	 * Removes the User for a given id
	 * @param userId The User id
	 * @param handler The handler to be notified on response
	 */
	public void removeUser(String userId, ResponseHandler<User> handler);

}