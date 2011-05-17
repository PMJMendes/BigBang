package bigBang.module.clientModule.interfaces;

import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ClientService")
public interface ClientService
	extends SearchService 
{
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

	//CLIENT GROUP
	public ClientGroup[] getAllClientGroups() throws SessionExpiredException, BigBangException;
	public ClientGroup getClientGroup(String id) throws SessionExpiredException, BigBangException;
	public ClientGroup createClientGroup(ClientGroup clientGroup) throws SessionExpiredException, BigBangException;
	public ClientGroup editClientGroup(ClientGroup clientGroup) throws SessionExpiredException, BigBangException;
	public void deleteClientGroup(String id) throws SessionExpiredException, BigBangException;

	//CLIENT
	public Client getClient(String clientId) throws SessionExpiredException, BigBangException;
	public Client createClient(Client client) throws SessionExpiredException, BigBangException;
	public Client editClient(Client client) throws SessionExpiredException, BigBangException;


	public void deleteClient(String clientId) throws SessionExpiredException, BigBangException;
}
