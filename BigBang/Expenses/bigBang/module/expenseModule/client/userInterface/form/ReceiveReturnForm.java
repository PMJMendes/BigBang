package bigBang.module.expenseModule.client.userInterface.form;

import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiveReturnForm extends FormView<ReturnEx>{

	protected TextAreaFormField reason;
	
	public ReceiveReturnForm(){
		
		addSection("Recusa");
		
		reason = new TextAreaFormField("Motivo");
		
		addFormField(reason);
		
		setValidator(new ReceiveReturnFormValidator(this));
	}
	
	@Override
	public ReturnEx getInfo() {

		ReturnEx returnEx = value;
		returnEx.reason = reason.getValue();
		
		return returnEx;
	}

	@Override
	public void setInfo(ReturnEx info) {
		reason.setValue(info.reason);
	}

	
	
}


