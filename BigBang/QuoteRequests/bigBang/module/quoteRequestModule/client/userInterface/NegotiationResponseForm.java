package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.Negotiation.Response;
import bigBang.library.client.userInterface.IncomingMessageFormField;
import bigBang.library.client.userInterface.view.FormView;

public class NegotiationResponseForm extends FormView<Response>{
	
	public IncomingMessageFormField message;
	
	public NegotiationResponseForm(){
		
		addSection("Receber resposta da seguradora");
		message = new IncomingMessageFormField();
		addFormField(message);
		
	}
	
	@Override
	public Response getInfo() {

		Response response = new Response();
		response = value;
		response.message = message.getValue();
		return response;
		
	}

	@Override
	public void setInfo(Response info) {
	
		value = info;
		message.setValue(info.message);
		
	}

}
