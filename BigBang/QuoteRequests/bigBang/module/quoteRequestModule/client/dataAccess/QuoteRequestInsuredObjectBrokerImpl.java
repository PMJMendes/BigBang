package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;

public class QuoteRequestInsuredObjectBrokerImpl extends DataBroker<QuoteRequestObject>
		implements QuoteRequestObjectDataBroker {

	protected QuoteRequestObjectServiceAsync service;
	protected QuoteRequestServiceAsync requestService;
	protected SearchDataBroker<QuoteRequestObjectStub> searchBroker;

	protected Map<String, String> objectsInScratchPad;
	protected QuoteRequestBroker quoteRequestBroker;
	protected boolean requiresRefresh;

	public QuoteRequestInsuredObjectBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT;
		this.service = QuoteRequestObjectService.Util.getInstance();
		this.requestService = QuoteRequestService.Util.getInstance();
		this.searchBroker = new QuoteRequestObjectSearchBroker(this.service);
		this.objectsInScratchPad = new HashMap<String, String>();
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getQuoteRequestObject(itemId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(QuoteRequestObject response) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequestObject> bc : getClients()){
					((QuoteRequestObjectDataBrokerClient) bc).addQuoteRequestObject(response);
					((QuoteRequestObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		incrementDataVersion();
		for(DataBrokerClient<QuoteRequestObject> bc : getClients()){
			((QuoteRequestObjectDataBrokerClient) bc).removeQuoteRequestObject(itemId);
			((QuoteRequestObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getQuoteRequestObject(itemId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(QuoteRequestObject response) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequestObject> bc : getClients()){
					((QuoteRequestObjectDataBrokerClient) bc).updateQuoteRequestObject(response);
					((QuoteRequestObjectDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getQuoteRequestObject(String id,
			ResponseHandler<QuoteRequestObject> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getProcessQuoteRequestObjects(String ownerId,
			ResponseHandler<Collection<QuoteRequestObjectStub>> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchDataBroker<QuoteRequestObjectStub> getSearchBroker() {
		// TODO Auto-generated method stub
		return null;
	}

}
