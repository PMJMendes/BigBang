package bigBang.module.quoteRequestModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.NegotiationBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Deletion;
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

				handler.onResponse(result);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.UPDATE_NEGOTIATION, result.id));

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

				handler.onResponse(deletion.negotiationId);
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.NegotiationProcess.DELETE_NEGOTIATION, deletion.negotiationId));
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
