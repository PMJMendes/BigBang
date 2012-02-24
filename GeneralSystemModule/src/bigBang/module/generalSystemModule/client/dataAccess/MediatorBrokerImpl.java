package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.MediatorBroker;
import bigBang.definitions.client.dataAccess.MediatorDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Mediator;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.generalSystemModule.interfaces.MediatorService;
import bigBang.module.generalSystemModule.interfaces.MediatorServiceAsync;

public class MediatorBrokerImpl extends DataBroker<Mediator> implements MediatorBroker {

	protected MediatorServiceAsync service;
	protected int dataVersion;
	protected boolean needsRefresh = true;

	public MediatorBrokerImpl(){
		this(MediatorService.Util.getInstance());
	}

	public MediatorBrokerImpl(MediatorServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.MEDIATOR;
		cache.setThreshold(0);
	}

	@Override
	public void getMediators(final ResponseHandler<Mediator[]> handler) {
		if(needsRefresh()){
			service.getMediators(new BigBangAsyncCallback<Mediator[]>() {

				@Override
				public void onResponseSuccess(Mediator[] result) {
					cache.clear();
					for(int i = 0; i < result.length; i++){
						cache.add(result[i].id, result[i]);
					}
					incrementDataVersion();
					for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
						((MediatorDataBrokerClient)c).setMediators(result);
						((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
				
				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
						new String("Could not get the mediators list")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			int size = this.cache.getNumberOfEntries();
			Mediator[] mediators = new Mediator[size];
			
			int i = 0;
			for(Object o : this.cache.getEntries()){
				mediators[i] = ((Mediator) o);
				i++;
			}
			handler.onResponse(mediators);
		}
	}

	@Override
	public void getMediator(String mediatorId,
			ResponseHandler<Mediator> handler) {
		if(!cache.contains(mediatorId)){
			handler.onError(new String[]{
					new String("Could not get the requested mediator. id: " + mediatorId)
			});
		}else{
			handler.onResponse((Mediator) cache.get(mediatorId));
		}
	}


	@Override
	public void addMediator(Mediator mediator, final ResponseHandler<Mediator> handler) {
		this.service.createMediator(mediator, new BigBangAsyncCallback<Mediator>() {

			@Override
			public void onResponseSuccess(Mediator result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).addMediator(result);
					((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not create mediator")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateMediator(Mediator mediator,
			final ResponseHandler<Mediator> handler) {
		this.service.saveMediator(mediator, new BigBangAsyncCallback<Mediator>() {

			@Override
			public void onResponseSuccess(Mediator result) {
				cache.update(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).updateMediator(result);
					((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not update mediator")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void removeMediator(final String mediatorId,
			final ResponseHandler<Mediator> handler) {
		this.service.deleteMediator(mediatorId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(mediatorId);
				incrementDataVersion();
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).removeMediator(mediatorId);
					((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not delete mediator")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void requireDataRefresh() {
		this.needsRefresh = true;
	}

	/**
	 * Returns whether or not the broker needs to refresh its data
	 * @return True if the data needs to be refreshed, false otherwise
	 */
	public boolean needsRefresh(){
		return this.needsRefresh;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		getMediator(itemId, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).addMediator(response);
					((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
		requireDataRefresh();
		incrementDataVersion();
		for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
			((MediatorDataBrokerClient)c).removeMediator(itemId);
			((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		getMediator(itemId, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).updateMediator(response);
					((MediatorDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

}
