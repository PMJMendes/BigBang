package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.PasswordTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;
import bigBang.module.generalSystemModule.shared.formValidator.UserFormValidator;

public class UserForm extends FormView<User> {

	private TextBoxFormField name;
	private TextBoxFormField username;
	private TextBoxFormField email;
	private ListBoxFormField role;
	private PasswordTextBoxFormField password;
	private ListBoxFormField costCenter;
	
	private Button editCostCenterButton;
	private Button saveCostCenterButton;
	private Button deleteCostCenterButton;
	
	public UserForm(){
		super();
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome", new UserFormValidator.NameValidator());
		username = new TextBoxFormField("Nome de Utilizador", new UserFormValidator.UsernameValidator());
		password = new PasswordTextBoxFormField("Palavra-passe", new UserFormValidator.PasswordValidator());
		email = new TextBoxFormField("E-mail", new UserFormValidator.EmailValidator());
		role = new ListBoxFormField("Perfil", new UserFormValidator.UserProfileValidator());
		costCenter = new ListBoxFormField("Centro de Custo", new UserFormValidator.UserCostCenterValidator());

		addFormField(name);
		addFormField(username);
		addFormField(password);
		addFormField(email);
		addFormField(role);
		addFormField(costCenter);

		showPasswordField(false);
		
		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
		
		this.addButton(editCostCenterButton);
		this.addButton(saveCostCenterButton);
		this.addButton(deleteCostCenterButton);
		
		clearInfo();
		
		setReadOnly(true);
	}
	
	public void setUserProfiles(UserProfile[] profiles){
		this.role.clearValues();
		if(profiles != null){
			for(int i = 0; i < profiles.length; i++) {
				addRoleItem(profiles[i].name, profiles[i].id);
			}
		}
		this.role.clear();
	}
	
	public void setCostCenters(CostCenter[] costCenters) {
		this.role.clearValues();
		if(costCenters != null){
			for(int i = 0; i < costCenters.length; i++) {
				addCostCenterItem(costCenters[i].name, costCenters[i].id);
			}
		}
		this.role.clear();
	}

	public void showPasswordField(boolean show){
		this.password.setVisible(show);
	}
	
	public void addRoleItem(String item, String value) {
		if(!role.hasItem(item, value))
			role.addItem(item, value);
	}

	public void addCostCenterItem(String item, String value) {
		if(!costCenter.hasItem(item, value))
			costCenter.addItem(item, value);
	}

	public void selectRoleValue(String value) {
		role.setValue(value);
	}

	@Override
	public User getInfo() {
		User info = value == null ? new User() : value;
		info.name = this.name.getValue();
		info.username = this.username.getValue();
		info.password = this.password.getValue();
		info.email = this.email.getValue();
		if(info.profile == null)
			info.profile = new UserProfile();
		info.profile.id = this.role.getValue();
		info.profile.name = this.role.getSelectedItemText();
		info.costCenterId = this.costCenter.getValue();
		return info;
	}

	@Override
	public void setInfo(User user) {
		if(user == null){
			clearInfo();
			return;
		}
		if(user.name == null)
			name.clear();
		else
			this.name.setValue(user.name);
		
		if(user.username == null)
			username.clear();
		else
			this.username.setValue(user.username);
		
		if(user.password == null)
			password.clear();
		else
			this.password.setValue(user.password);
		
		if(user.email == null)
			email.clear();
		else
			this.email.setValue(user.email);
		
		if(user.profile == null || user.profile.id == null)
			role.clear();
		else
			this.role.setValue(user.profile.id);
		
		if(user.costCenterId == null)
			costCenter.clear();
		else
			this.costCenter.setValue(user.costCenterId);
	}
	
	public HasClickHandlers getSaveButton() {
		return saveCostCenterButton;
	}
	
	public HasClickHandlers getEditButton() {
		return editCostCenterButton;
	}
	
	public HasClickHandlers getDeleteButton() {
		return deleteCostCenterButton;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		saveCostCenterButton.setVisible(!readOnly);
		editCostCenterButton.setVisible(readOnly);
	}

}
