package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptReturnForm extends FormView<ReturnMessage>{

	private TypifiedTextFormField message = new TypifiedTextFormField();

	
	public ReceiptReturnForm(){
		
		addSection("Detalhes da devolução");
		message.setTypifiedTexts("RETURNRECEIPT");
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
	
		if(info == null){
			clear();
			return;
		}
		
		TypifiedText newText = new TypifiedText();
		newText.text = info.text;
		newText.subject = info.subject;
		message.setValue(newText);
		
		
	}

	private void clear() {
		
		ReturnMessage newMessage = new ReturnMessage();
		
		newMessage.text = "";
		newMessage.subject = "";
		newMessage.receiptId = "";
		
		setValue(newMessage);
		
	}

}
