package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InsurerAccountingExtraForm extends
		FormView<InsurerAccountingExtra> {

	protected NumericTextBoxFormField value;
	protected TextBoxFormField description;
	protected CheckBoxFormField isCommission, isTax;
	
	public InsurerAccountingExtraForm(){
		addSection("Informação Extra");
		
		value = new NumericTextBoxFormField("Valor", true);
		value.setUnitsLabel("€");
		description = new TextBoxFormField("Descrição");
		description.setFieldWidth("350px");
		isCommission = new CheckBoxFormField("Comissão");
		isTax = new CheckBoxFormField("Imposto de Selo");
		
		addFormField(value);
		addFormField(description);
		addFormField(isCommission, true);
		addFormField(isTax, true);
		
		isCommission.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isTax.setVisible(event.getValue() != null && event.getValue());
			}
		});
		isCommission.setValue(false, true);
	}
	

	@Override
	public InsurerAccountingExtra getInfo() {
		InsurerAccountingExtra result = getValue();
		
		if(result == null){
			result = new InsurerAccountingExtra();
		}
		
		result.text = description.getValue();
		result.value = value.getValue();
		result.hasTax = isTax.getValue();
		result.isCommissions = isCommission.getValue();
		
		return result;
	}

	@Override
	public void setInfo(InsurerAccountingExtra info) {
		if(info == null) {
			clearInfo();
		}else{
			value.setValue(info.value);
			description.setValue(info.text);
			isTax.setValue(info.hasTax);
			isCommission.setValue(info.isCommissions, true);
		}
	}

}
