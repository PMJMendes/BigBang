package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SignatureRequestForm extends FormView<SignatureRequest>{

	private TextBoxFormField replyLimit;

	public SignatureRequestForm(){
		addSection("Pedido de assinatura");
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("70px");
		addFormField(replyLimit);
	}

	@Override
	public SignatureRequest getInfo() {
		SignatureRequest signature = value;
		try{
			signature.replylimit = Integer.parseInt(replyLimit.getValue());
		}catch(NumberFormatException e){
			
		}
		return signature;
	}

	@Override
	public void setInfo(SignatureRequest info) {
		replyLimit.setValue(""+info.replylimit);
	}

}
