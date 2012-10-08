package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalInfoRequestForm extends FormView<ExternalInfoRequest>{

	TextBoxFormField requestSubject = new TextBoxFormField("Assunto do pedido");
	NumericTextBoxFormField replyLimit = new NumericTextBoxFormField("Número de dias", false);
	IncomingMessageFormField messageFormField = new IncomingMessageFormField();


	public ExternalInfoRequestForm() {

		requestSubject.setMandatory(true);
		replyLimit.setMandatory(true);
		
		addSection("Detalhes do Pedido de Informação Externo");
		addFormField(requestSubject);
		addFormField(replyLimit);
		
		messageFormField.setReadOnly(false);
		addFormField(messageFormField);
		replyLimit.setFieldWidth("50px");

	}

	@Override
	public ExternalInfoRequest getInfo() {

		ExternalInfoRequest request = value;
		request.message = messageFormField.getValue();
		if(replyLimit.getValue() != null){
			request.replylimit = replyLimit.getValue().intValue();
		}

		request.subject = requestSubject.getValue();

		return request;

	}

	@Override
	public void setInfo(ExternalInfoRequest info) {

		requestSubject.setValue(info.subject);
		messageFormField.setValue(info.message);
		replyLimit.setValue(info.replylimit == null ? null : (double)info.replylimit);
		messageFormField.setValue(info.message);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(requestSubject == null){
			return;
		}
		requestSubject.setReadOnly(readOnly);
		replyLimit.setReadOnly(readOnly);
		messageFormField.setReadOnly(readOnly);
	}

	public IncomingMessageFormField getIncomingMessageFormField(){
		return messageFormField;
	}

}
