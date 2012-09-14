package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestDataBrokerClient;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.definitions.shared.QuoteRequest.TableSection;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.Remap.RemapId;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.shared.CorruptedPadException;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter.SortableField;

import com.google.gwt.core.client.GWT;

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
		final String requestId = getEffectiveId(id);
		if(isTemp(requestId)){
			this.service.getRequestInPad(requestId, new BigBangAsyncCallback<QuoteRequest>() {

				@Override
				public void onResponseSuccess(QuoteRequest result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<QuoteRequest> bc : getClients()){
						((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
						((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(requestId);
					}
					handler.onError(new String[]{
							new String("Could not get the quote request")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			this.service.getRequest(requestId, new BigBangAsyncCallback<QuoteRequest>() {

				@Override
				public void onResponseSuccess(QuoteRequest result) {
					incrementDataVersion();
					for(DataBrokerClient<QuoteRequest> bc : getClients()){
						((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
						((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the quote request")
					});
					super.onResponseFailure(caught);
				}
			});
		}
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
	public void openRequestResource(final String requestId,
			final ResponseHandler<QuoteRequest> handler) {
		//NEW REQUEST
		if(requestId == null){
			service.openRequestScratchPad(requestId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onResponseSuccess(Remap[] result) {
					//If new policy
					if(result.length == 1 && result[0].remapIds.length == 1 && result[0].remapIds[0].oldId == null){
						QuoteRequest request = new QuoteRequest();
						RemapId remapId = result[0].remapIds[0];
						request.id = remapId.newId;
						requestsInScratchPad.put(request.id, request.id);
						
						service.initRequestInPad(request, new BigBangAsyncCallback<QuoteRequest>() {

							@Override
							public void onResponseSuccess(QuoteRequest result) {
								handler.onResponse(result);
							}
							
							@Override
							public void onResponseFailure(Throwable caught) {
								handler.onError(new String[]{
									new String("Could not initialize the quote request")	
								});
								super.onResponseFailure(caught);
							}
						});
					}
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(requestId);
					}
					handler.onError(new String[]{
							new String("Could not open the new request resource")
					});
					super.onResponseFailure(caught);
				}

			});
		}else if(!isTemp(requestId)){
			//EXISTING POLICY
			service.openRequestScratchPad(requestId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onResponseSuccess(Remap[] result) {
					doRemapping(result);
					getQuoteRequest(requestId, handler);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(requestId);
					}
					handler.onError(new String[]{
							new String("Could not open the request resource")
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			handler.onError(new String[]{
					new String("Cannot open a request resource twice for the same policy")	
			});
		}
	}

	@Override
	public void commitRequest(final QuoteRequest request,
			final ResponseHandler<QuoteRequest> handler) {
		String tempId = request.id;
		request.id = getEffectiveId(request.id);
		this.updateQuoteRequest(request, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				service.commitPad(getEffectiveId(response.id), new BigBangAsyncCallback<Remap[]>() {

					@Override
					public void onResponseSuccess(Remap[] result) {
						String finalId = result[0].remapIds[0].newId;
						
						final boolean newQuoteRequest = result[0].remapIds[0].oldId == null;
						
						doRemapping(result);
						getQuoteRequest(finalId, new ResponseHandler<QuoteRequest>() {

							@Override
							public void onResponse(QuoteRequest response) {
								incrementDataVersion();
								for(DataBrokerClient<QuoteRequest> client : getClients()) {
									if(newQuoteRequest) {
										((QuoteRequestDataBrokerClient) client).addQuoteRequest(response);
									}else{
										((QuoteRequestDataBrokerClient) client).updateQuoteRequest(response);
									}
									((QuoteRequestDataBrokerClient) client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
								}
								EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST, response.id));
								handler.onResponse(response);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								handler.onError(errors);
							}
						});
					}

					@Override
					public void onResponseFailure(Throwable caught) {
						request.id = getFinalMapping(request.id);
						if(caught instanceof CorruptedPadException){
							onPadCorrupted(request.id);
						}
						handler.onError(new String[]{
								new String("Could not commit the scratch pad")	
						});
						super.onResponseFailure(caught);
					}

				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				request.id = getFinalMapping(request.id);
				handler.onError(new String[]{
						new String("Could not commit the scratch pad")	
				});
			}
		});
		request.id = tempId;
	}

	@Override
	public void closeRequestResource(String requestId,
			final ResponseHandler<Void> handler) {
		requestId = getEffectiveId(requestId);
		if(isTemp(requestId)){
			service.discardPad(requestId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onResponseSuccess(Remap[] result) {
					doRemapping(result);
					handler.onResponse(null);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not close the request resource")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Cannot close an unopened request resource")
			});
		}
	}

	@Override
	public void openCoverageDetailsPage(String quoteRequestId,
			String subLineId, String insuredObjectId, final ResponseHandler<TableSection> handler) {
		final String requestId = getEffectiveId(quoteRequestId);
		if(isTemp(requestId)){
			service.getPageForEdit(requestId, subLineId, insuredObjectId, new BigBangAsyncCallback<QuoteRequest.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(requestId);
					}
					handler.onError(new String[]{
							new String("Could not get the requested page for edit")
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			service.getPage(requestId, subLineId, insuredObjectId, new BigBangAsyncCallback<QuoteRequest.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested page")
					});
					super.onResponseFailure(caught);
				}
			});
		}
	}

	@Override
	public void saveCoverageDetailsPage(String quoteRequestId, String subLineId,
			String insuredObjectId, TableSection data,
			final ResponseHandler<TableSection> handler) {
		final String requestId = getEffectiveId(quoteRequestId);
		if(isTemp(requestId)){
			this.service.savePage(data, new BigBangAsyncCallback<QuoteRequest.TableSection>() {

				@Override
				public void onResponseSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(requestId);
					}
					handler.onError(new String[]{
							new String("Coulg not save the page")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Could not save the page on an unopened request resource")
			});
		}
	}

	@Override
	public void updateQuoteRequest(QuoteRequest request,
			final ResponseHandler<QuoteRequest> handler) {
		final String requestId = getEffectiveId(request.id);
		if(isTemp(requestId)){
			String tempRequestId = request.id;
			request.id = requestId;
			service.updateHeader(request, new BigBangAsyncCallback<QuoteRequest>() {

				@Override
				public void onResponseSuccess(QuoteRequest result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<QuoteRequest> bc : getClients()){
						((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
						((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					if(caught instanceof CorruptedPadException){
						onPadCorrupted(requestId);
					}
					handler.onError(new String[]{
							new String("Could not save request header")	
					});
					super.onResponseFailure(caught);
				}

			});
			request.id = tempRequestId;
		}else{
			handler.onError(new String[]{
					new String("Could not save the request header. The request is not in scratch pad")
			});
		}
	}

	@Override
	public void closeQuoteRequest(String id, String notes,
			final ResponseHandler<QuoteRequest> handler) {
		service.closeProcess(id, notes, new BigBangAsyncCallback<QuoteRequest>() {

			@Override
			public void onResponseSuccess(QuoteRequest result) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).updateQuoteRequest(result);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.CLOSE_QUOTE_REQUEST, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not close the request process")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void deleteQuoteRequest(final String id, String reason, final ResponseHandler<String> handler) {
		service.deleteRequest(id, reason, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				incrementDataVersion();
				for(DataBrokerClient<QuoteRequest> bc : getClients()){
					((QuoteRequestDataBrokerClient) bc).removeQuoteRequest(id);
					((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.QuoteRequestProcess.DELETE_QUOTE_REQUEST, id));
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the quote request")
				});
				super.onResponseFailure(caught);
			}
		});
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
	public void addSubLine(String requestId, String subLineId,
			final ResponseHandler<RequestSubLine> handler) {
		String id = getEffectiveId(requestId);
		service.addSubLineToPad(id, subLineId, new BigBangAsyncCallback<QuoteRequest.RequestSubLine>() {

			@Override
			public void onResponseSuccess(RequestSubLine result) {
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not add the subline to scratch pad")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void deleteSubLine(String requestSubLineId, final ResponseHandler<Void> handler) {
		service.deleteSubLineFromPad(requestSubLineId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete sub line in pad")
				});
				super.onResponseFailure(caught);
			}
		});
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

	@Override
	public SearchDataBroker<QuoteRequestStub> getSearchBroker() {
		return this.searchBroker;
	}


	@Override
	public boolean isTemp(String quoteRequestId) {
		return this.requestsInScratchPad.containsKey(quoteRequestId) || this.requestsInScratchPad.containsValue(quoteRequestId);
	}

	@Override
	public void discardTemp(String policyId) {
		requestsInScratchPad.remove(policyId);
		String finalId = getFinalMapping(policyId);
		this.requestsInScratchPad.remove(finalId);
	}

	public String getEffectiveId(String id){
		if(this.requestsInScratchPad.containsKey(id)){
			return this.requestsInScratchPad.get(id);
		}
		return id;
	}

	public String getFinalMapping(String tempId){
		for(String key : this.requestsInScratchPad.keySet()){
			if(this.requestsInScratchPad.get(key).equalsIgnoreCase(tempId)){
				return key;
			}
		}
		return tempId;
	}

	private void onPadCorrupted(String requestId){
		closeRequestResource(requestId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	protected void doRemapping(Remap[] remappings){
		for(int i = 0; i < remappings.length; i++) {
			Remap remap = remappings[i];

			//REQUEST
			if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}

				//OBJECTS
			}
			else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					this.requestObjectsBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}
			}else{
				GWT.log("Could not remap item id for typeId = " + remap.typeId);
			}
		}
	}

	@Override
	public void remapItemId(String oldId, String newId, boolean newInScratchPad) {
		if(newInScratchPad){
			this.requestsInScratchPad.put(oldId, newId);
		}else if(newId == null){
			discardTemp(oldId);
		}else {
			this.requestsInScratchPad.remove(newId);
		}
		//		incrementDataVersion();
		//		for(DataBrokerClient<QuoteRequest> bc : getClients()){
		//			((QuoteRequestDataBrokerClient) bc).remapItemId(oldId, newId);
		//			((QuoteRequestDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.QUOTE_REQUEST, getCurrentDataVersion());
		//		}
	}

}
