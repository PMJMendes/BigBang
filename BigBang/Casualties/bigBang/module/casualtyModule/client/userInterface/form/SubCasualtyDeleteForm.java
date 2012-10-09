package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubCasualtyDeleteForm extends FormView<String> {

	protected TextAreaFormField reason;
	
	public SubCasualtyDeleteForm(){
		reason = new TextAreaFormField();
		addSection("Eliminação de Sub-Sinistro");
		addFormField(reason);
		reason.setMandatory(true);
		
		setValidator(new SubCasualtyDeleteFormValidator(this));
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
