package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.clientModule.shared.ClientManager;

public class ClientManagerFormView extends FormView<ClientManager> {

	protected ExpandableListBoxFormField clientManager;
	protected TextBoxFormField name;
	protected TextBoxFormField username;
	protected TextBoxFormField userProfile;
	protected TextBoxFormField costCenter;
	protected TextBoxFormField email;
	
	public ClientManagerFormView(){
		clientManager = new ExpandableListBoxFormField("Gestor de Cliente");
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
	public ClientManager getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(ClientManager info) {
		// TODO Auto-generated method stub
		
	}

}
