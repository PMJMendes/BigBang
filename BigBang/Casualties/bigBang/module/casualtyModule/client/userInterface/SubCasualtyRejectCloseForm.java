package bigBang.module.casualtyModule.client.userInterface;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubCasualtyRejectCloseForm extends FormView<String> {

	protected TextAreaFormField reason;
	
	public SubCasualtyRejectCloseForm(){
		reason = new TextAreaFormField("Motivo");
		addSection("Rejeição de Encerramento de Sub-Sinistro");
		reason.getNativeField().setSize("450px", "100px");
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
