package bigBang.library.client.userInterface.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userinterface.autocomplete.AutoCompleteTextListFormField;

public class InfoOrDocumentRequestForm extends FormView<InfoOrDocumentRequest> {
	
	protected ExpandableListBoxFormField requestType;
	protected TypifiedTextFormField text;
	protected TextBoxFormField replyLimit;
	protected AutoCompleteTextListFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;
	
	public InfoOrDocumentRequestForm(){
		addSection("Detalhes do Pedido");

		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Documento");
		text = new TypifiedTextFormField();
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("70px");
		forwardReply = new AutoCompleteTextListFormField(); //TODO "Forward/reply");
		internalCCAddresses = new TextBoxFormField("BCC (interno) Endereços separados por ';'");
		externalCCAddresses = new TextBoxFormField("CC (externo) Endereços separados por ';'");
		
		addFormField(requestType, true);
		addFormField(replyLimit);
		addFormField(forwardReply);
		addFormField(internalCCAddresses);
		addFormField(externalCCAddresses);
		addFormField(text);
		
		requestType.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				text.setTypifiedTexts(event.getValue());
			}
		});
	}
	
	@Override
	public InfoOrDocumentRequest getInfo() {
		if(value == null){
			value = new InfoOrDocumentRequest();
		}
		InfoOrDocumentRequest request = value;
		request.requestTypeId = requestType.getValue();
		TypifiedText requestText = text.getValue();
		request.subject = requestText.subject;
		request.text = requestText.text;
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
		TypifiedText requestText = new TypifiedText();
		requestText.subject = info.subject;
		requestText.text = info.text;
		text.setValue(requestText);
		replyLimit.setValue(info.replylimit+"");
		//forwardReply.setValue(info.forwardUserIds.toString()); TODO
		internalCCAddresses.setValue(info.internalBCCs);
		externalCCAddresses.setValue(info.externalCCs);
	}

}
