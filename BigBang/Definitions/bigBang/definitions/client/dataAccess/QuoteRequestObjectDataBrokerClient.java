package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.QuoteRequestObject;

public interface QuoteRequestObjectDataBrokerClient extends
		DataBrokerClient<QuoteRequestObject> {

	public void addQuoteRequestObject(QuoteRequestObject object);
	
	public void updateQuoteRequestObject(QuoteRequestObject object);
	
	public void removeQuoteRequestObject(String id);
	
	public void remapItemId(String newId, String oldId);
	
}
