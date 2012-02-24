package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.Collection;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.dataAccess.UserDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.User;
import bigBang.library.client.BigBangAsyncCallback;
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
		this.cache.setThreshold(0);
	}

	@Override
	public void getUsers(final ResponseHandler<User[]> handler) {
		if(needsRefresh()) {
			service.getUsers(new BigBangAsyncCallback<User[]>() {

				@Override
				public void onResponseSuccess(User[] result) {
					cache.clear();
					for(int i = 0; i < result.length; i++){
						result[i].id = result[i].id.toLowerCase();
						cache.add(result[i].id, result[i]);
					}
					incrementDataVersion();
					for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
						((UserDataBrokerClient)c).setUsers(result);
						((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
					needsRefresh = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							"Could not fetch user list."
					});
					super.onResponseFailure(caught);
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
	public void getUser(final String userId,
			final ResponseHandler<User> handler) {
		if(!cache.contains(userId.toLowerCase())){
			this.requireDataRefresh();
			this.getUsers(new ResponseHandler<User[]>() {

				@Override
				public void onResponse(User[] response) {
					if(cache.contains(userId.toLowerCase())){
						handler.onResponse((User) cache.get(userId));
					}else{
						handler.onError(new String[]{"The requested user could not be found. id:" + userId});
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					for(ResponseError e : errors){
						GWT.log(e.description);
					}
				}
			});
		}else{
			handler.onResponse((User) cache.get(userId));
		}
	}

	@Override
	public void addUser(User user, final ResponseHandler<User> handler) {
		this.service.addUser(user, new BigBangAsyncCallback<User>() {

			@Override
			public void onResponseSuccess(User result) {
				result.id = result.id.toLowerCase();
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).addUser(result);
					((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						"Could not create user"	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateUser(User user,
			final ResponseHandler<User> handler) {
		this.service.saveUser(user, new BigBangAsyncCallback<User>() {

			@Override
			public void onResponseSuccess(User result) {
				result.id = result.id.toLowerCase();
				cache.update(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).updateUser(result);
					((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						"Could not update user"	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void removeUser(final String userId,
			final ResponseHandler<User> handler) {
		this.getUser(userId, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				UserBrokerImpl.this.service.deleteUser(response, new BigBangAsyncCallback<Void>() {

					@Override
					public void onResponseSuccess(Void result) {
						cache.remove(userId);
						incrementDataVersion();
						for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
							((UserDataBrokerClient)c).removeUser(userId);
							((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
						}
						handler.onResponse(null);
					}

					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[]{
								"Could not delete user"
						});
						super.onResponseFailure(caught);
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(errors);
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

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		getUser(itemId, new ResponseHandler<User>(){

			@Override
			public void onResponse(User response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).addUser(response);
					((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;				
			}

		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		requireDataRefresh();
		cache.remove(itemId);
		for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
			((UserDataBrokerClient)c).removeUser(itemId);
			((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		getUser(itemId, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<User> c : UserBrokerImpl.this.getClients()){
					((UserDataBrokerClient)c).updateUser(response);
					((UserDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

}
