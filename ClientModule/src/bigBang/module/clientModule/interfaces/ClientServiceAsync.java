package bigBang.module.clientModule.interfaces;

import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientServiceAsync extends SearchServiceAsync{

	void createClient(Client client, AsyncCallback<Client> callback);

	void deleteClient(String clientId, AsyncCallback<Void> callback);

	void editClient(Client client, AsyncCallback<Client> callback);

	void getClient(String clientId, AsyncCallback<Client> callback);

	void createClientGroup(ClientGroup clientGroup,
			AsyncCallback<ClientGroup> callback);

	void deleteClientGroup(String id, AsyncCallback<Void> callback);

	void editClientGroup(ClientGroup clientGroup,
			AsyncCallback<ClientGroup> callback);

	void getClientGroup(String id, AsyncCallback<ClientGroup> callback);

	void getAllClientGroups(AsyncCallback<ClientGroup[]> callback);

}
