package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.SearchResult;

public interface SearchBrokerClient<T extends SearchResult> extends DataBrokerClient<T> {

	public void setResults(T[] results);
	
}
