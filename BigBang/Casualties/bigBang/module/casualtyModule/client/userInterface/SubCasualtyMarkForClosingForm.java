package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubCasualtyMarkForClosingForm extends FormView<String> {

	protected ExpandableListBoxFormField revisor;
	
	public SubCasualtyMarkForClosingForm(){
		revisor = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Revisor");
		
		addSection("Marcação para Encerramento");
		addFormField(revisor);
	}
	
	
	@Override
	public String getInfo() {
		return revisor.getValue();
	}

	@Override
	public void setInfo(String info) {
		revisor.setValue(info);
	}

}
