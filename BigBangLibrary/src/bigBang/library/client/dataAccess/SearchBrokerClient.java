package bigBang.library.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.SearchResult;

public interface SearchBrokerClient<T extends SearchResult> extends DataBrokerClient<T> {

	public void setResults(T[] results);
	
}
