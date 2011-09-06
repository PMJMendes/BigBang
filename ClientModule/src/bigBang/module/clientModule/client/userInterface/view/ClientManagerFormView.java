package bigBang.module.clientModule.client.userInterface.view;

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.client.dataAccess.CostCenterBroker;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.CostCenter;
import bigBang.definitions.shared.User;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ClientManagerFormView extends FormView<User> {

	protected ExpandableListBoxFormField clientManager;
	protected TextBoxFormField name;
	protected TextBoxFormField username;
	protected TextBoxFormField userProfile;
	protected TextBoxFormField costCenter;
	protected TextBoxFormField email;
	
	public ClientManagerFormView(){
		clientManager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		clientManager.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				UserBroker userBroker = ((UserBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER));
				userBroker.getUser(clientManager.getValue(), new ResponseHandler<User>() {
					
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
		costCenter = new TextBoxFormField("Centro de custo");
		email = new TextBoxFormField("Email");
		
		addSection("Gestor de Cliente");
		
		addFormField(clientManager);
		addFormField(name);
		addFormField(username);
		addFormField(userProfile);
		addFormField(costCenter);
		addFormField(email);
		
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
		CostCenterBroker costCenterBroker = ((CostCenterBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.COST_CENTER));
		costCenterBroker.getCostCenter(info.costCenterId, new ResponseHandler<CostCenter>() {
			
			@Override
			public void onResponse(CostCenter response) {
				costCenter.setValue(response.name);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
		this.email.setValue(info.email);
	}

}
