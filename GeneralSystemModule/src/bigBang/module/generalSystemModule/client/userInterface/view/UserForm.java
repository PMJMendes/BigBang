package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.shared.userInterface.ListBoxFormField;
import bigBang.library.shared.userInterface.TextBoxFormField;
import bigBang.library.shared.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.User;

public class UserForm extends FormView {
	
	private TextBoxFormField name;
	private TextBoxFormField username;
	private TextBoxFormField email;
	private ListBoxFormField role;
	private TextBoxFormField costCenter;
	
	public UserForm(){
		super();
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome");
		username = new TextBoxFormField("Nome de Utilizador");
		email = new TextBoxFormField("E-mail");
		role = new ListBoxFormField();
		costCenter = new TextBoxFormField("Centro de Custo");
		
		addFormField(name);
		addFormField(username);
		addFormField(email);
		addFormField(role);
		addFormField(costCenter);
		
		setReadOnly(true);
	}

	public void setUser(User user) {
		name.setValue(user.name);
		username.setValue(user.username);
		email.setValue(user.email);
		role.setValue(user.role.name);
		costCenter.setValue("Não atribuído");
	}
}
