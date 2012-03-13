package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalInfoRequestForm extends FormView<ExternalInfoRequest>{

	IncomingMessageFormField messageFormField = new IncomingMessageFormField();

	
	public ExternalInfoRequestForm() {
		addSection("Detalhes do Pedido de Informação Externo");
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
