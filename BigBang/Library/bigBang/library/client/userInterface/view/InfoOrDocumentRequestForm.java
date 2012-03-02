package bigBang.library.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.TypifiedText;
import bigBang.definitions.shared.User;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.autocomplete.AutoCompleteTextListFormField;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class InfoOrDocumentRequestForm extends FormView<InfoOrDocumentRequest> {

	protected ExpandableListBoxFormField requestType;
	protected TypifiedTextFormField text;
	protected ListBoxFormField to;
	protected TextBoxFormField replyLimit;
	protected AutoCompleteTextListFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;

	public InfoOrDocumentRequestForm(){
		addSection("Detalhes do Pedido");

		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Documento");
		text = new TypifiedTextFormField();
		to = new ListBoxFormField("Destinatário");
		to.setFieldWidth("400px");
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("70px");
		forwardReply = new AutoCompleteTextListFormField("Forward/Reply");
		internalCCAddresses = new TextBoxFormField("BCC (interno) Endereços separados por ';'");
		externalCCAddresses = new TextBoxFormField("CC (externo) Endereços separados por ';'");

		addFormField(requestType, true);
		addFormField(replyLimit);
		addFormField(to);
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
		
		((UserBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.USER)).getUsers(new ResponseHandler<User[]>() {
			
			@Override
			public void onResponse(User[] response) {
				List<String> suggestions = new ArrayList<String>();
				for(int i = 0; i < response.length; i++) {
					suggestions.add(response[i].name);
				}
				forwardReply.setSuggestions(suggestions);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
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
		request.message.toContactInfoId = to.getValue();
		String limit = replyLimit.getValue();
		try{
			request.replylimit = limit == null ? null : new Integer(limit);
		}catch(NumberFormatException e){
			request.replylimit = null;
		}
		
		Collection<String> selectedUsers = this.forwardReply.getValue();
		String[] users = new String[selectedUsers.size()];
		int i = 0;
		for(String user : selectedUsers){
			users[i] = user;
			i++;
		}
		
		request.message.forwardUserFullNames = users;
		request.message.internalBCCs = internalCCAddresses.getValue();
		request.message.externalCCs = externalCCAddresses.getValue();
		request.message.subject = requestText.subject;
		request.message.text = requestText.text;
		return request;
	}

	@Override
	public void setInfo(InfoOrDocumentRequest info) {
		if(info == null) {
			clearInfo();
			return;
		}
		to.setValue(info.message.toContactInfoId);
		requestType.setValue(info.requestTypeId);
		TypifiedText requestText = new TypifiedText();
		requestText.subject = info.message.subject;
		requestText.text = info.message.text;
		text.setValue(requestText);
		replyLimit.setValue(info.replylimit == null ? null : info.replylimit.toString());
		
		List<String> users = new ArrayList<String>();
		int nUsers = info.message.forwardUserFullNames == null ? 0 : info.message.forwardUserFullNames.length;
		for(int i = 0; i < nUsers; i++) {
			users.add(info.message.forwardUserFullNames[i]);
		}
		forwardReply.setValue(users);

		internalCCAddresses.setValue(info.message.internalBCCs);
		externalCCAddresses.setValue(info.message.externalCCs);
	}

	public void setAvailableContacts(Contact[] contacts) {
		this.to.clearValues();
		if(contacts != null){
			for(int i = 0; i < contacts.length; i++){
				Contact contact = contacts[i];
				ContactInfo info = contact.info[0];
				this.to.addItem(contact.name + " <" + info.value + ">", info.id);
			}
		}
	}
}
