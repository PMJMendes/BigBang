package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.OutgoingMessageFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalRequestReplyForm extends FormView<Outgoing> {

	protected CheckBoxFormField isFinal;
	protected NumericTextBoxFormField replyLimit;
	protected OutgoingMessageFormField message;

	public ExternalRequestReplyForm(){
		addSection("Detalhes do processo de mensagem");
		
		isFinal = new CheckBoxFormField("Finalizar processo");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)", false);
		replyLimit.setFieldWidth("72px");
		message = new OutgoingMessageFormField();

		addFormField(replyLimit, true);
		addFormField(isFinal, true);
		addSection("Detalhes da mensagem a enviar");
		addFormField(message);
		
		setValidator(new ExternalRequestReplyFormValidator(this));
	}


	@Override
	public Outgoing getInfo() {
		Outgoing result = getValue();
		if(result != null) {
			result.isFinal = isFinal.getValue();
			if(replyLimit.getValue() != null)
				result.replylimit = replyLimit.getValue().intValue();
			result.message = message.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(Outgoing info) {
		if(info == null) {
			clearInfo();
		}else{
			isFinal.setValue(info.isFinal);
			replyLimit.setValue(info.replylimit == null ? null : (double)info.replylimit);
			message.setValue(info.message);
		}
	}

	public void setAvailableContacts(Contact[] contacts){
		message.setAvailableContacts(contacts);
	}

	public void setUserList(String[] usernames) {
		message.setUserList(usernames);
	}

}
