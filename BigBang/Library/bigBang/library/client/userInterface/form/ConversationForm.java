package bigBang.library.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class ConversationForm extends FormView<Conversation>{

	protected ExpandableListBoxFormField requestType;
	protected TextBoxFormField subject;
	private TextBoxFormField pendingAction;
	protected NumericTextBoxFormField replyLimit;
	private RichTextAreaFormField text;
	private String directionText = "";
	private TextBoxFormField messageSubject;
	public FormViewSection messageSection;


	public ConversationForm() {
		addSection("Troca de Mensagens");
		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Pedido");
		subject = new TextBoxFormField("Assunto");
		pendingAction = new TextBoxFormField("Acção Pendente");
		pendingAction.setFieldWidth("150px");
		pendingAction.setEditable(false);
		replyLimit = new NumericTextBoxFormField("Prazo", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("50px");
		
		addFormField(subject, true);
		addFormField(requestType, true);
		addLineBreak();
		addFormField(pendingAction, true);
		addFormField(replyLimit, true);
		
		messageSection = new FormViewSection("");
		addSection(messageSection);

		messageSubject = new TextBoxFormField("Assunto da Mensagem");
		messageSubject.setEditable(false);
		text = new RichTextAreaFormField();
		text.setEditable(false);
		
		messageSection.addFormField(messageSubject);
		messageSection.addFormField(text);
				
		setValidator(new ConversationFormValidator(this));
		
	}

	@Override
	public Conversation getInfo() {
		Conversation conv = value;

		conv.subject = subject.getValue();
		conv.requestTypeId = requestType.getValue();
		try{
			conv.replylimit = replyLimit.getValue().intValue();
		}catch(Exception e){
			conv.replylimit = null;
		}		
		return conv;
	}

	@Override
	public void setInfo(Conversation info) {
		
		subject.setValue(info.subject);
		requestType.setValue(info.requestTypeId);
		replyLimit.setValue(info.replylimit != null ? info.replylimit.doubleValue() : null);
		pendingAction.setValue(Conversation.Direction.INCOMING.equals(info.pendingDir) ? "Receber Mensagem" : "Enviar Mensagem");
		
		
	}
	
	public void setCurrentMessage(Message info){
		directionText = Conversation.Direction.INCOMING.equals(info.direction) ? " recebida" : " enviada";
		messageSection.setHeaderText("Mensagem nº. " + (info.order+1) + directionText + " a: " + info.date);
		text.setValue(info.text);
		messageSubject.setValue(info.subject);
		messageSection.setVisible(true);
	}

	public Message getMessage() {
		Message message = new Message();
		
		return message;
	}



}
