package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InsurerAccountingExtraForm extends
		FormView<InsurerAccountingExtra> {

	protected NumericTextBoxFormField value;
	protected TextBoxFormField description;
	
	public InsurerAccountingExtraForm(){
		addSection("Informação Extra");
		
		value = new NumericTextBoxFormField("Valor", true);
		value.setUnitsLabel("€");
		description = new TextBoxFormField("Descrição");
		description.setFieldWidth("350px");
		
		addFormField(value);
		addFormField(description);
	}
	

	@Override
	public InsurerAccountingExtra getInfo() {
		InsurerAccountingExtra result = getValue();
		
		if(result == null){
			result = new InsurerAccountingExtra();
		}
		
		result.text = description.getValue();
		result.value = value.getValue();
		
		return result;
	}

	@Override
	public void setInfo(InsurerAccountingExtra info) {
		if(info == null) {
			clearInfo();
		}else{
			value.setValue(info.value);
			description.setValue(info.text);
		}
	}

}
