package bigBang.library.interfaces;

import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync {

	void search(String workspaceId, SearchParameter parameter, int size,
			AsyncCallback<SearchResult[]> callback);

}
