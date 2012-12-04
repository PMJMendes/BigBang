package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.NegotiationBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.definitions.shared.Negotiation.Grant;
import bigBang.definitions.shared.Negotiation.Response;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.quoteRequestModule.interfaces.NegotiationService;
import bigBang.module.quoteRequestModule.interfaces.NegotiationServiceAsync;

public class NegotiationBrokerImpl extends DataBroker<Negotiation> implements NegotiationBroker{

	protected NegotiationServiceAsync service;
	protected boolean requiresRefresh = true;

	public NegotiationBrokerImpl(){
		this(NegotiationService.Util.getInstance());
		this.dataElementId = BigBangConstants.EntityIds.NEGOTIATION;
	}
	public NegotiationBrokerImpl(NegotiationServiceAsync service) {
		this.service = service;
	}

	@Override
	public void createExternalInfoRequest(final ExternalInfoRequest request, final ResponseHandler<ExternalInfoRequest> handler){
//		service.createExternalRequest(request, new BigBangAsyncCallback<ExternalInfoRequest>() {
//			
//			@Override
//			public void onResponseSuccess(ExternalInfoRequest result) {
//				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.EXTERNAL_REQUEST, result.id));
//				handler.onResponse(result);
//			}
//			
//			@Override
//			public void onResponseFailure(Throwable caught) {
//				handler.onError((new String[]{
//							new String("Could create the external request")
//					}));
//				super.onResponseFailure(caught);
//			}
//			
//		});
	}

	@Override
	public void getNegotiation(final String negotiationId, final ResponseHandler<Negotiation> handler){
		if(cache.contains(negotiationId) && !requiresRefresh){
			handler.onResponse((Negotiation) cache.get(negotiationId));
		}
		else{
			service.getNegotiation(negotiationId, new BigBangAsyncCallback<Negotiation>() {

				@Override
				public void onResponseSuccess(Negotiation result) {
					cache.add(negotiationId, result);
					incrementDataVersion();

					for(DataBrokerClient<Negotiation> bc : getClients()){
						((NegotiationBrokerClient)bc).updateNegotiation(result);
						((NegotiationBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.NEGOTIATION, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;	

				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError((new String[]{
							new String("Could not get the requested negotiation")
					}));
					super.onResponseFailure(caught);
				}

			});
		}
	}

	@Override
	public void updateNegotiation(Negotiation negotiation, final ResponseHandler<Negotiation> handler){

		service.editNegotiation(negotiation, new BigBangAsyncCallback<Negotiation>() {

			@Override
			public void onResponseSuccess(Negotiation result) {
				cache.add(result.id, result);
				incrementDataVersion();

				for(DataBrokerClient<Negotiation> bc : getClients()){
					((NegotiationBrokerClient)bc).updateNegotiation(result);
					((NegotiationBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.NEGOTIATION, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.UPDATE_NEGOTIATION, result.id));
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not save the negotiation")
				}));
				super.onResponseFailure(caught);
			}

		});

	}

	@Override
	public void removeNegotiation(final Deletion deletion, final ResponseHandler<String> handler){

		service.deleteNegotiation(deletion, new BigBangAsyncCallback<Void>() {


			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(deletion.negotiationId);
				incrementDataVersion();

				for(DataBrokerClient<Negotiation> bc : getClients()){
					((NegotiationBrokerClient)bc).removeNegotiation(deletion.negotiationId);
					((NegotiationBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.NEGOTIATION, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.DELETE_NEGOTIATION, deletion.negotiationId));
				handler.onResponse(deletion.negotiationId);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not delete the negotiation")
				}));
				super.onResponseFailure(caught);

			}

		});
	}

	@Override
	public void cancelNegotiation(final Cancellation cancellation, final ResponseHandler<Negotiation> handler){

		service.cancelNegotiation(cancellation, new BigBangAsyncCallback<Negotiation>() {

			@Override
			public void onResponseSuccess(Negotiation result) {
				cache.remove(result.id);
				cache.add(result.id, result);

				for(DataBrokerClient<Negotiation> bc : getClients()){
					((NegotiationBrokerClient)bc).updateNegotiation(result);
					((NegotiationBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.NEGOTIATION, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.CANCEL_NEGOTIATION, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not cancel the negotiation")
				}));
				super.onResponseFailure(caught);
			}

		});

	}
	
	@Override
	public void grantNegotiation(final Grant grant, final ResponseHandler<Negotiation> handler){
			
		service.grantNegotiation(grant, new BigBangAsyncCallback<Negotiation>() {
			
			@Override
			public void onResponseSuccess(Negotiation result) {
				cache.remove(result.id);
				cache.add(result.id, result);

				for(DataBrokerClient<Negotiation> bc : getClients()){
					((NegotiationBrokerClient)bc).updateNegotiation(result);
					((NegotiationBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.NEGOTIATION, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.GRANT_NEGOTIATION, grant.negotiationId));
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not grant the negotiation")
				}));
				super.onResponseFailure(caught);
			}
			
		});
	}
	
	@Override
	public void receiveResponse(final Response response, final ResponseHandler<Negotiation> handler){

		service.receiveResponse(response, new BigBangAsyncCallback<Negotiation>() {

			@Override
			public void onResponseSuccess(Negotiation result) {
				cache.remove(result.id);
				cache.add(result.id, result);

				for(DataBrokerClient<Negotiation> bc : getClients()){
					((NegotiationBrokerClient)bc).updateNegotiation(result);
					((NegotiationBrokerClient)bc).setDataVersionNumber(BigBangConstants.EntityIds.NEGOTIATION, getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.RECEIVE_QUOTE, result.id));
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError((new String[]{
						new String("Could not receive response")
				}));
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void requireDataRefresh() {
		requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		getNegotiation(itemId, new ResponseHandler<Negotiation>(){

			@Override
			public void onResponse(Negotiation response) {
				return;	
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;

			}

		});

	}

	@Override
	public void notifyItemDeletion(String itemId) {
		if(cache.contains(itemId)){
			cache.remove(itemId);
		}

		for(DataBrokerClient<Negotiation> c: clients){
			((NegotiationBrokerClient) c).removeNegotiation(itemId);
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		getNegotiation(itemId, new ResponseHandler<Negotiation>(){

			@Override
			public void onResponse(Negotiation response) {
				return;	
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;

			}

		});
	}



}
