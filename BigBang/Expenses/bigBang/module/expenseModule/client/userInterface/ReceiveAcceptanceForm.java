package bigBang.module.expenseModule.client.userInterface;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiveAcceptanceForm extends FormView<String>{

	private TextAreaFormField reason;
	
	public ReceiveAcceptanceForm(){
		
		addSection("Aceitação");
		
		reason = new TextAreaFormField("Motivo");
		
	}
	
	@Override
	public String getInfo() {
		
		String reason = this.reason.getValue();
		return reason;
	}

	@Override
	public void setInfo(String info) {
		reason.setValue(info);
	}

	
	
}
