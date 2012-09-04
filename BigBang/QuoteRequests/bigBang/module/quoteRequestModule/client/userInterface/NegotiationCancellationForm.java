package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.OutgoingMessageFormField;
import bigBang.library.client.userInterface.view.FormView;

public class NegotiationCancellationForm extends FormView<Cancellation>{
	
	private ExpandableListBoxFormField internalMotive;
	private CheckBoxFormField sendResponse;
	private OutgoingMessageFormField message;
	
	public NegotiationCancellationForm(){
		
		addSection("Cancelamento de Negociação");
		internalMotive = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.INFO_REQUEST_CANCEL_MOTIVES , "Motivo");
		sendResponse = new CheckBoxFormField("Enviar mensagem à seguradora");
		sendResponse.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()){
					message.setReadOnly(false);
				}
				else{
					message.setReadOnly(true);
				}
			}
		});
		message = new OutgoingMessageFormField();
		addFormField(internalMotive, true);
		addFormField(sendResponse);
		addFormField(message);
		
	}

	@Override
	public Cancellation getInfo() {
		Cancellation cancel = new Cancellation();
		cancel = value;
		cancel.internalMotiveId = internalMotive.getValue();
		cancel.sendResponseToInsuranceAgency = sendResponse.getValue();
		if(cancel.sendResponseToInsuranceAgency){
			cancel.message = message.getValue();
		}
		return cancel;
	}

	@Override
	public void setInfo(Cancellation info) {
		
		internalMotive.setValue(info.internalMotiveId);
		sendResponse.setValue(info.sendResponseToInsuranceAgency);
		message.setValue(info.message);
		
	}
	
	public boolean sendsMessage(){
		return sendResponse.getValue();
	}
	
	public void setAvailableContacts(Contact[] contacts){
		message.setAvailableContacts(contacts);
	}

	public void setUserList(String[] displayNames) {
		message.setUserList(displayNames);
		
	}

}
