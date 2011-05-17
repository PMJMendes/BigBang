package bigBang.module.clientModule.server;

import Jewel.Engine.Engine;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;
import bigBang.module.clientModule.shared.ClientSearchParameter;

public class ClientServiceImpl
	extends EngineImplementor
	implements ClientService
{
	private static final long serialVersionUID = 1L;

	public NewSearchResult openSearch(SearchParameter[] parameters, int size)
		throws SessionExpiredException, BigBangException
	{
		ClientSearchParameter lparam;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		for ( i = 0; i < parameters.length; i++ )
		{
			if ( parameters[i] instanceof ClientSearchParameter)
			{
				lparam = (ClientSearchParameter) parameters[i];
				clientToServerBridge(lparam);
			}
		}

		return null;
	}

	public SearchResult[] search(String workspaceId, SearchParameter[] parameters, int size)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public SearchResult[] getResults(String workspaceId, int from, int size)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public void closeSearch(String workspaceId)
		throws SessionExpiredException, BigBangException
	{
			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

	}

	public ClientGroup[] getAllClientGroups()
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public ClientGroup getClientGroup(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public ClientGroup createClientGroup(ClientGroup clientGroup)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public ClientGroup editClientGroup(ClientGroup clientGroup)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public void deleteClientGroup(String id)
		throws SessionExpiredException, BigBangException
	{
			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

	}

	public Client getClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Client createClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Client editClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public void deleteClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

	}

	private void clientToServerBridge(ClientSearchParameter pParam)
	{
		
	}
}
