package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItem.AlteredItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.HistoryService;
import bigBang.library.interfaces.HistoryServiceAsync;

public class HistoryBrokerImpl extends DataBroker<HistoryItem> implements
HistoryBroker {

	protected static final String ALL_PROCESSES = "";

	protected HistoryServiceAsync service;
	protected Map<String, List<HistoryDataBrokerClient>> clientProcessRegistrations;
	protected Map<String, Boolean> dataRefreshRequests;
	protected SearchDataBroker<HistoryItemStub> searchBroker; 

	public HistoryBrokerImpl(){
		clientProcessRegistrations = new HashMap<String, List<HistoryDataBrokerClient>>();
		dataRefreshRequests = new HashMap<String, Boolean>();
		service = HistoryService.Util.getInstance();
		this.searchBroker = new SearchDataBrokerImpl<HistoryItemStub>(this.service);

		this.dataElementId = BigBangConstants.EntityIds.HISTORY;
	}

	@Override
	public void requireDataRefresh() {
		this.requireDataRefresh(ALL_PROCESSES);
	}

	@Override
	public void requireDataRefresh(String processId) {
		processId = processId == null ? ALL_PROCESSES : processId;
		dataRefreshRequests.put(processId, true);
	}

	/**
	 * Returns whether or not a process data requires a refresh
	 * @param processId The process for which the data needs to be refreshed
	 * @return True if the data needs refresh
	 */
	protected boolean requiresDataRefresh(String processId) {
		return dataRefreshRequests.get(processId);
	}

	@Override
	public void registerClient(DataBrokerClient<HistoryItem> client,
			String processId) {
		ArrayList<HistoryDataBrokerClient> processHistoryClients = null;
		processId = processId == null ? ALL_PROCESSES : processId;

		if(!clientProcessRegistrations.containsKey(processId)){
			processHistoryClients = new ArrayList<HistoryDataBrokerClient>();
			dataRefreshRequests.put(processId, true);
		}else{
			processHistoryClients = (ArrayList<HistoryDataBrokerClient>) clientProcessRegistrations.get(processId);
		}
		processHistoryClients.add((HistoryDataBrokerClient) client);
	}

	@Override
	public void registerClient(DataBrokerClient<HistoryItem> client) {
		registerClient(client, ALL_PROCESSES);
	}

	@Override
	public void unregisterClient(DataBrokerClient<HistoryItem> client) {
		unregisterClient(client, ALL_PROCESSES);
	}

	@Override
	public void unregisterClient(DataBrokerClient<HistoryItem> client,
			String processId) {
		if(!clientProcessRegistrations.containsKey(processId))
			return;
		List<HistoryDataBrokerClient> processHistoryItems = clientProcessRegistrations.get(processId);
		processHistoryItems.remove(client);
	}

	@Override
	public void getItems(String processId,
			ResponseHandler<HistoryItem[]> handler) {
		Collection<ResponseError> errors = new ArrayList<ResponseError>();
		ResponseError error = new ResponseError();
		error.description = "Please use the search broker to get the items";
		errors.add(error);
		handler.onError(errors);
	}

	@Override
	public void getItem(String itemId, String processId, final ResponseHandler<HistoryItem> handler) {
		if(!dataRefreshRequests.containsKey(processId)){
			throw new RuntimeException("The given process id is not managed by the history broker at the moment : " + processId);
		}

		boolean isRefreshNeeded = dataRefreshRequests.get(processId);

		if(isRefreshNeeded || !cache.contains(itemId)){
			service.getItem(itemId, new BigBangAsyncCallback<HistoryItem>() {

				@Override
				public void onSuccess(HistoryItem result) {
					handler.onResponse(result);
				}
			});
		}else{
			handler.onResponse((HistoryItem) cache.get(itemId));
		}
	}

	@Override
	public void undo(String undoItemId, final ResponseHandler<HistoryItem> handler) {
		this.service.undo(undoItemId, new BigBangAsyncCallback<HistoryItem>() {

			@Override
			public void onSuccess(HistoryItem result) {
				if(cache.contains(result.id))
					cache.update(result.id, result);
				for(int i = 0; i < result.alteredEntities.length; i++) {
					AlteredItem entity = result.alteredEntities[i];
					if(DataBrokerManager.Util.getInstance().hasBrokerImplementationForDataElement(entity.typeId)){
						DataBroker<?> itemBroker = DataBrokerManager.Util.getInstance().getBroker(entity.typeId);
						for(int j = 0; j < entity.createdIds.length; j++) {
							itemBroker.notifyItemCreation(entity.createdIds[j]);
						}
						for(int j = 0; j < entity.modifiedIds.length; j++) {
							itemBroker.notifyItemUpdate(entity.modifiedIds[j]);
						}
						for(int j = 0; j < entity.deletedIds.length; j++) {
							itemBroker.notifyItemDeletion(entity.deletedIds[j]);
						}
					}
				}
				handler.onResponse((HistoryItem) result);
			}
		});
	}

	@Override
	public SearchDataBroker<HistoryItemStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		return;
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		return;
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		return;
	}

}
