package bigBang.library.client.userInterface;

import java.util.Arrays;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.autocomplete.AutoCompleteTextListFormField;

public class OutgoingMessageFormField extends FormField<OutgoingMessage>{

	protected TypifiedTextSelector text;
	protected ListBoxFormField to;
	protected AutoCompleteTextListFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;

	private boolean readOnly = false;
	
	public OutgoingMessageFormField(){

		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);

		to = new ListBoxFormField("Destinatário");
		to.setFieldWidth("400px");
		forwardReply = new AutoCompleteTextListFormField("Utilizadores a envolver no processo");
		internalCCAddresses = new TextBoxFormField("BCC Endereços separados por ';'");
		externalCCAddresses = new TextBoxFormField("CC Endereços separados por ';'");
		text = new TypifiedTextSelector();
		text.setTypifiedTexts("OUTGOING_MESSAGE");
		text.setHeight("300px");
		
		
		wrapper.add(to);
		wrapper.add(forwardReply);
		wrapper.add(internalCCAddresses);
		wrapper.add(externalCCAddresses);
		wrapper.add(text);
	
	}

	@Override
	public void clear() {
		
		to.clear();
		forwardReply.clear();
		internalCCAddresses.clear();
		externalCCAddresses.clear();
		text.clear();

	}

	@Override
	protected void setReadOnlyInternal(boolean readonly) {
		
		readOnly = readonly;
		to.setReadOnly(readonly);
		forwardReply.setReadOnly(readonly);
		internalCCAddresses.setReadOnly(readonly);
		externalCCAddresses.setReadOnly(readonly);
		text.setReadOnly(readonly);

	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setLabelWidth(String width) {
		return;

	}
	
	@Override
	public OutgoingMessage getValue() {
		OutgoingMessage message = new OutgoingMessage();
		
		message.toContactInfoId = to.getValue();
		message.externalCCs = externalCCAddresses.getValue();
		message.internalBCCs = internalCCAddresses.getValue();
		
		Object[] temp = forwardReply.getValue().toArray();
		String[] forwardFN = new String[temp.length];
	
		
		for(int i = 0; i<forwardFN.length; i++){
			forwardFN[i] = (String)temp[i];
		}

		message.forwardUserFullNames = forwardFN;
		message.subject = text.getValue().subject;
		message.text = "<html><body>" + text.getValue().text + "</body></html>";
		
		return message;
	}
	
	@Override
	public void setValue(OutgoingMessage message, boolean fireEvents) {
		to.setValue(message.toContactInfoId);
		if(message.forwardUserFullNames!= null){
			forwardReply.setValue(Arrays.asList(message.forwardUserFullNames));
		}
		internalCCAddresses.setValue(message.internalBCCs);
		externalCCAddresses.setValue(message.externalCCs);
		TypifiedText tText = new TypifiedText();
		tText.subject = message.subject;
		tText.text = message.text;
		tText.tag = "OUTGOING_MESSAGE";
		text.setValue(tText);
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

	public void setUserList(String[] displayNames) {
		forwardReply.setSuggestions(Arrays.asList(displayNames));
	}

	
	@Override
	public void focus() {
		to.focus();
	}
	
}
