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

public class ClientManagerFormView extends FormView<String> implements UserDataBrokerClient {

	protected ExpandableListBoxFormField clientManager;
	protected TextBoxFormField username;
	protected TextBoxFormField userProfile;
	protected UserBroker broker;
	
	protected int userDataVersion;
	
	public ClientManagerFormView(){
		clientManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		username = new TextBoxFormField("Nome de utilizador");
		userProfile = new TextBoxFormField("Perfil");

		addSection("Gestor de Cliente");
		
		addFormField(clientManager);
		addFormField(username);
		addFormField(userProfile);
		
		this.broker = ((UserBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER));
		this.broker.registerClient(this);
		
		setReadOnly(true);
		clientManager.setReadOnly(false);
		clientManager.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setValue(event.getValue());
			}
		});
	}

	@Override
	public String getInfo() {
		return this.clientManager.getValue();
	}

	@Override
	public void setInfo(String info) {
		if(info == null){
			clearInfo();
			return;
		}
		this.broker.getUser(info, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				clientManager.setValue(response.id, false);
				username.setValue(response.username);
				userProfile.setValue(response.profile.name);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
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
			String userId = this.value;
			if(userId != null && userId.equalsIgnoreCase(users[i].id)) {
				this.setValue(users[i].id);
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
		String userid = this.value;
		if(userid != null && userid.equalsIgnoreCase(alteredUser.id)) {
			this.setValue(alteredUser.id);
		}
	}

	@Override
	public void removeUser(String userId) {
		this.clearInfo();
	}

}
