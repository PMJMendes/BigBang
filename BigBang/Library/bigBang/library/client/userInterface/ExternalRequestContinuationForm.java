package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalRequestContinuationForm extends FormView<Incoming> {

	protected TextBoxFormField replyLimit;
	protected IncomingMessageFormField message;
	
	public ExternalRequestContinuationForm(){
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		message = new IncomingMessageFormField();
		
		addSection("Detalhes da Continuação do Pedido de Informação");
		addFormField(replyLimit);
		addFormField(message);
	}
	
	@Override
	public Incoming getInfo() {
		Incoming result = getValue();
		if(result != null){
			int limit = -1;
			try{
				limit = Integer.parseInt(replyLimit.getValue());
			}catch (Exception e) {
				//
			}
			result.replylimit = limit;
			result.message = message.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(Incoming info) {
		if(info == null) {
			clearInfo();
		}else{
			replyLimit.setValue(info.replylimit + "");
			message.setValue(info.message);
		}
	}

}
