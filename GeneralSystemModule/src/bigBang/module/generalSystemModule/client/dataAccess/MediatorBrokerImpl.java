package bigBang.module.generalSystemModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.MediatorBroker;
import bigBang.definitions.client.dataAccess.MediatorDataBrokerClient;
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
				public void onSuccess(Mediator[] result) {
					cache.clear();
					for(int i = 0; i < result.length; i++){
						cache.add(result[i].id, result[i]);
					}

					for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
						((MediatorDataBrokerClient)c).setMediators(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
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
		if(!cache.contains(mediatorId))
			throw new RuntimeException("The requested mediator could not be found locally. id:\"" + mediatorId + "\"");
		handler.onResponse((Mediator) cache.get(mediatorId));
	}


	@Override
	public void addMediator(Mediator mediator, final ResponseHandler<Mediator> handler) {
		this.service.createMediator(mediator, new BigBangAsyncCallback<Mediator>() {

			@Override
			public void onSuccess(Mediator result) {
				cache.add(result.id, result);
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).addMediator(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateMediator(Mediator mediator,
			final ResponseHandler<Mediator> handler) {
		this.service.saveMediator(mediator, new BigBangAsyncCallback<Mediator>() {

			@Override
			public void onSuccess(Mediator result) {
				cache.update(result.id, result);
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).updateMediator(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeMediator(final String mediatorId,
			final ResponseHandler<Mediator> handler) {
		this.service.deleteMediator(mediatorId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(mediatorId);
				for(DataBrokerClient<Mediator> c : MediatorBrokerImpl.this.getClients()){
					((MediatorDataBrokerClient)c).removeMediator(mediatorId);
				}
				handler.onResponse(null);
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

}
