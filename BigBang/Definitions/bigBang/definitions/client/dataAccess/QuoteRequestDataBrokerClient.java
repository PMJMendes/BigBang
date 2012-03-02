package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.QuoteRequest;

public interface QuoteRequestDataBrokerClient extends
		DataBrokerClient<QuoteRequest> {

	public void addQuoteRequest(QuoteRequest request);
	public void updateQuoteRequest(QuoteRequest request);
	public void removeQuoteRequest(String id);
	
}
