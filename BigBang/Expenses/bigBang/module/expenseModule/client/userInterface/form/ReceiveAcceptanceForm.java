package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiveAcceptanceForm extends FormView<Acceptance>{

	private NumericTextBoxFormField value;
	
	public ReceiveAcceptanceForm(){
		
		addSection("Aceitação");
		
		value = new NumericTextBoxFormField("Indemnização", true);
		
		value.setFieldWidth("175px");
		value.setUnitsLabel("€");
		
		addFormField(value);
	}
	
	@Override
	public Acceptance getInfo() {

		Acceptance acc = super.value;
		acc.settlement = value.getValue();
		
		return acc;
	}

	@Override
	public void setInfo(Acceptance info) {
		value.setValue(info.settlement);
	}

	
	
}
