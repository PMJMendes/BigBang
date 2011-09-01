package bigBang.library.interfaces;

import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.shared.NewSearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync
	extends Service
{
	void openSearch(SearchParameter[] parameter, SortParameter[] sorts, int size, AsyncCallback<NewSearchResult> callback);
	void openForOperation(String opId, SearchParameter[] parameters, SortParameter[] sorts, int size, AsyncCallback<NewSearchResult> callback);
	void search(String workspaceId, SearchParameter[] parameter, SortParameter[] sorts, int size, AsyncCallback<NewSearchResult> callback);
	void getResults(String workspaceId, int from, int size, AsyncCallback<SearchResult[]> callback);
	void closeSearch(String workspaceId, AsyncCallback<Void> callback);
}
