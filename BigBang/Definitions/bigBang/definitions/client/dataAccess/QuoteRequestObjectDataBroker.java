package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;

public interface QuoteRequestObjectDataBroker extends
		DataBrokerInterface<QuoteRequestObject> {

	public void getQuoteRequestObject(String id, ResponseHandler<QuoteRequestObject> handler);
	
	public void getProcessQuoteRequestObjects(String ownerId, ResponseHandler<Collection<QuoteRequestObjectStub>> handler);
	
	public SearchDataBroker<QuoteRequestObjectStub> getSearchBroker();
	
}
