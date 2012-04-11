package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.DASRequest;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class DASRequestForm extends FormView<DASRequest>{

	private TextBoxFormField replyLimit;

	public DASRequestForm(){
		addSection("Declaração de Ausência de Sinistro");
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("70px");
		addFormField(replyLimit);
	}

	@Override
	public DASRequest getInfo() {
		DASRequest request = value;
		try{
			request.replylimit = Integer.parseInt(replyLimit.getValue());
		}catch( NumberFormatException e){
			return null;
		}

		return request;
	}

	@Override
	public void setInfo(DASRequest info) {
		replyLimit.setValue(""+info.replylimit);

	}

}
