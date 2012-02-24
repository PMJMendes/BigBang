package bigBang.module.clientModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InfoOrDocumentRequestForm extends FormView<InfoOrDocumentRequest> {
	
	protected ExpandableListBoxFormField documentType;
	protected RichTextAreaFormField text;
	protected TextBoxFormField replyLimit;
	protected TextBoxFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;
	
	public InfoOrDocumentRequestForm(){
		addSection("Detalhes do Pedido");

		documentType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "Tipo de Documento");
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
	public InfoOrDocumentRequest getInfo() {
		if(value == null){
			value = new InfoOrDocumentRequest();
		}
		InfoOrDocumentRequest request = value;
		request.requestTypeId = documentType.getValue();
		request.replylimit = Integer.parseInt(replyLimit.getValue());
		request.message.forwardUserIds = new String[0];
		request.message.internalBCCs = internalCCAddresses.getValue();
		request.message.externalCCs = externalCCAddresses.getValue();
		request.message.text = text.getValue();
		return request;
	}

	@Override
	public void setInfo(InfoOrDocumentRequest info) {
		if(info == null) {
			clearInfo();
			return;
		}
		documentType.setValue(info.requestTypeId);
		text.setValue(info.message.text);
		replyLimit.setValue(info.replylimit+"");
		forwardReply.setValue(info.message.forwardUserIds.toString());
		internalCCAddresses.setValue(info.message.internalBCCs);
		externalCCAddresses.setValue(info.message.externalCCs);
	}

}
