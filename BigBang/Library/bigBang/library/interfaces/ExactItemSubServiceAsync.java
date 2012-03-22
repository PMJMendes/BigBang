package bigBang.library.interfaces;

import bigBang.definitions.shared.SearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExactItemSubServiceAsync
{
	void getExactResults(String label, AsyncCallback<SearchResult[]> callback);
}
