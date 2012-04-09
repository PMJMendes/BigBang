package bigBang.module.receiptModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SignatureRequestBroker;
import bigBang.definitions.client.dataAccess.SignatureRequestBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.definitions.shared.SignatureRequest.Cancellation;
import bigBang.definitions.shared.SignatureRequest.Response;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.receiptModule.interfaces.SignatureRequestService;
import bigBang.module.receiptModule.interfaces.SignatureRequestServiceAsync;

public class SignatureRequestBrokerImpl extends DataBroker<SignatureRequest> implements SignatureRequestBroker{

	protected SignatureRequestServiceAsync service;
	protected boolean requiresRefresh = true;

	public SignatureRequestBrokerImpl(){
		this(SignatureRequestService.Util.getInstance());
		this.dataElementId = BigBangConstants.EntityIds.SIGNATURE_REQUEST;
	}

	public SignatureRequestBrokerImpl(SignatureRequestServiceAsync service){
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
	public void getRequest(final String id, final ResponseHandler<SignatureRequest> handler) {
		if(cache.contains(id) && !requiresRefresh){
			handler.onResponse((SignatureRequest) cache.get(id));
		}
		else{
			service.getRequest(id, new BigBangAsyncCallback<SignatureRequest>() {

				@Override
				public void onResponseSuccess(SignatureRequest result) {
					cache.add(id, result);
					incrementDataVersion();
					
					for(DataBrokerClient<SignatureRequest> bc : getClients()){
						((SignatureRequestBrokerClient)bc).updateSignatureRequest(result);
						((SignatureRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.SIGNATURE_REQUEST, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError((new String[]{
						new String("Could not get the requested signature request")	
					}));
					super.onResponseFailure(caught);
				}

			});

		}
	}

	@Override
	public void repeatRequest(SignatureRequest request,
			final ResponseHandler<SignatureRequest> handler) {
		service.repeatRequest(request, new BigBangAsyncCallback<SignatureRequest>() {

			@Override
			public void onResponseSuccess(SignatureRequest result) {
				cache.add(result.id, result);
				incrementDataVersion();
				
				for(DataBrokerClient<SignatureRequest> bc : getClients()){
					((SignatureRequestBrokerClient)bc).updateSignatureRequest(result);
					((SignatureRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.SIGNATURE_REQUEST, getCurrentDataVersion());
				}
				
				handler.onResponse(result);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SignatureRequestProcess.REPEAT_SIGNATURE_REQUEST, result.id));
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
					new String("Could not repeat the signature request")	
				}));
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void receiveResponse(Response response,
			final ResponseHandler<SignatureRequest> handler) {
		service.receiveResponse(response, new BigBangAsyncCallback<SignatureRequest>() {
		
			@Override
			public void onResponseSuccess(SignatureRequest result) {
				cache.add(result.id, result);
				incrementDataVersion();
				
				for(DataBrokerClient<SignatureRequest> bc : getClients()){
					((SignatureRequestBrokerClient)bc).updateSignatureRequest(result);
					((SignatureRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.SIGNATURE_REQUEST, getCurrentDataVersion());
				}
				
				handler.onResponse(result);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SignatureRequestProcess.RECEIVE_REPLY, result.id));
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
					new String("Could not receive the the signature request response")	
				}));
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void cancelRequest(final Cancellation cancellation,
			final ResponseHandler<Void> handler) {
		service.cancelRequest(cancellation, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(cancellation.requestId);
				incrementDataVersion();
				
				for(DataBrokerClient<SignatureRequest> bc : getClients()){
					((SignatureRequestBrokerClient)bc).removeSignatureRequest(cancellation.requestId);
					((SignatureRequestBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.SIGNATURE_REQUEST, getCurrentDataVersion());
				}
				
				handler.onResponse(null);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.SignatureRequestProcess.CANCEL_SIGNATURE_REQUEST, cancellation.requestId));
				
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
					new String("Could not cancel the the signature request")	
				}));
				super.onResponseFailure(caught);
			}
		
		});

	}


}
