package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class QuoteRequestForm extends FormView<QuoteRequest> implements ClientProcessDataBrokerClient {

	protected ExpandableListBoxFormField processManager;
	protected TextBoxFormField daysUntilReply;
	protected CheckBoxFormField caseStudyFlag;
	protected TextAreaFormField notes;
	
	protected TextBoxFormField clientName;
	protected TextBoxFormField clientNumber;
	
	protected ClientProcessBroker clientBroker;
	
	public QuoteRequestForm(){
		processManager = new ExpandableListBoxFormField("Gestor do Processo");
		daysUntilReply = new TextBoxFormField("Prazo de resposta em dias");
		caseStudyFlag = new CheckBoxFormField("Estudo de caso");
		notes = new TextAreaFormField("Observações internas");
		
		addSection("Consulta de Mercado");
		
		addFormField(processManager);
		addFormField(daysUntilReply);
		addFormField(caseStudyFlag);
		addFormField(notes);
		
		clientName = new TextBoxFormField("Nome");
		clientNumber = new TextBoxFormField("Nº");
		
		clientName.setEditable(false);
		clientNumber.setEditable(false);
		
		addSection("Cliente");
		
		addFormField(clientName);
		addFormField(clientNumber);
		
		daysUntilReply.setFieldWidth("50");
		notes.setFieldHeight("150px");
		
		clientBroker = ((ClientProcessBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
		clientBroker.registerClient(this);
	}
	
	@Override
	public QuoteRequest getInfo() {
		QuoteRequest result = this.value;
		return result;
	}

	@Override
	public void setInfo(QuoteRequest info) {
		this.processManager.setValue(info.managerId);
		this.daysUntilReply.setValue(""+info.responseLimitInDays);
		this.caseStudyFlag.setValue(info.caseStudy);
		this.notes.setValue(info.internalNotes);
		clientBroker.getClient(info.clientId, new ResponseHandler<Client>() {
			
			@Override
			public void onResponse(Client response) {
				clientName.setValue(response.name);
				clientNumber.setValue(response.clientNumber);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	//Broker client methods
	
	//CLIENT
	protected int clientProcessDataVersion;

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			clientProcessDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			return clientProcessDataVersion;
		}
		return -1;
	}

	@Override
	public void addClient(Client client) {
		return;
	}

	@Override
	public void updateClient(Client client) {
		if(this.value.clientId != null && client.id.equalsIgnoreCase(this.value.clientId)){
			this.clientName.setValue(client.name);
			this.clientNumber.setValue(client.clientNumber);
		}
	}

	@Override
	public void removeClient(String clientId) {
		if(value.clientId != null && clientId.equalsIgnoreCase(value.clientId)){
			this.setValue(null, true);
		}
	}

}
