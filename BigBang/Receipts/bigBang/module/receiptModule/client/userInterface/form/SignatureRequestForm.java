package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SignatureRequestForm extends FormView<SignatureRequest>{

	protected NumericTextBoxFormField replyLimit;

	public SignatureRequestForm(){
		addSection("Pedido de assinatura");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)", false);
		replyLimit.setFieldWidth("70px");
		replyLimit.setMandatory(true);
		addFormField(replyLimit);
		
		setValidator(new SignatureRequestFormValidator(this));
	}

	@Override
	public SignatureRequest getInfo() {
		SignatureRequest signature = value;
		signature.replylimit = replyLimit.getValue().intValue();
		return signature;
	}

	@Override
	public void setInfo(SignatureRequest info) {
		replyLimit.setValue((double) info.replylimit);
	}

}
