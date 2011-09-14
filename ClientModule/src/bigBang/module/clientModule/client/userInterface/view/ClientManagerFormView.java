package bigBang.module.clientModule.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.dataAccess.UserDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.User;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class ClientManagerFormView extends FormView<User> implements UserDataBrokerClient {

	protected ExpandableListBoxFormField clientManager;
	protected TextBoxFormField name;
	protected TextBoxFormField username;
	protected TextBoxFormField userProfile;
	
	protected int userDataVersion;
	
	public ClientManagerFormView(){
		clientManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		clientManager.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				UserBroker userBroker = ((UserBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER));
				userBroker.getUser(event.getValue(), new ResponseHandler<User>() {
					
					@Override
					public void onResponse(User response) {
						ClientManagerFormView.this.setValue(response);
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		});
		name = new TextBoxFormField("Nome");
		username = new TextBoxFormField("Nome de utilizador");
		userProfile = new TextBoxFormField("Perfil");
		
		addSection("Gestor de Cliente");
		
		addFormField(clientManager);
		addFormField(name);
		addFormField(username);
		addFormField(userProfile);
		
		((UserBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER)).registerClient(this);
		
		setReadOnly(true);
		clientManager.setReadOnly(false);
	}

	@Override
	public User getInfo() {
		User user = new User();
		user.id = this.clientManager.getValue();
		return user;
	}

	@Override
	public void setInfo(User info) {
		if(info == null){
			clearInfo();
			return;
		}
		this.clientManager.setValue(info.id);
		this.name.setValue(info.name);
		this.username.setValue(info.username);
		this.userProfile.setValue(info.profile.name);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.USER)){
			this.userDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.USER)){
			return this.userDataVersion;
		}
		throw new RuntimeException("This object is not a client of broker for element with id : " + dataElementId);
	}

	@Override
	public void setUsers(User[] users) {
		for(int i = 0; i < users.length; i++) {
			User user = this.value;
			if(user != null && user.id != null && user.id.equalsIgnoreCase(users[i].id)) {
				this.setValue(users[i]);
				break;
			}
		}
	} 

	@Override
	public void addUser(User user) {
		return;
	}

	@Override
	public void updateUser(User alteredUser) {
		User user = this.value;
		if(user != null && user.id != null && user.id.equalsIgnoreCase(alteredUser.id)) {
			this.setValue(alteredUser);
		}
	}

	@Override
	public void removeUser(String userId) {
		this.clearInfo();
	}

}
