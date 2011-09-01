package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class TransferClientManagerForm extends FormView<String> {
	
	protected ExpandableListBoxFormField manager;
	
	public TransferClientManagerForm(){
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Cliente");
		
		addSection("Informação da Transferência");
		addFormField(manager);
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(String info) {
		// TODO Auto-generated method stub

	}

}
