package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class MassSignatureRequestForm extends FormView<Integer> {
	
	protected NumericTextBoxFormField replyLimit; 
	
	public MassSignatureRequestForm(){
		replyLimit = new NumericTextBoxFormField("Limite de Resposta", false);
		addSection("Pedir Assinatura para Recibos");
		addFormField(replyLimit);
	}

	@Override
	public Integer getInfo() {
		Double replyLimitDouble = replyLimit.getValue();
		Integer result = replyLimitDouble == null ? null : new Integer(replyLimitDouble.intValue());
		return result;
	}

	@Override
	public void setInfo(Integer info) {
		replyLimit.setValue(info == null ? null : new Double(info));
	}

}
