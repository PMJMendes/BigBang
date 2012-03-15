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
		addFormField(messageFormField);
	}
	
	@Override
	public ExternalInfoRequest getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(ExternalInfoRequest info) {
		
		//messageFormField.setValue(info.message);
		
	}

}
