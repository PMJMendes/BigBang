package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiptReturnForm extends FormView<ReturnMessage>{

	private TypifiedTextFormField message = new TypifiedTextFormField();
	private CheckBoxFormField send	= new CheckBoxFormField("Enviar mensagem");
	
	public ReceiptReturnForm(){
		
		addSection("Mensagem a enviar à seguradora");
		addFormField(send);
		message.setTypifiedTexts("RETURNRECEIPT");
		addFormField(message);
		
		send.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				if(event.getValue()){
					message.setReadOnly(false);
				}
				else{
					message.clear();
					message.setReadOnly(true);
				}
				
			}
		});
		
	}
	
	@Override
	public ReturnMessage getInfo() {
		
		ReturnMessage newMessage = getValue();
		if(!send.getValue()){
			return newMessage;
		}

		newMessage.motiveId = null; //TODO: Pôr aqui o ID do motivo (JMMM)
//		newMessage.subject = message.getValue().subject;
//		newMessage.text = message.getValue().text;
		
		return newMessage;
	}

	@Override
	public void setInfo(ReturnMessage info) {
	
		clear();
		send.setValue(false);
		
	}

	private void clear() {
		
		message.setValue(new TypifiedText());
		
	}

}
