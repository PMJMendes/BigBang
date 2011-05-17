package bigBang.module.clientModule.server;

import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;

public class ClientServiceImpl
	extends EngineImplementor
	implements ClientService
{
	private static final long serialVersionUID = 1L;

	public NewSearchResult openSearch(SearchParameter[] parameters, int size)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public SearchResult[] search(String workspaceId, SearchParameter[] parameters, int size)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public SearchResult[] getResults(String workspaceId, int from, int size)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public void closeSearch(String workspaceId)
		throws SessionExpiredException, BigBangException
	{
	}

	public ClientGroup[] getAllClientGroups()
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public ClientGroup getClientGroup(String id)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public ClientGroup createClientGroup(ClientGroup clientGroup)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public ClientGroup editClientGroup(ClientGroup clientGroup)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public void deleteClientGroup(String id)
		throws SessionExpiredException, BigBangException
	{
	}

	public Client getClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public Client createClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public Client editClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		return null;
	}

	public void deleteClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
	}
}
