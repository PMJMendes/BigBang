package bigBang.module.clientModule.server;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ClientServiceImpl extends RemoteServiceServlet implements ClientService {

	private static final long serialVersionUID = 1L;

	@Override
	public NewSearchResult openSearch(SearchParameter[] parameters, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] search(String workspaceId,
			SearchParameter[] parameters, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] getResults(String workspaceId, int from, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeSearch(String workspaceId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Client getClient(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client createClient(Client client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client editClient(Client client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteClient(String clientId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClientGroup getClientGroup(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientGroup[] getAllClientGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientGroup createClientGroup(ClientGroup clientGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientGroup editClientGroup(ClientGroup clientGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteClientGroup(String id) {
		// TODO Auto-generated method stub
		
	}

}
