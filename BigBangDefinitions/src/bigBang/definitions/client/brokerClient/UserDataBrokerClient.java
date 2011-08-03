package bigBang.definitions.client.brokerClient;

import bigBang.definitions.client.types.User;
import bigBang.library.client.dataAccess.DataBrokerClient;

public interface UserDataBrokerClient extends
DataBrokerClient<User> {

/**
* Sends all the existing users to the broker client
* @param users The array with the users
*/
public void setUsers(User[] users);

/**
* Adds a user to the BrokerClient cache
* @param user The user to add
*/
public void addUser(User user);

/**
* Updates the current user info in the BrokerClient
* @param user The user information
*/
public void updateUser(User user);

/**
* Removes a user from the BrokerClient cache
* @param userId The user id
*/
public void removeUser(String userId);

}