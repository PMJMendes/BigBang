package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;

public class InfoOrDocumentRequestForm extends FormView<InfoOrDocumentRequest> {
	
	protected ExpandableListBoxFormField requestType;
	protected RichTextAreaFormField text;
	protected TextBoxFormField replyLimit;
	protected TextBoxFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;
	
	public InfoOrDocumentRequestForm(){
		addSection("Detalhes do Pedido");

		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Documento");
		text = new RichTextAreaFormField();
		text.setFieldWidth("630px");
		text.setFieldHeight("300px");
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("70px");
		forwardReply = new TextBoxFormField("Forward/reply");
		internalCCAddresses = new TextBoxFormField("CC interno");
		externalCCAddresses = new TextBoxFormField("CC externo");
		
		addFormField(requestType);
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
		request.requestTypeId = requestType.getValue();
		request.text = text.getValue();
		request.replylimit = Integer.parseInt(replyLimit.getValue());
		request.forwardUserIds = new String[0];
		request.internalBCCs = internalCCAddresses.getValue();
		request.externalCCs = externalCCAddresses.getValue();
		return request;
	}

	@Override
	public void setInfo(InfoOrDocumentRequest info) {
		if(info == null) {
			clearInfo();
			return;
		}
		requestType.setValue(info.requestTypeId);
		text.setValue(info.text);
		replyLimit.setValue(info.replylimit+"");
		forwardReply.setValue(info.forwardUserIds.toString());
		internalCCAddresses.setValue(info.internalBCCs);
		externalCCAddresses.setValue(info.externalCCs);
	}

}
