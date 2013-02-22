package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.quoteRequestModule.client.QuoteRequestWorkSpace;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter.SortableField;

public class QuoteRequestBrokerImpl extends DataBroker<QuoteRequest> implements	QuoteRequestBroker {

	protected QuoteRequestServiceAsync service;
	protected SearchDataBroker<QuoteRequestStub> searchBroker;
	protected ClientProcessBroker clientBroker;
	protected QuoteRequestObjectServiceAsync requestObjectService;
	protected QuoteRequestWorkSpace workspace;
	public boolean requiresRefresh;

	public QuoteRequestBrokerImpl(){
		this(QuoteRequestService.Util.getInstance(), QuoteRequestObjectService.Util.getInstance());
	}

	public QuoteRequestBrokerImpl(QuoteRequestServiceAsync service, QuoteRequestObjectServiceAsync requestObjectService){
		this.service = service;
		this.requestObjectService = requestObjectService;
		this.clientBroker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		this.dataElementId = BigBangConstants.EntityIds.QUOTE_REQUEST;
		this.searchBroker = new QuoteRequestSearchDataBroker(this.service);
		this.workspace = new QuoteRequestWorkSpace();
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
		this.service.getRequest(id, new BigBangAsyncCallback<QuoteRequest>() {

			@Override
			public void onResponseSuccess(QuoteRequest result) {
				workspace.loadRequest(result);

				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
				}
				handler.onResponse(result);
				requiresRefresh = false;
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the requested quote request")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getEmptyQuoteRequest(String clientId, final ResponseHandler<QuoteRequest> handler) {
		this.service.getEmptyRequest(clientId, new BigBangAsyncCallback<QuoteRequest>() {

			@Override
			public void onResponseSuccess(QuoteRequest result) {
				workspace.loadRequest(result);

				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
				}
				handler.onResponse(result);
				requiresRefresh = false;
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the empty quote request")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public QuoteRequest getRequestHeader(String requestId) {
		return workspace.getRequestHeader(requestId);
	}

	@Override
	public QuoteRequest updateRequestHeader(QuoteRequest request) {
		return workspace.updateRequestHeader(request);
	}

	@Override
	public void persistQuoteRequest(String requestId,
			final ResponseHandler<QuoteRequest> handler) {
		QuoteRequest request;

		request = workspace.getWholeRequest(requestId);

		if(request != null) {
			if ( request.id == null ) {
				clientBroker.createQuoteRequest(request, new ResponseHandler<QuoteRequest>() {

					@Override
					public void onResponse(QuoteRequest response) {
						workspace.loadRequest(response);

						incrementDataVersion();
						for(DataBrokerClient<QuoteRequest> bc : getClients()) {
							((QuoteRequestDataBrokerClient) bc).addQuoteRequest(response);
							((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
						}
						handler.onResponse(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						handler.onError(errors);
					}
				});
			} else {
				this.service.editRequest(request, new BigBangAsyncCallback<QuoteRequest>() {

					@Override
					public void onResponseSuccess(QuoteRequest result) {
						workspace.loadRequest(result);

						incrementDataVersion();
						for(DataBrokerClient<QuoteRequest> bc : getClients()){
							((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
							((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
						}
						handler.onResponse(result);
						requiresRefresh = false;
					}

					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[]{
								"Could not update the quote request"	
						});
						super.onResponseFailure(caught);
					}
				});
			}
		} else {
			handler.onError(new String[]{
					"Could not update the quote request"	
			});
		}
	}

	@Override
	public QuoteRequest discardEditData(String requestId) {
		return workspace.reset(requestId);
	}

	@Override
	public void removeQuoteRequest(final String requestId, String reason, final ResponseHandler<String> handler) {
		this.service.deleteRequest(requestId, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				workspace.loadRequest(null);

				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).removeQuoteRequest(requestId);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.DELETE_QUOTE_REQUEST, requestId));
				handler.onResponse(requestId);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not remove the quote request")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public CompositeFieldContainer.SubLineFieldContainer[] getLocalSubLines(String requestId) {
		return workspace.getLocalSubLines(requestId);
	}

	@Override
	public void createSubLine(final String requestId, String subLineId,
			final ResponseHandler<CompositeFieldContainer.SubLineFieldContainer> handler) {
		this.service.getEmptySubLine(subLineId, new BigBangAsyncCallback<CompositeFieldContainer.SubLineFieldContainer>() {

			@Override
			public void onResponseSuccess(CompositeFieldContainer.SubLineFieldContainer result) {
				handler.onResponse(workspace.loadSubLine(requestId, result));				
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not obtain the empty subline")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateSubLineCoverages(String requestId,
			String subLineId, Coverage[] coverages) {
		workspace.updateCoverages(requestId, subLineId, coverages);
	}

	@Override
	public CompositeFieldContainer.SubLineFieldContainer removeSubLine(String requestId, String subLineId) {
		return workspace.deleteSubLine(requestId, subLineId);
	}

	@Override
	public QuoteRequestObjectStub[] getAlteredObjects(String requestId) {
		return workspace.getLocalObjects(requestId);
	}

	@Override
	public void getRequestObject(final String requestId, String objectId,
			final ResponseHandler<QuoteRequestObject> handler) {
		QuoteRequestObject object = workspace.getObjectHeader(requestId, objectId);

		if(object != null) {
			handler.onResponse(object);
		} else {
			requestObjectService.getObject(objectId, new BigBangAsyncCallback<QuoteRequestObject>() {

				@Override
				public void onResponseSuccess(QuoteRequestObject result) {
					handler.onResponse(workspace.loadExistingObject(requestId, result));
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the object")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public QuoteRequestObject createRequestObject(String requestId, String typeId) {
		return workspace.createLocalObject(requestId, typeId);
	}

	@Override
	public QuoteRequestObject updateRequestObject(String requestId, QuoteRequestObject object) {
		return workspace.updateObjectHeader(requestId, object);
	}

	@Override
	public QuoteRequestObjectStub removeRequestObject(String requestId, String objectId) {
		return workspace.deleteObject(requestId, objectId);
	}

	@Override
	public FieldContainer getContextForRequest(String requestId, String subLineId) {
		return workspace.getContext(requestId, subLineId, null);
	}

	@Override
	public void saveContextForRequest(String requestId, String subLineId, FieldContainer contents) {
		workspace.updateContext(requestId, subLineId, null, contents);
	}

	@Override
	public FieldContainer getContextForCompositeObject(String requestId, String subLineId, String objectId) {
		return workspace.getContext(requestId, subLineId, objectId);
	}

	@Override
	public void saveContextForCompositeObject(String requestId, String subLineId, String objectId, FieldContainer contents) {
		workspace.updateContext(requestId, subLineId, objectId, contents);
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
			String managerId, final ResponseHandler<QuoteRequest> handler) {
		ManagerTransfer transfer = new ManagerTransfer();
		transfer.dataObjectIds = requestIds;
		transfer.newManagerId = managerId;


		service.createManagerTransfer(transfer, new BigBangAsyncCallback<ManagerTransfer>() {

			@Override
			public void onResponseSuccess(ManagerTransfer result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.CasualtyProcess.CREATE_MANAGER_TRANSFER, result.id));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not transfer the processes")
				});
				super.onResponseFailure(caught);
			}
		});

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
	}

	@Override
	public void receiveMessage(Conversation info,
			final ResponseHandler<Conversation> responseHandler) {
		service.receiveMessage(info, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CONVERSATION, result.id));
				responseHandler.onResponse(result);
			}


			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not receive the message")		
				});	
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void sendMessage(Conversation info,
			final ResponseHandler<Conversation> responseHandler) {
		service.sendMessage(info, new BigBangAsyncCallback<Conversation>() {

			@Override
			public void onResponseSuccess(Conversation result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CONVERSATION, result.id));
				responseHandler.onResponse(result);
			}


			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not send the message")		
				});	
				super.onResponseFailure(caught);
			}
		});		
	}

	@Override
	public void createNegotiation(Negotiation negotiation,
			final ResponseHandler<Negotiation> responseHandler) {
		service.createNegotiation(negotiation, new BigBangAsyncCallback<Negotiation>() {
		
		@Override
		public void onResponseSuccess(Negotiation result) {
			EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_NEGOTIATION, result.id));
			responseHandler.onResponse(result);				
		}
		
		@Override
			public void onResponseFailure(Throwable caught) {
			responseHandler.onError(new String[]{
					new String("Could not create Negotiation")		
			});	
			super.onResponseFailure(caught);			}
		
		});
	}

	@Override
	public void closeQuoteRequest(String id, String info,
			final ResponseHandler<QuoteRequest> responseHandler) {
		service.closeProcess(id, info, new BigBangAsyncCallback<QuoteRequest>() {

			@Override
			public void onResponseSuccess(QuoteRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CLOSE_QUOTE_REQUEST, result.id));
				responseHandler.onResponse(result);				
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(result.id, getCurrentDataVersion());
				}
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				responseHandler.onError(new String[]{
						new String("Could not close the quote request")		
				});	
				super.onResponseFailure(caught);
			}

		});
	}
}
