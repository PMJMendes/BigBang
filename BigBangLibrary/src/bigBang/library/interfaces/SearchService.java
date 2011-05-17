package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;

public interface SearchService
	extends RemoteService
{
	NewSearchResult openSearch(SearchParameter[] parameters, int size) throws SessionExpiredException, BigBangException;
	SearchResult[] search(String workspaceId, SearchParameter[] parameters, int size) throws SessionExpiredException, BigBangException;
	SearchResult[] getResults(String workspaceId, int from, int size) throws SessionExpiredException, BigBangException;
	void closeSearch(String workspaceId) throws SessionExpiredException, BigBangException;
}
