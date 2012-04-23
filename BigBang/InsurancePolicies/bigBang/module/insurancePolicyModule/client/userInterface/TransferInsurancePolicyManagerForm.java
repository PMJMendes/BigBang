package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class TransferInsurancePolicyManagerForm extends FormView<String> {
	
	protected ExpandableListBoxFormField manager;
	
	public TransferInsurancePolicyManagerForm(){
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Apólice");
		
		addSection("Informação da Transferência");
		addFormField(manager);
	}
	
	@Override
	public String getInfo() {
		return manager.getValue();
	}

	@Override
	public void setInfo(String info) {
		this.manager.setValue(info);
	}

}
