package bigBang.module.receiptModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DASRequestBroker;
import bigBang.definitions.client.dataAccess.DASRequestBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DASRequest.Cancellation;
import bigBang.definitions.shared.DASRequest.Response;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.receiptModule.interfaces.DASRequestService;
import bigBang.module.receiptModule.interfaces.DASRequestServiceAsync;

public class DASRequestBrokerImpl extends DataBroker<DASRequest> implements DASRequestBroker{

	protected DASRequestServiceAsync service;
	protected boolean requiresRefresh = true;


	public DASRequestBrokerImpl(){
		this(DASRequestService.Util.getInstance());
		this.dataElementId = BigBangConstants.EntityIds.DAS_REQUEST;
	}
	public DASRequestBrokerImpl(DASRequestServiceAsync service) {
		this.service = service;
	}
	@Override
	public void requireDataRefresh() {
		// TODO Auto-generated method stub
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getRequest(final String id, final ResponseHandler<DASRequest> handler) {
		if(cache.contains(id) && !requiresRefresh){
			handler.onResponse((DASRequest) cache.get(id));
		}
		else
			service.getRequest(id, new BigBangAsyncCallback<DASRequest>() {

				@Override
				public void onResponseSuccess(DASRequest result) {
					cache.add(id, result);
					incrementDataVersion();


					for(DataBrokerClient<DASRequest> bc : getClients()){
						((DASRequestBrokerClient)bc).updateDASRequest(result);
						((DASRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.DAS_REQUEST, getCurrentDataVersion());
					}

					handler.onResponse(result);
					requiresRefresh = false;

				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError((new String[]{
							new String("Could not get the requested DAS request")	
					}));
					super.onResponseFailure(caught);
				}
			});
	}

	@Override
	public void repeatRequest(DASRequest request,
			final ResponseHandler<DASRequest> handler) {
		service.repeatRequest(request, new BigBangAsyncCallback<DASRequest>() {
			@Override
			public void onResponseSuccess(DASRequest result) {
				cache.add(result.id, result);
				incrementDataVersion();


				for(DataBrokerClient<DASRequest> bc : getClients()){
					((DASRequestBrokerClient)bc).updateDASRequest(result);
					((DASRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.DAS_REQUEST, getCurrentDataVersion());
				}

				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.DASRequestProcess.REPEAT_DAS_REQUEST, result.id));
				handler.onResponse(result);
				requiresRefresh = false;

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not repeat the requested DAS request")	
				}));
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void receiveResponse(Response request,
			final ResponseHandler<DASRequest> handler) {
		service.receiveResponse(request, new BigBangAsyncCallback<DASRequest>() {
			@Override
			public void onResponseSuccess(DASRequest result) {
				cache.add(result.id, result);
				incrementDataVersion();


				for(DataBrokerClient<DASRequest> bc : getClients()){
					((DASRequestBrokerClient)bc).updateDASRequest(result);
					((DASRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.DAS_REQUEST, getCurrentDataVersion());
				}

				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.DASRequestProcess.RECEIVE_REPLY, result.id));
				handler.onResponse(result);
				requiresRefresh = false;

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not receive the DAS request response")	
				}));
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void cancelRequest(final Cancellation request,
			final ResponseHandler<Void> handler) {
		service.cancelRequest(request, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(request.requestId);
				incrementDataVersion();

				for(DataBrokerClient<DASRequest> bc : getClients()){
					((DASRequestBrokerClient)bc).removeDASRequest(request.requestId);
					((DASRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.DAS_REQUEST, getCurrentDataVersion());
				}

				handler.onResponse(null);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.DASRequestProcess.CANCEL_DAS_REQUEST, request.requestId));

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not cancel the the DAS request")	
				}));
				super.onResponseFailure(caught);
			}

		});

	}

}
