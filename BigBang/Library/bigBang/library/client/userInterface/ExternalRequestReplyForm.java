package bigBang.library.client.userInterface;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;
import bigBang.library.client.userInterface.view.FormView;

public class ExternalRequestReplyForm extends FormView<Outgoing> {

	protected CheckBoxFormField isFinal;
	protected NumericTextBoxFormField replyLimit;
	protected OutgoingMessageFormField message;

	public ExternalRequestReplyForm(){
		isFinal = new CheckBoxFormField("Finalizar Pedido");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("72px");
		message = new OutgoingMessageFormField();

		addSection("Detalhes da Resposta");
		addFormField(replyLimit, true);
		addFormField(isFinal, false);
		addFormField(message);
	}


	@Override
	public Outgoing getInfo() {
		Outgoing result = getValue();
		if(result != null) {
			result.isFinal = isFinal.getValue();
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
