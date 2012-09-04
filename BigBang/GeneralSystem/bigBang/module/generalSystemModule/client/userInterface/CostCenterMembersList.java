package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CostCenterBroker;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.dataAccess.UserDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.User;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;

public class CostCenterMembersList extends FilterableList<User> implements UserDataBrokerClient {

	protected class Entry extends UserListEntry {
		public Entry(User user) {
			super(user);
		}
	}
	
	protected UserBroker broker;
	protected CostCenterBroker costCenterBroker;
	protected String ownerId;
	protected int version = 0;
	
	public CostCenterMembersList(){
		this.broker = (UserBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.USER);
		this.broker.registerClient(this);
		this.costCenterBroker = (CostCenterBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COST_CENTER);
		showFilterField(false);
	}

	public void setOwner(String ownerId) {
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else{
			costCenterBroker.getCostCenterMembers(ownerId, new ResponseHandler<Collection<User>>(){

				@Override
				public void onResponse(Collection<User> response) {
					clear();
					for(User user : response) {
						addEntry(user);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}
	
	protected void addEntry(User user){
		add(new Entry(user));
	}
	
	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		version = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return version;
	}

	@Override
	public void setUsers(User[] users) {
		return;
	}

	@Override
	public void addUser(User user) {
		if(this.ownerId != null && user.costCenterId.equalsIgnoreCase(this.ownerId)) {
			this.addEntry(user);
		}
	}

	@Override
	public void updateUser(User user) {
		for(ValueSelectable<User> selectable : this) {
			if(user.id.equalsIgnoreCase(selectable.getValue().id)) {
				selectable.setValue(user);
				break;
			}
		}
	}

	@Override
	public void removeUser(String userId) {
		for(ValueSelectable<User> selectable : this) {
			if(userId.equalsIgnoreCase(selectable.getValue().id)) {
				remove(selectable);
				break;
			}
		}
	}
	
}
