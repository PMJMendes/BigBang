package bigBang.module.generalSystemModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.ClientGroupBroker;
import bigBang.definitions.client.dataAccess.ClientGroupDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ClientGroup;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.generalSystemModule.interfaces.ClientGroupService;
import bigBang.module.generalSystemModule.interfaces.ClientGroupServiceAsync;

public class ClientGroupBrokerImpl extends DataBroker<ClientGroup> implements ClientGroupBroker {

	protected ClientGroupServiceAsync service;
	protected int dataVersion;
	protected boolean needsRefresh = true;

	public ClientGroupBrokerImpl(){
		this(ClientGroupService.Util.getInstance());
	}

	public ClientGroupBrokerImpl(ClientGroupServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.CLIENT_GROUP;
		cache.setThreshold(0);
	}

	@Override
	public void getClientGroups(final ResponseHandler<ClientGroup[]> handler) {
		if(needsRefresh()){
			service.getClientGroupList(new BigBangAsyncCallback<ClientGroup[]>() {

				@Override
				public void onSuccess(ClientGroup[] result) {
					cache.clear();
					for(int i = 0; i < result.length; i++){
						cache.add(result[i].id, result[i]);
					}

					for(DataBrokerClient<ClientGroup> c : ClientGroupBrokerImpl.this.getClients()){
						((ClientGroupDataBrokerClient)c).setGroups(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
			});
		}else{
			handler.onResponse((ClientGroup[]) cache.getEntries().toArray());
		}
	}

	@Override
	public void getClientGroup(String groupId,
			ResponseHandler<ClientGroup> handler) {
		if(!cache.contains(groupId))
			throw new RuntimeException("The requested client group could not be found locally. id:\"" + groupId + "\"");
		handler.onResponse((ClientGroup) cache.get(groupId));		
	}

	@Override
	public void addClientGroup(ClientGroup group,
			final ResponseHandler<ClientGroup> handler) {
		service.createClientGroup(group, new BigBangAsyncCallback<ClientGroup>() {

			@Override
			public void onSuccess(ClientGroup result) {
				cache.add(result.id, result);
				for(DataBrokerClient<ClientGroup> c : getClients()){
					((ClientGroupDataBrokerClient)c).addGroup(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateClientGroup(ClientGroup group,
			final ResponseHandler<ClientGroup> handler) {
		this.service.saveClientGroup(group, new BigBangAsyncCallback<ClientGroup>() {

			@Override
			public void onSuccess(ClientGroup result) {
				cache.update(result.id, result);
				for(DataBrokerClient<ClientGroup> c : ClientGroupBrokerImpl.this.getClients()){
					((ClientGroupDataBrokerClient)c).updateGroup(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeClientGroup(final String groupId,
			final ResponseHandler<ClientGroup> handler) {
		this.service.deleteClientGroup(groupId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(groupId);
				for(DataBrokerClient<ClientGroup> c : ClientGroupBrokerImpl.this.getClients()){
					((ClientGroupDataBrokerClient)c).removeGroup(groupId);
				}
				handler.onResponse(null);
			}
		});
	}
	
	/**
	 * Returns whether or not the cache should be refreshed
	 * @return true if the cache needs to be refreshed and false otherwise
	 */
	protected boolean needsRefresh(){
		return this.needsRefresh;
	}

	@Override
	public void requireDataRefresh() {
		this.needsRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		//TODO FJVC
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		requireDataRefresh();
		//TODO
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		//TODO
	}

}
