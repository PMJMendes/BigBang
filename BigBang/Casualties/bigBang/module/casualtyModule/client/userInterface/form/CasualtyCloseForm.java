package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CasualtyCloseForm extends FormView<String> {

	protected TextAreaFormField reason;
	
	public CasualtyCloseForm(){
		reason = new TextAreaFormField("Motivo");
		addSection("Encerramento");
		addFormField(reason);
	}
	
	@Override
	public String getInfo() {
		return reason.getValue();
	}

	@Override
	public void setInfo(String info) {
		reason.setValue(info);
	}

}
