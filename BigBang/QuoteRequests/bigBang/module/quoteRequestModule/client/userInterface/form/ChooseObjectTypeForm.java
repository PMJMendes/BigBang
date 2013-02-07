package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ChooseObjectTypeForm extends FormView<String>{

	protected ExpandableListBoxFormField objectType;
	
	public ChooseObjectTypeForm() {
		addSection("Escolha do tipo de Unidade de Risco");
		
		objectType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.OBJECT_TYPE, "Tipo de Unidade de Risco");
		
		addFormField(objectType);
		
		setValidator(new ChooseObjectTypeFormValidator(this));
	}
	
	@Override
	public String getInfo() {
		return objectType.getValue();
	}

	@Override
	public void setInfo(String info) {
		objectType.setValue(info);
	}
	
	

}
