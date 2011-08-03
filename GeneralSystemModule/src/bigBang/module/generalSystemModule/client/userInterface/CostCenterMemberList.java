package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.UserBroker;
import bigBang.definitions.client.brokerClient.UserDataBrokerClient;
import bigBang.definitions.client.types.User;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.response.ResponseError;
import bigBang.library.client.response.ResponseHandler;
import bigBang.library.client.userInterface.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CostCenterMemberList extends List<User> implements UserDataBrokerClient {

	private Button addButton;
	private Button removeButton;

	protected String costCenterId;

	protected UserBroker userBroker;
	protected int userDataVersion;

	public CostCenterMemberList(){
		super();

		HorizontalPanel toolbar = new HorizontalPanel();

		setHeaderWidget(toolbar);
		updateFooterLabel();

		this.userBroker = (UserBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER);
		this.userBroker.registerClient(this);
	}

	@Override
	protected void onSizeChanged(){
		updateFooterLabel();
	}

	private void updateFooterLabel(){
		int size = this.size();
		String text;
		switch(size){
		case 0:
			text = "Sem membros associados";
			break;
		case 1:
			text = "1 membro associado";
			break;
		default:
			text = size + " membros associados";
			break;
		}

		setFooterText(text);
	}

	public HasClickHandlers getAddButton() {
		return this.addButton;
	}

	public HasClickHandlers getRemoveButton() {
		return this.removeButton;
	}

	public void setCostCenterId(final String costCenterId) {
		if(costCenterId == null){
			this.costCenterId = null;
			clear();
			return;
		}

		if(this.costCenterId != null && this.costCenterId.equals(costCenterId))
			return;	
		this.costCenterId = costCenterId;
		clear();

		this.userBroker.getUsers(new ResponseHandler<User[]>() {

			@Override
			public void onResponse(User[] response) {
				setUsers(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.USER)){
			this.userDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.USER)){
			return this.userDataVersion;
		}
		return -1;
	}

	@Override
	public void setUsers(User[] users) {
		if(this.costCenterId != null){
			this.clear();
			for(int i = 0; i < users.length; i++) {
				if(users[i].costCenterId.equals(this.costCenterId))
					addUser(users[i]);
			}
		}
	}

	@Override
	public void addUser(User user) {
		add(new CostCenterMemberListEntry(user));
	}

	@Override
	public void updateUser(User user) {
		for(ValueSelectable<User> s : this) {
			if(s.getValue().id.equals(user.id)){
				if(user.costCenterId.equals(this.costCenterId)){
					s.setValue(user);
				}else{
					remove(s);
				}
				break;
			}
		}
	}

	@Override
	public void removeUser(String userId) {
		for(ValueSelectable<User> s : this) {
			if(s.getValue().id.equals(userId)){
				remove(s);
				break;
			}
		}
	}

}
