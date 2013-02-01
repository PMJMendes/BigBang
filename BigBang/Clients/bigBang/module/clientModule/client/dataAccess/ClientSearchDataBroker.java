package bigBang.module.clientModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;

public class ClientSearchDataBroker extends SearchDataBrokerImpl<ClientStub> implements SearchDataBroker<ClientStub> {

	public ClientSearchDataBroker() {
		this(ClientService.Util.getInstance());
	}
	
	public ClientSearchDataBroker(ClientServiceAsync service){
		super(service);
	}
}
