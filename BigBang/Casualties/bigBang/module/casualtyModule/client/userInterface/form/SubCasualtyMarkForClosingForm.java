package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubCasualtyMarkForClosingForm extends FormView<String> {

	protected ExpandableListBoxFormField revisor;
	
	public SubCasualtyMarkForClosingForm(){
		revisor = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Revisor");
		
		addSection("Marcação para Encerramento");
		addFormField(revisor);
		
		setValidator(new SubCasualtyMarkForClosingFormValidator(this));
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
