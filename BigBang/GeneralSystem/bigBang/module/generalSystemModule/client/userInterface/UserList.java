package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.dataAccess.UserDataBrokerClient;
import bigBang.definitions.shared.User;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class UserList extends FilterableList<User> implements UserDataBrokerClient {
	
	protected int userDataVersion;
	protected UserBroker userBroker;
	protected ListHeader header;
	
	public UserList(){
		super();
		header = new ListHeader();
		header.setText("Utilizadores");
		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
		showFilterField(false);
		
		this.userBroker = (UserBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER);
		this.userBroker.registerClient(this);
	}

	@Override
	protected void onSizeChanged(){
		int size = this.size();
		String text;
		switch(size){
		case 0:
			text = "Sem Utilizadores";
			break;
		case 1:
			text = "1 Utilizador";
			break;
		default:
			text = size + " Utilizadores";
			break;
		}
		
		setFooterText(text);
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
		this.clear();
		for(int i = 0; i < users.length; i++) {
			add(new UserListEntry(users[i]));
		}
	}

	@Override
	public void addUser(User user) {
		add(0, new UserListEntry(user));
	}

	@Override
	public void updateUser(User user) {
		for(ValueSelectable<User> s : this) {
			if(s.getValue().id.equals(user.id)){
				s.setValue(user);
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

	/**
	 * Gets a reference to the 'new' button
	 * @return The reference to the 'new' button
	 */
	public HasClickHandlers getNewButton(){
		return header.getNewButton();
	}
	
	/**
	 * Gets a reference to the 'refresh' button
	 * @return The reference to the 'refresh' button
	 */
	public HasClickHandlers getRefreshButton(){
		return header.getRefreshButton();
	}
	
//	@Override
//	protected void onAttach() {
//		super.onAttach();
//		userBroker.requireDataRefresh();
//		userBroker.getUsers(new ResponseHandler<User[]>() {
//			
//			@Override
//			public void onResponse(User[] response) {
//				return;
//			}
//			
//			@Override
//			public void onError(Collection<ResponseError> errors) {
//				return;
//			}
//		});
//	}
	
}
