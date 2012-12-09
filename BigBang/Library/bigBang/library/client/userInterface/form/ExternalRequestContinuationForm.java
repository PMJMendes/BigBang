package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
import bigBang.library.client.userInterface.ReceiveMessageFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalRequestContinuationForm extends FormView<Incoming> {

	protected NumericTextBoxFormField replyLimit;
	protected ReceiveMessageFormField message;

	public ExternalRequestContinuationForm(){
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)", false);
		message = new ReceiveMessageFormField();

		addSection("Detalhes da continuação do processo de mensagem");
		addFormField(replyLimit);
		addFormField(message);
		
		setValidator(new ExternalRequestContinuationFormValidator(this));
	}

	@Override
	public Incoming getInfo() {
		Incoming result = getValue();
		result.replylimit = replyLimit.getValue().intValue();
//		result.message = message.getValue();
		return result;
	}

	@Override
	public void setInfo(Incoming info) {
		if(info == null) {
			clearInfo();
		}else{
			replyLimit.setValue(info.replylimit == null ? null : (double)info.replylimit);
//			message.setValue(info.message);
		}
	}

}
