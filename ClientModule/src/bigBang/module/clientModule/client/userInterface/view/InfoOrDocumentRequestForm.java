package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.ClientInfoOrDocumentRequest;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InfoOrDocumentRequestForm extends FormView<ClientInfoOrDocumentRequest> {
	
	protected ExpandableListBoxFormField documentType;
	protected RichTextAreaFormField text;
	protected TextBoxFormField replyLimit;
	protected TextBoxFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;
	
	public InfoOrDocumentRequestForm(){
		addSection("Detalhes do Pedido");

		documentType = new ExpandableListBoxFormField("", "Tipo de Documento"); //TODO
		text = new RichTextAreaFormField();
		text.setFieldWidth("630px");
		text.setFieldHeight("300px");
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("70px");
		forwardReply = new TextBoxFormField("Forward/reply");
		internalCCAddresses = new TextBoxFormField("CC interno");
		externalCCAddresses = new TextBoxFormField("CC externo");
		
		addFormField(documentType);
		addFormField(replyLimit);
		addFormField(forwardReply);
		addFormField(internalCCAddresses);
		addFormField(externalCCAddresses);
		addFormField(text);
	}
	
	@Override
	public ClientInfoOrDocumentRequest getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(ClientInfoOrDocumentRequest info) {
		// TODO Auto-generated method stub
	}

}
