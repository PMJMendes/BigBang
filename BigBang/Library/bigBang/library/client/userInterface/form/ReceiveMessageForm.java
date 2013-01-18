package bigBang.library.client.userInterface.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.ReceiveMessageFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReceiveMessageForm extends FormView<Conversation>{

	protected ExpandableListBoxFormField requestType;
	protected RadioButtonFormField expectsResponse;
	protected NumericTextBoxFormField replyLimit;
	protected ReceiveMessageFormField messageFormField = new ReceiveMessageFormField();
	protected TextBoxFormField subject;


	public ReceiveMessageForm() {

		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Mensagem");
		subject = new TextBoxFormField("Tópico");
		expectsResponse = new RadioButtonFormField("Espera resposta");
		expectsResponse.addOption("YES", "Sim");
		expectsResponse.addOption("NO", "Não");
		
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("70px");
		
		expectsResponse.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equals("YES")){
					replyLimit.clear();
					replyLimit.setEditable(true);
					replyLimit.setReadOnly(false);
				}else{
					replyLimit.clear();
					replyLimit.setEditable(false);
					replyLimit.setReadOnly(true);
				}
			}
		});
		
		addSection("Detalhes do Processo de Mensagem");
		addFormField(requestType);
		addFormField(subject, false);
		addFormField(expectsResponse, true);
		addFormField(replyLimit, true);
		
		messageFormField.setReadOnly(false);
		
		addSection("Detalhes da Mensagem");
		addFormField(messageFormField);
		replyLimit.setFieldWidth("50px");
		
		expectsResponse.setValue("YES");

		setValidator(new ReceiveMessageFormValidator(this));
	}

	@Override
	public Conversation getInfo() {

		Conversation request = (value == null ? new Conversation() : value);
		
		request.requestTypeId = requestType.getValue();
		request.subject = subject.getValue();
		
		try{
			request.replylimit = replyLimit.getValue().intValue();
		}catch(Exception e){
			request.replylimit = null;
		}

		request.subject = subject.getValue();
		request.messages = new Message[1];
		request.messages[0] = messageFormField.getValue();
		request.messages[0].conversationId = request.id;
		return request;

	}

	@Override
	public void setInfo(Conversation info) {

		subject.setValue(info.subject);
		requestType.setValue(info.requestTypeId);
		messageFormField.setValue(info.messages[0]);
		replyLimit.setValue(info.replylimit == null ? null : (double)info.replylimit);
	
		if(requestType.getValue() != null){
			requestType.setReadOnly(true);
		}
		if(subject.getValue() != null){
			subject.setReadOnly(true);
		}

	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(subject == null){
			return;
		}
		subject.setReadOnly(readOnly);
		replyLimit.setReadOnly(readOnly);
		messageFormField.setReadOnly(readOnly);
	}

	public ReceiveMessageFormField getIncomingMessageFormField(){
		return messageFormField;
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		expectsResponse.setValue("YES");
	}

}
