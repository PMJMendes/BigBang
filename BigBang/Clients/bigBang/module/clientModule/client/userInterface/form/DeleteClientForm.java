package bigBang.module.clientModule.client.userInterface.form;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.clientModule.shared.ModuleConstants;

public class DeleteClientForm extends FormView<String> {

	protected ExpandableListBoxFormField motive;
	
	public DeleteClientForm(){
		motive = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CLIENT_DELETION_MOTIVES, "Motivo");
		
		addSection("Dados da Eliminação de Cliente");
		addFormField(motive);
	}

	@Override
	public String getInfo() {
		return motive.getValue();
	}

	@Override
	public void setInfo(String info) {
		motive.setValue(info);
	}

}
