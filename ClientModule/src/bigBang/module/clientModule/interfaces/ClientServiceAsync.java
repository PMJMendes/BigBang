package bigBang.module.clientModule.interfaces;

import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.clientModule.shared.Client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientServiceAsync
	extends SearchServiceAsync
{
	void getClient(String clientId, AsyncCallback<Client> callback);
	void createClient(Client client, AsyncCallback<Client> callback);
	void editClient(Client client, AsyncCallback<Client> callback);
	void deleteClient(String clientId, AsyncCallback<Void> callback);
}
