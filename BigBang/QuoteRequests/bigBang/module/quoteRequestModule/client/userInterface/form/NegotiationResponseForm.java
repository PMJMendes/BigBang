package bigBang.module.quoteRequestModule.client.userInterface.form;

import bigBang.definitions.shared.Negotiation.Response;
import bigBang.library.client.userInterface.ReceiveMessageFormField;
import bigBang.library.client.userInterface.view.FormView;

public class NegotiationResponseForm extends FormView<Response>{
	
	public ReceiveMessageFormField message;
	
	public NegotiationResponseForm(){
		
		addSection("Receber resposta da seguradora");
		message = new ReceiveMessageFormField();
		addFormField(message);
		
	}
	
	@Override
	public Response getInfo() {

		Response response = new Response();
		response = value;
//		response.message = new IncomingMessage(message.getValue());
		return response;
		
	}

	@Override
	public void setInfo(Response info) {
	
		value = info;
//		message.setValue(info.message.toMessage());
		
	}

}
