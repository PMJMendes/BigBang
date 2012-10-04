package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
import bigBang.library.client.userInterface.IncomingMessageFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InfoOrDocumentRequestReplyForm extends FormView<Response> {

	private IncomingMessageFormField incomingMessage;
	
	public InfoOrDocumentRequestReplyForm(){
		incomingMessage = new IncomingMessageFormField();
		
		addSection("Resposta a Pedido de Informação");
		addFormField(incomingMessage);
		
		setValidator(new InfoOrDocumentRequestReplyFormValidator(this));
	}
	
	@Override
	public Response getInfo() {
		Response result = getValue();
		if(result != null) {
			result.message = incomingMessage.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(Response info) {
		if(info == null) {
			clearInfo();
		}else{
			incomingMessage.setValue(info.message);
		}
	}

}
