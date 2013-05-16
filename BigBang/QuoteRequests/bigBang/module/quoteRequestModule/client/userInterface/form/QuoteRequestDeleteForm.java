package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class QuoteRequestDeleteForm extends FormView<String> {

	private ExpandableListBoxFormField reason;
	
	public QuoteRequestDeleteForm(){
		addSection("Eliminação de Consulta de Mercado");
		//reason = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INSURANCE_POLICY_VOID_MOTIVES, "Motivo");
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
