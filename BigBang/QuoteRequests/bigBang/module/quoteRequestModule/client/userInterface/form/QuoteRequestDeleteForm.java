package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.view.FormView;

public class QuoteRequestDeleteForm extends FormView<String> {

	private TextAreaFormField reason;
	
	public QuoteRequestDeleteForm(){
		addSection("Eliminação de Consulta de Mercado");
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
