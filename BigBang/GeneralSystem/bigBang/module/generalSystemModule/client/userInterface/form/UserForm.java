package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.CostCenter;
import bigBang.definitions.shared.User;
import bigBang.definitions.shared.UserProfile;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.PasswordTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.interfaces.PrintService;

public class UserForm extends FormView<User> {

	protected TextBoxFormField name;
	protected TextBoxFormField username;
	protected TextBoxFormField email;
	protected ExpandableListBoxFormField role;
	protected PasswordTextBoxFormField password;
	protected ExpandableListBoxFormField costCenter;
	protected ExpandableListBoxFormField delegate;
	protected ListBoxFormField printers;

	private boolean printersInitialized = false;

	public UserForm(){
		super();
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome");
		username = new TextBoxFormField("Nome de Utilizador");
		password = new PasswordTextBoxFormField("Palavra-passe");
		email = new TextBoxFormField("E-mail");
		role = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER_PROFILE, "Perfil");
		costCenter = new ExpandableListBoxFormField(BigBangConstants.EntityIds.COST_CENTER, "Centro de Custo");
		delegate = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Delegado");
		printers = new ListBoxFormField("Impressora pré-definida");

		role.allowEdition(false);
		costCenter.allowEdition(false);		

		addFormField(name);
		addFormField(username);
		addFormField(password);
		addFormField(email);
		addFormField(role);
		addFormField(costCenter);
		addFormField(delegate);
		addFormField(printers);

		showPasswordField(false);

		clearInfo();

		setReadOnly(true);
		setValidator(new UserFormValidator(this));
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
		this.costCenter.clearValues();
		if(costCenters != null){
			for(int i = 0; i < costCenters.length; i++) {
				addCostCenterItem(costCenters[i].name, costCenters[i].id);
			}
		}
		this.costCenter.clear();
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

	@Override
	public User getInfo() {
		User info = value == null ? new User() : new User(value);
		info.name = this.name.getValue();
		info.username = this.username.getValue();
		info.password = this.password.getValue();
		info.email = this.email.getValue();
		if(info.profile == null)
			info.profile = new UserProfile();
		info.profile.id = this.role.getValue();
		info.profile.name = this.role.getSelectedItemText();
		info.costCenterId = this.costCenter.getValue();
		info.delegateId = this.delegate.getValue();
		info.defaultPrinter = this.printers.getValue();
		return info;
	}

	@Override
	public void setInfo(final User user) {
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

		if(user.profile == null)
			this.role.clear();
		else
			this.role.setValue(user.profile.id);

		if(user.costCenterId == null)
			costCenter.clear();
		else
			this.costCenter.setValue(user.costCenterId);

		if(user.delegateId == null)
			delegate.clear();
		else
			this.delegate.setValue(user.delegateId);
		
		if(user.defaultPrinter == null)
			this.printers.clear();

		if(printersInitialized){
			this.printers.setValue(user.defaultPrinter);
		}else{
			PrintService.Util.getInstance().getAvailablePrinterNames(new BigBangAsyncCallback<String[]>() {

				@Override
				public void onResponseSuccess(String[] result) {
					for(String printer : result) {
						printers.addItem(printer, printer);
					}
					printers.setValue(user.defaultPrinter);
					printersInitialized = true;
				}
			});
		}
	}

}
