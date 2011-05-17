package bigBang.library.server;

import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;

public class SearchServiceImpl
	extends EngineImplementor
	implements SearchService
{
	private static final long serialVersionUID = 1L;

	@Override
	public SearchResult[] search(String workspaceId, SearchParameter parameter,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult openSearch(String[] entityTypeIds,
			SearchParameter parameter, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] getResults(String workspaceId, int from, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeSearch(String workspaceId) {
		// TODO Auto-generated method stub
		
	}
}
