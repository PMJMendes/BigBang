package bigBang.library.client.dataAccess;

import bigBang.library.shared.SearchResult;

public interface SearchBrokerClient<T extends SearchResult> extends DataBrokerClient<T> {

	public void setResults(T[] results);
	
}
