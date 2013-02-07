package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestDataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.CompositeFieldContainer.SubLineFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter.SortableField;

public class QuoteRequestBrokerImpl extends DataBroker<QuoteRequest> implements	QuoteRequestBroker {

	protected QuoteRequestServiceAsync service;
	protected SearchDataBroker<QuoteRequestStub> searchBroker;
	protected Map<String, String> requestsInScratchPad;
	protected QuoteRequestObjectDataBroker requestObjectsBroker;

	public QuoteRequestBrokerImpl(QuoteRequestObjectDataBroker requestObjectsBroker){
		this.service = QuoteRequestService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.QUOTE_REQUEST;
		this.searchBroker = new QuoteRequestSearchDataBroker(this.service);
		this.requestsInScratchPad = new HashMap<String, String>();
		this.requestObjectsBroker = requestObjectsBroker;
	}

	@Override
	public void requireDataRefresh() {
		return;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getEmptyQuoteRequest(String clientId,
			ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuoteRequest getRequestHeader(String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequest updateRequestHeader(QuoteRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistQuoteRequest(String requestId,
			ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuoteRequest discardEditData(String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeQuoteRequest(String requestId,
			ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SubLineFieldContainer createSubLine(String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubLineFieldContainer updateSubLineCoverages(String requestId,
			String subLineId, Coverage[] coverages) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestObjectStub[] getAlteredObjects(String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getCompositeObject(String requestId, String objectId,
			ResponseHandler<QuoteRequestObject> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuoteRequestObject createCompositeObject(String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestObject updateCompositeObject(String requestId,
			QuoteRequestObject object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestObjectStub removeCompositeObject(String requestId,
			String objectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldContainer getContextForRequest(String requestId,
			String subLineId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveContextForRequest(String requestId, String subLineId,
			FieldContainer contents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FieldContainer getContextForCompositeObject(String requestId,
			String subLineId, String objectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveContextForCompositeObject(String requestId,
			String subLineId, String objectId, FieldContainer contents) {
		// TODO Auto-generated method stub
		
	}


	//Other operations

	@Override
	public SearchDataBroker<QuoteRequestStub> getSearchBroker() {
		return this.searchBroker;
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
		}, true);
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
	public void createManagerTransfer(String[] dataObjectIds, String managerId,
			final ResponseHandler<ManagerTransfer> handler) {
		ManagerTransfer transfer = new ManagerTransfer();
		transfer.newManagerId = managerId;
		transfer.dataObjectIds = dataObjectIds;

		if(dataObjectIds.length > 1){
			service.massCreateManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					for(int i = 0; i < result.dataObjectIds.length; i++) {
						requireDataRefresh();
						getQuoteRequest(result.dataObjectIds[i], new ResponseHandler<QuoteRequest>(){

							@Override
							public void onResponse(QuoteRequest response) {
								for(DataBrokerClient<QuoteRequest> c : getClients()) {
									QuoteRequestDataBrokerClient b = (QuoteRequestDataBrokerClient)c;
									b.updateQuoteRequest(response);
								}
								EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER, response.id));
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});
					}
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER, result.newManagerId));
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create the manager transfer")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

				@Override
				public void onResponseSuccess(ManagerTransfer result) {
					for(int i = 0; i < result.dataObjectIds.length; i++) {
						getQuoteRequest(result.dataObjectIds[i], new ResponseHandler<QuoteRequest>(){

							@Override
							public void onResponse(QuoteRequest response) {
								for(DataBrokerClient<QuoteRequest> c : QuoteRequestBrokerImpl.this.clients) {
									QuoteRequestDataBrokerClient b = (QuoteRequestDataBrokerClient)c;
									b.updateQuoteRequest(response);
								}
								EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER, response.id));
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								return;
							}
						});
					}
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER, result.newManagerId));
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create manager transfer")	
					});
					super.onResponseFailure(caught);
				}
			});
		}		
	}

}
