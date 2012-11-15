package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;
import com.google.gwt.user.client.ui.StackPanel;

public class ClientChildrenPanel extends View  {

	protected Client client;
	protected int clientDataVersion;

	public ContactsList contactsList;
	public DocumentsList documentsList;
	public ClientPoliciesList insurancePoliciesList;
	public ClientQuoteRequestsList quoteRequestsList;
	public ClientCasualtyList casualtiesList;
	public HistoryList historyList;
	public SubProcessesList subProcessesList;
	public ClientDeadPoliciesList deadInsurancePoliciesList;

	public ClientChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		insurancePoliciesList = new ClientPoliciesList();
		quoteRequestsList = new ClientQuoteRequestsList();
		casualtiesList = new ClientCasualtyList();
		historyList = new HistoryList();
		subProcessesList = new SubProcessesList();
		deadInsurancePoliciesList = new ClientDeadPoliciesList();
		
		wrapper.add(this.contactsList, "Contactos");
		wrapper.add(this.documentsList, "Documentos");
		wrapper.add(this.insurancePoliciesList, "Apólices");
		wrapper.add(this.quoteRequestsList, "Consultas de Mercado");
		wrapper.add(this.casualtiesList, "Sinistros");
		wrapper.add(this.subProcessesList, "Sub-Processos");
		wrapper.add(this.historyList, "Histórico");
		wrapper.add(this.deadInsurancePoliciesList, "Apólices Antigas");

	}

	protected void initializeView() {};

	public void setClient(Client client){
		this.client = client;
		
		String clientId = client == null ? null : client.id;
		
		boolean allow = client != null ? PermissionChecker.hasPermission(client, BigBangConstants.OperationIds.ClientProcess.UPDATE_CLIENT) : false;

		this.contactsList.setOwner(clientId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.CLIENT);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(clientId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.CLIENT);
		this.documentsList.allowCreation(allow);
		this.insurancePoliciesList.setOwner(clientId);	
		this.quoteRequestsList.setOwner(clientId);
		this.casualtiesList.setOwner(clientId);
		this.subProcessesList.setOwner(clientId);
		this.historyList.setOwner(clientId);
		this.deadInsurancePoliciesList.setOwner(clientId);
	}

}
