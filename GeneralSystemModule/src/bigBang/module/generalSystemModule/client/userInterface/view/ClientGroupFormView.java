package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ClientGroup;

public class ClientGroupFormView extends FormView<ClientGroup> {

	protected ExpandableListBoxFormField parentGroup;
	protected TextBoxFormField name;
	
	public ClientGroupFormView(){
		name = new TextBoxFormField("Nome");
		parentGroup = new ExpandableListBoxFormField("Grupo pai");
		
		addSection("Grupo de Clientes");
		
		addFormField(name);
		addFormField(parentGroup);
	}
	
	@Override
	public ClientGroup getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(ClientGroup info) {
		// TODO Auto-generated method stub
		
	}

}
