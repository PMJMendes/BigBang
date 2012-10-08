package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.shared.DASRequest;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class DASRequestForm extends FormView<DASRequest>{

	protected NumericTextBoxFormField replyLimit;

	public DASRequestForm(){
		addSection("Declaração de Ausência de Sinistro");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)", false);
		replyLimit.setFieldWidth("70px");
		addFormField(replyLimit);
		
		setValidator(new DASRequestFormValidator(this));
	}

	@Override
	public DASRequest getInfo() {
		DASRequest request = value;
		try{
			request.replylimit = replyLimit.getValue() == null ? 0 : replyLimit.getValue().intValue();
		}catch( NumberFormatException e){
			return null;
		}

		return request;
	}

	@Override
	public void setInfo(DASRequest info) {
		replyLimit.setValue(new Double(info.replylimit));
	}

}
