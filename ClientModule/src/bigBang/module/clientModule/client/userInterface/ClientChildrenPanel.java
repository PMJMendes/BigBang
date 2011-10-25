package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientChildrenLists.ContactsList;
import bigBang.module.clientModule.client.userInterface.ClientChildrenLists.DocumentsList;
import bigBang.module.clientModule.client.userInterface.ClientChildrenLists.InfoRequestList;
import bigBang.module.clientModule.client.userInterface.ClientChildrenLists.InsurancePoliciesList;
import bigBang.module.clientModule.client.userInterface.ClientChildrenLists.ManagerTransferList;

import com.google.gwt.user.client.ui.StackPanel;

public class ClientChildrenPanel extends View implements ClientProcessDataBrokerClient {

	protected Client client;
	protected int clientDataVersion;
	
	protected ClientChildrenLists.ContactsList contactsList;
	protected ClientChildrenLists.DocumentsList documentsList;
	protected ClientChildrenLists.InsurancePoliciesList insurancePoliciesList;
	protected ClientChildrenLists.InfoRequestList requestsList;
	protected ClientChildrenLists.ManagerTransferList managerTransfersList;
	
	public ClientChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		insurancePoliciesList = new InsurancePoliciesList();
		requestsList = new InfoRequestList();
		managerTransfersList = new ManagerTransferList();
		
		wrapper.add(this.contactsList, "Contactos");
		wrapper.add(this.documentsList, "Documentos");
		wrapper.add(this.insurancePoliciesList, "Apólices");
		wrapper.add(this.requestsList, "Pedidos de Informação");
		wrapper.add(this.managerTransfersList, "Transferências de Gestor");
		
		initWidget(wrapper);
	}
	
	public void setClient(Client client){
		this.client = client;
		this.contactsList.setOwner(client.id);
		this.documentsList.setOwner(client.id);	
		this.insurancePoliciesList.setOwner(client.id);	
		this.requestsList.setOwner(client.id);	
		this.managerTransfersList.setOwner(client.id);	
	}

	public Client getCurrentClient(){
		return this.client;
	}
	
	public void clear(){
		this.contactsList.clear();
		this.documentsList.clear();
		this.insurancePoliciesList.clear();
		this.requestsList.clear();
		this.managerTransfersList.clear();
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			this.clientDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			return this.clientDataVersion;
		}
		return -1;
	}

	@Override
	public void addClient(Client client) {
		return;
	}

	@Override
	public void updateClient(Client client) {
		if(this.client == null){
			return;
		}
		if(client.id.equalsIgnoreCase(this.client.id)){
			setClient(client);
		}
	}

	@Override
	public void removeClient(String clientId) {
		if(this.client == null){
			return;
		}
		if(clientId.equalsIgnoreCase(this.client.id)){
			this.clear();
		}
	}
	
	public void setReadOnly(boolean readOnly){
		//TODO FJVC
	}
	
}
