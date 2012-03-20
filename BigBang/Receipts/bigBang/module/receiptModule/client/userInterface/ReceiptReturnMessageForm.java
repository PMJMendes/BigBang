package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptReturnMessageForm extends FormView<ReturnMessage>{

	private TypifiedTextFormField message = new TypifiedTextFormField();

	
	public ReceiptReturnMessageForm(){
		
		addSection("Detalhes da devolução");
		addFormField(message);
		
	}
	
	@Override
	public ReturnMessage getInfo() {
		
		ReturnMessage newMessage = new ReturnMessage();
		newMessage.subject = message.getValue().subject;
		newMessage.text = message.getValue().text;
		
		return newMessage;
	}

	@Override
	public void setInfo(ReturnMessage info) {
	
		TypifiedText newText = new TypifiedText();
		newText.text = info.text;
		newText.subject = info.subject;
		message.setValue(newText);
		
		
	}

}
