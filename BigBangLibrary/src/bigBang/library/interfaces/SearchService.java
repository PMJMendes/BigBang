package bigBang.library.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.library.shared.SortParameter;

public interface SearchService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SearchServiceAsync instance;
		public static SearchServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SearchService.class);
			}
			return instance;
		}
	}
	
	NewSearchResult openSearch(SearchParameter[] parameters, SortParameter[] sorts, int size) throws SessionExpiredException, BigBangException;
	NewSearchResult openForOperation(String opId, SearchParameter[] parameters, SortParameter[] sorts, int size) throws SessionExpiredException, BigBangException;
	NewSearchResult search(String workspaceId, SearchParameter[] parameters, SortParameter[] sorts, int size) throws SessionExpiredException, BigBangException;
	SearchResult[] getResults(String workspaceId, int from, int size) throws SessionExpiredException, BigBangException;
	void closeSearch(String workspaceId) throws SessionExpiredException, BigBangException;
}
