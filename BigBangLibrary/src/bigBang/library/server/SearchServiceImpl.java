package bigBang.library.server;

import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;

public class SearchServiceImpl
	extends EngineImplementor
	implements SearchService
{
	private static final long serialVersionUID = 1L;

	@Override
	public SearchResult[] search(String workspaceId, SearchParameter[] parameters,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult openSearch(String[] entityTypeIds,
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
}
