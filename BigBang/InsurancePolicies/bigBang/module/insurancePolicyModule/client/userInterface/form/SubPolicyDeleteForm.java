package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubPolicyDeleteForm extends FormView<String> {

	protected TextAreaFormField reason;
	
	public SubPolicyDeleteForm(){
		addSection("Eliminação de Apólice Adesão");
		reason = new TextAreaFormField("Motivo");
		addFormField(reason);
		
		setValidator(new SubPolicyDeleteFormValidator(this));
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
