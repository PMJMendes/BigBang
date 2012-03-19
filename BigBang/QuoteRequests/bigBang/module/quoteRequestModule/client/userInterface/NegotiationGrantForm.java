package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Negotiation.Grant;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.OutgoingMessageFormField;
import bigBang.library.client.userInterface.view.FormView;

public class NegotiationGrantForm extends FormView<Grant>{

	private OutgoingMessageFormField message;
	private DatePickerFormField effectDate;
	
	public NegotiationGrantForm(){
		
		addSection("Adjudicação de Negociação");
		effectDate = new DatePickerFormField("Data efectiva da adjudicação");
		message = new OutgoingMessageFormField();
		
		addFormField(effectDate);
		addFormField(message);
		
	}
	
	@Override
	public Grant getInfo() {
		Grant newGrant = value;
		
		newGrant.message = message.getValue();
		newGrant.effectDate = effectDate.getStringValue();
		
		return newGrant;
	}

	@Override
	public void setInfo(Grant info) {
		
		message.setValue(info.message);
		effectDate.setValue(info.effectDate);
		
	}

	public void setAvailableContacts(Contact[] result) {
		
		message.setAvailableContacts(result);
		
	}

	public void setUserList(String[] displayNames) {
		message.setUserList(displayNames);
	}
	

}
