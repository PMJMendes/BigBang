package bigBang.module.clientModule.interfaces;

import bigBang.library.interfaces.SearchService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ClientService")
public interface ClientService extends SearchService, RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ClientServiceAsync instance;
		public static ClientServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ClientService.class);
			}
			return instance;
		}
	}

	//CLIENT
	public Client getClient(String clientId);
	
	public Client createClient(Client client);
	
	public Client editClient(Client client);
	
	public void deleteClient(String clientId);
	
	
	//CLIENT GROUP
	public ClientGroup getClientGroup(String id);
	
	public ClientGroup[] getAllClientGroups();
	
	public ClientGroup createClientGroup(ClientGroup clientGroup);
	
	public ClientGroup editClientGroup(ClientGroup clientGroup);
	
	public void deleteClientGroup(String id);

}
