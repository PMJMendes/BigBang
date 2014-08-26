package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.OtherEntityBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.module.generalSystemModule.client.userInterface.OtherEntityBrokerClient;
import bigBang.module.generalSystemModule.interfaces.OtherEntityService;
import bigBang.module.generalSystemModule.interfaces.OtherEntityServiceAsync;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class OtherEntityBrokerImpl extends DataBroker<OtherEntity> implements OtherEntityBroker{

	protected OtherEntityServiceAsync service;
	protected boolean needsRefresh = true;

	public OtherEntityBrokerImpl() {
		this(OtherEntityService.Util.getInstance());
	}

	public OtherEntityBrokerImpl(OtherEntityServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.OTHER_ENTITY;
		cache.setThreshold(0);
	}

	@Override
	public void requireDataRefresh() {
		this.needsRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		getOtherEntity(itemId, new ResponseHandler<OtherEntity>() {

			@Override
			public void onResponse(OtherEntity response) {
				cache.add(response.id, response);
				incrementDataVersion();

				for(DataBrokerClient<OtherEntity> c : getClients()) {
					((OtherEntityBrokerClient) c).addOtherEntity(response);
					((OtherEntityBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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

		for(DataBrokerClient<OtherEntity> c : getClients()){
			((OtherEntityBrokerClient)c).removeOtherEntity(itemId);
			((OtherEntityBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		getOtherEntity(itemId, new ResponseHandler<OtherEntity>() {

			@Override
			public void onResponse(OtherEntity response) {
				cache.update(response.id, response);
				incrementDataVersion();

				for(DataBrokerClient<OtherEntity> c : getClients()) {
					((OtherEntityBrokerClient) c).updateOtherEntity(response);
					((OtherEntityBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getOtherEntities(final ResponseHandler<OtherEntity[]> handler) {
		if(needsRefresh){
			this.service.getOtherEntities(new BigBangAsyncCallback<OtherEntity[]>() {

				@Override
				public void onResponseSuccess(OtherEntity[] result) {
					OtherEntityBrokerImpl.this.cache.clear();
					for(int i = 0; i < result.length; i++) {
						cache.add(result[i].id, result[i]);
					}
					incrementDataVersion();

					for(DataBrokerClient<OtherEntity> c : getClients()){
						((OtherEntityBrokerClient)c).setOtherEntities(result);
						((OtherEntityBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}

					handler.onResponse(result);
					needsRefresh = false;
				}


				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get other entities list")	
					});					super.onResponseFailure(caught);
				}

			});
		}else{
			int size = this.cache.getNumberOfEntries();
			OtherEntity[] entities = new OtherEntity[size];

			int i = 0;

			for(Object o : this.cache.getEntries()){
				entities[i] = ((OtherEntity)o);
				i++;
			}

			handler.onResponse(entities);
		}
	}

	@Override
	public void getOtherEntity(String id, ResponseHandler<OtherEntity> handler) {
		if(!cache.contains(id)){
			handler.onError(new String[]{
					new String("Could not find the entity")
			});
		}else{
			handler.onResponse((OtherEntity) cache.get(id));
		}		
	}

	@Override
	public void createOtherEntity(OtherEntity entity,
			final ResponseHandler<OtherEntity> handler) {
		service.createOtherEntity(entity, new BigBangAsyncCallback<OtherEntity>() {

			@Override
			public void onResponseSuccess(OtherEntity result) {
				cache.add(result.id, result);
				incrementDataVersion();

				for(DataBrokerClient<OtherEntity> c : getClients()) {
					((OtherEntityBrokerClient) c).addOtherEntity(result);
					((OtherEntityBrokerClient) c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(ModuleConstants.OpTypeIDs.MANAGE_OTHER_ENTITIES, result.id));
				handler.onResponse(result);
			}


			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create entity")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void saveOtherEntity(OtherEntity entity,
			final ResponseHandler<OtherEntity> handler) {
		this.service.saveOtherEntity(entity, new BigBangAsyncCallback<OtherEntity>() {

			@Override
			public void onResponseSuccess(OtherEntity result) {
				cache.update(result.id, result);
				incrementDataVersion();

				for(DataBrokerClient<OtherEntity> c : getClients()){
					((OtherEntityBrokerClient)c).updateOtherEntity(result);
					((OtherEntityBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(ModuleConstants.OpTypeIDs.MANAGE_OTHER_ENTITIES, result.id));
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update entity")	
				});				
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void removeOtherEntity(final String entityId, final ResponseHandler<Void> handler) {
		this.service.deleteOtherEntity(entityId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(entityId);
				incrementDataVersion();

				for(DataBrokerClient<OtherEntity> c : getClients()){
					((OtherEntityBrokerClient)c).removeOtherEntity(entityId);
					((OtherEntityBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(ModuleConstants.OpTypeIDs.MANAGE_OTHER_ENTITIES, entityId));
				handler.onResponse(null);

			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete entity")	
				});				
				super.onResponseFailure(caught);
			}

		});
	}

}
