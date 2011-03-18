package bigBang.library.interfaces;

import java.util.HashMap;

import bigBang.library.client.SearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync {

	void search(HashMap<String, String> filters, String[] requiredFields, String searchTerms, AsyncCallback<SearchResult[]> callback);

	

}
