package bigBang.module.generalSystemModule.interfaces;

import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientGroupServiceAsync
	extends Service
{
	void getClientGroupList(AsyncCallback<ClientGroup[]> callback);
	void createClientGroup(ClientGroup clientGroup, AsyncCallback<ClientGroup> callback);
	void saveClientGroup(ClientGroup clientGroup, AsyncCallback<ClientGroup> callback);
	void deleteClientGroup(String id, AsyncCallback<Void> callback);
}
