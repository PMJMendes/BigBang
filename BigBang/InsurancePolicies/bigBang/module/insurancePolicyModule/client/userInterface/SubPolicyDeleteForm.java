package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubPolicyDeleteForm extends FormView<String> {

	private TextAreaFormField reason;
	
	public SubPolicyDeleteForm(){
		addSection("Eliminação de Apólice Adesão");
		reason = new TextAreaFormField("Motivo");
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
