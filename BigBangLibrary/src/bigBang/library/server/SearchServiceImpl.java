package bigBang.library.server;

import bigBang.library.interfaces.SearchService;
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
}
