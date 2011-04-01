package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.PasswordTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;

public class UserForm extends FormView {

	private TextBoxFormField name;
	private TextBoxFormField username;
	private TextBoxFormField email;
	private ListBoxFormField role;
	private PasswordTextBoxFormField password;
	private ListBoxFormField costCenter;
	
	private final String EMPTY_VALUE = "";
	
	private User user;

	public UserForm(UserProfile[] userRoles, CostCenter[] costCenters){
		super();
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome");
		username = new TextBoxFormField("Nome de Utilizador");
		password = new PasswordTextBoxFormField("Palavra-passe");
		email = new TextBoxFormField("E-mail");
		role = new ListBoxFormField("Perfil");
		costCenter = new ListBoxFormField("Centro de Custo");

		addFormField(name);
		addFormField(username);
		addFormField(password);
		addFormField(email);
		addFormField(role);
		addFormField(costCenter);

		addRoleItem("Não atribuído", EMPTY_VALUE);
		if(userRoles != null){
			for(int i = 0; i < userRoles.length; i++) {
				addRoleItem(userRoles[i].name, userRoles[i].id);
			}
		}

		addCostCenterItem("Não atribuído", EMPTY_VALUE);
		if(costCenters != null) {
			for(int i = 0; i < costCenters.length; i++) {
				addRoleItem(costCenters[i].name, costCenters[i].id);
			}
		}
		
		showPasswordField(false);
		setReadOnly(true);
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

	public void setUser(User user) {
		
	}

	@Override
	public Object getInfo() {
		//TODO
		return this.user;
	}

	@Override
	public void setInfo(Object info) {
		user = (User) info;
		name.setValue(user.name);
		username.setValue(user.username);
		email.setValue(user.email);
		if(user.profileId != null){
			role.setValue(user.profileId);
		}else{
			this.role.setValue(EMPTY_VALUE);
		}
		if(user.costCenterId != null){
			costCenter.setValue(user.costCenterId);
		}else{
			this.costCenter.setValue(EMPTY_VALUE);
		}
	}

	@Override
	public void clearInfo() {
		user = new User();
		name.setValue("");
		username.setValue("");
		email.setValue("");
		role.setValue(EMPTY_VALUE);
		costCenter.setValue(EMPTY_VALUE);
	}

}
