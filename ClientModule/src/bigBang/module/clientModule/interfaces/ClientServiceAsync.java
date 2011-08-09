package bigBang.module.clientModule.interfaces;

import bigBang.definitions.shared.Client;
import bigBang.library.interfaces.SearchServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientServiceAsync
	extends SearchServiceAsync
{
	void getClient(String clientId, AsyncCallback<Client> callback);
	void createClient(Client client, AsyncCallback<Client> callback);
	void editClient(Client client, AsyncCallback<Client> callback);
	void deleteClient(String clientId, AsyncCallback<Void> callback);
}
