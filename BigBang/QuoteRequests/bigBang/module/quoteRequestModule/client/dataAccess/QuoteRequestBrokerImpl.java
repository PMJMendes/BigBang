package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter.SortableField;

public class QuoteRequestBrokerImpl extends DataBroker<QuoteRequest> implements	QuoteRequestBroker {

	protected QuoteRequestServiceAsync service;
	protected SearchDataBroker<QuoteRequestStub> searchBroker;
	protected boolean requiresRefresh;

	public QuoteRequestBrokerImpl(){
		this.service = QuoteRequestService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.QUOTE_REQUEST;
		this.searchBroker = new QuoteRequestSearchDataBroker(this.service);
	}
	
	@Override
	public void requireDataRefresh() {
		requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getQuoteRequest(itemId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).addQuoteRequest(response);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(response.id, getCurrentDataVersion());
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
		for(DataBrokerClient<QuoteRequest> bc : getClients()) {
			((QuoteRequestDataBrokerClient) bc).removeQuoteRequest(itemId);
			((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(itemId, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getQuoteRequest(itemId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(response);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(response.id, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getQuoteRequest(String id, final ResponseHandler<QuoteRequest> handler) {
		service.getRequest(id, new BigBangAsyncCallback<QuoteRequest>() {

			@Override
			public void onResponseSuccess(QuoteRequest result) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(result.id, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the Quote request")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getQuoteRequestsForClient(String ownerId,
			final ResponseHandler<Collection<QuoteRequestStub>> handler) {
		QuoteRequestSearchParameter parameter = new QuoteRequestSearchParameter();
		parameter.ownerId = ownerId;
		
		QuoteRequestSearchParameter[] parameters = new QuoteRequestSearchParameter[]{
				parameter
		}; 
		
		QuoteRequestSortParameter sort = new QuoteRequestSortParameter(SortableField.NUMBER, SortOrder.ASC);
		QuoteRequestSortParameter[] sorts = new QuoteRequestSortParameter[]{
				sort
		};
		
		getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<QuoteRequestStub>>() {

			@Override
			public void onResponse(Search<QuoteRequestStub> response) {
				handler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
					new String("Could not get the Quote Requests for the given owner")	
				});
			}
		});
	}

	@Override
	public void updateQuoteRequest(QuoteRequest request,
			ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeQuoteRequest(String id,
			ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeQuoteRequest(String id, ResponseHandler<String> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertInsuredObject(InsuredObject object,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createInfoOrDocumentRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createQuoteRequestManagerTransfer(String[] requestIds,
			String managerId, ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createRiskAnalysis(RiskAnalysis riskAnalysis,
			ResponseHandler<RiskAnalysis> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public SearchDataBroker<QuoteRequestStub> getSearchBroker() {
		return this.searchBroker;
	}

}
