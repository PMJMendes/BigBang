package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientGroupBroker;
import bigBang.definitions.client.dataAccess.ClientGroupDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
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
				public void onResponseSuccess(ClientGroup[] result) {
					cache.clear();
					for(int i = 0; i < result.length; i++){
						cache.add(result[i].id, result[i]);
					}
					incrementDataVersion();
					for(DataBrokerClient<ClientGroup> c : ClientGroupBrokerImpl.this.getClients()){
						((ClientGroupDataBrokerClient)c).setGroups(result);
						((ClientGroupDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());						
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
				
				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
						new String("Could not fetch the list of client groups")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
			handler.onResponse((ClientGroup[]) cache.getEntries().toArray());
		}
	}

	@Override
	public void getClientGroup(String groupId,
			ResponseHandler<ClientGroup> handler) {
		if(!cache.contains(groupId)){
			handler.onError(new String[]{
				new String("The requested client group was not found. id: " + groupId)	
			});
		}else{
			handler.onResponse((ClientGroup) cache.get(groupId));
		}
	}

	@Override
	public void addClientGroup(ClientGroup group,
			final ResponseHandler<ClientGroup> handler) {
		service.createClientGroup(group, new BigBangAsyncCallback<ClientGroup>() {

			@Override
			public void onResponseSuccess(ClientGroup result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<ClientGroup> c : getClients()){
					((ClientGroupDataBrokerClient)c).addGroup(result);
					((ClientGroupDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not create client group.")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateClientGroup(ClientGroup group,
			final ResponseHandler<ClientGroup> handler) {
		this.service.saveClientGroup(group, new BigBangAsyncCallback<ClientGroup>() {

			@Override
			public void onResponseSuccess(ClientGroup result) {
				cache.update(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<ClientGroup> c : ClientGroupBrokerImpl.this.getClients()){
					((ClientGroupDataBrokerClient)c).updateGroup(result);
					((ClientGroupDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not update client group")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void removeClientGroup(final String groupId,
			final ResponseHandler<ClientGroup> handler) {
		this.service.deleteClientGroup(groupId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				cache.remove(groupId);
				incrementDataVersion();
				for(DataBrokerClient<ClientGroup> c : ClientGroupBrokerImpl.this.getClients()){
					((ClientGroupDataBrokerClient)c).removeGroup(groupId);
					((ClientGroupDataBrokerClient)c).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete client group")
				});
				super.onResponseFailure(caught);
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
		this.getClientGroup(itemId, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<ClientGroup> client : clients) {
					((ClientGroupDataBrokerClient)client).addGroup(response);
					((ClientGroupDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
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
		this.cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<ClientGroup> client : clients) {
			((ClientGroupDataBrokerClient)client).removeGroup(itemId);
			((ClientGroupDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		this.getClientGroup(itemId, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<ClientGroup> client : clients) {
					((ClientGroupDataBrokerClient)client).updateGroup(response);
					((ClientGroupDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

}
