package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;

public interface QuoteRequestObjectDataBroker extends
		DataBrokerInterface<QuoteRequestObject> {

	public void createQuoteRequestObject(String ownerId, String type, ResponseHandler<QuoteRequestObject> handler);
	
	public void getQuoteRequestObject(String id, ResponseHandler<QuoteRequestObject> handler);
	
	public void updateQuoteRequestObject(QuoteRequestObject object, ResponseHandler<QuoteRequestObject> handler);
	
	public void deleteQuoteRequestObject(String objectId, ResponseHandler<Void> handler);
	
	public void getProcessQuoteRequestObjects(String ownerId, ResponseHandler<Collection<QuoteRequestObjectStub>> handler);
	
	public SearchDataBroker<QuoteRequestObjectStub> getSearchBroker();
	
	public void remapItemId(String oldId, String newId, boolean newIdInScratchPad);
	
}
