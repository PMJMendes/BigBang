package bigBang.module.generalSystemModule.client.dataAccess;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.UserBroker;
import bigBang.definitions.client.brokerClient.UserDataBrokerClient;
import bigBang.definitions.client.types.User;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBroker;
import bigBang.library.client.dataAccess.DataBrokerClient;
import bigBang.library.client.response.ResponseHandler;
import bigBang.module.generalSystemModule.interfaces.UserService;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;

public class UserBrokerImpl extends DataBroker<User> implements UserBroker {

	protected UserServiceAsync service;
	protected int dataVersion;
	protected boolean needsRefresh = true;

	public UserBrokerImpl(){
		this(UserService.Util.getInstance());
	}

	public UserBrokerImpl(UserServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.USER;
		cache.setThreshold(0);
	}

	@Override
	public void getUsers(final ResponseHandler<User[]> handler) {
		if(needsRefresh()) {
			service.getUsers(new BigBangAsyncCallback<User[]>() {

				@Override
				public void onSuccess(User[] result) {
					cache.clear();
					for(int i = 0; i < result.length; i++){
						cache.add(result[i].id, result[i]);
					}

					for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
						((UserDataBrokerClient)c).setUsers(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
			});
		}else{
			int size = this.cache.getNumberOfEntries();
			User[] users = new User[size];

			int i = 0;
			for(Object o : this.cache.getEntries()){
				users[i] = (User) o;
				i++;
			}
			handler.onResponse(users);
		}
	}

	@Override
	public void getUser(String userId,
			ResponseHandler<User> handler) {
		if(!cache.contains(userId))
			throw new RuntimeException("The requested user could not be found locally. id:\"" + userId + "\"");
		handler.onResponse((User) cache.get(userId));
	}

	@Override
	public void addUser(User user, final ResponseHandler<User> handler) {
		this.service.addUser(user, new BigBangAsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				cache.add(result.id, result);
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).addUser(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateUser(User user,
			final ResponseHandler<User> handler) {
		this.service.saveUser(user, new BigBangAsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				cache.update(result.id, result);
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).updateUser(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeUser(final String userId,
			final ResponseHandler<User> handler) {
		this.service.deleteUser(userId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(userId);
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).removeUser(userId);
				}
				handler.onResponse(null);
			}
		});
	}

	@Override
	public void requireDataRefresh() {
		this.needsRefresh = true;
	}
	
	/**
	 * Returns whether or not the broker needs to refresh its data
	 * @return true if the data needs to be refreshed, false otherwise
	 */
	protected boolean needsRefresh(){
		return this.needsRefresh;
	}

}
