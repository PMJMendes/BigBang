package bigBang.library.interfaces;

import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync
	extends Service
{
	void openSearch(String[] entityTypeIds, SearchParameter parameter, int size, AsyncCallback<NewSearchResult> callback);
	void search(String workspaceId, SearchParameter parameter, int size, AsyncCallback<SearchResult[]> callback);
	void getResults(String workspaceId, int from, int size, AsyncCallback<SearchResult[]> callback);
	void closeSearch(String workspaceId, AsyncCallback<Void> callback);
}
