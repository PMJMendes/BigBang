package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalInfoRequestForm extends FormView<ExternalInfoRequest>{

	TextBoxFormField requestSubject = new TextBoxFormField("Assunto do pedido");
	TextBoxFormField replyLimit = new TextBoxFormField("Número de dias");
	IncomingMessageFormField messageFormField = new IncomingMessageFormField();

	
	public ExternalInfoRequestForm() {
		
		addSection("Detalhes do Pedido de Informação Externo");
		addFormField(requestSubject);
		addFormField(replyLimit);
		messageFormField.setReadOnly(true);
		addFormField(messageFormField);
		replyLimit.setFieldWidth("50px");
		
	}
	
	@Override
	public ExternalInfoRequest getInfo() {
		
		ExternalInfoRequest request = new ExternalInfoRequest();
		request.message = messageFormField.getValue();
		request.replylimit = Integer.parseInt(replyLimit.getValue());
		request.subject = requestSubject.getValue();
		
		return request;
		
	}

	@Override
	public void setInfo(ExternalInfoRequest info) {
		
		requestSubject.setValue(info.subject);
		messageFormField.setValue(info.message);
		replyLimit.setValue(info.replylimit+"");
		
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		if(requestSubject == null){
			return;
		}
		requestSubject.setReadOnly(readOnly);
		replyLimit.setReadOnly(readOnly);
	}
	
	public IncomingMessageFormField getIncomingMessageFormField(){
		return messageFormField;
	}

}
