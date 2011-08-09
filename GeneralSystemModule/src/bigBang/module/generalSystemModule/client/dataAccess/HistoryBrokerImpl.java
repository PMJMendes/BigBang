package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.HistoryDataBrokerClient;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.HistoryService;
import bigBang.library.interfaces.HistoryServiceAsync;

public class HistoryBrokerImpl extends DataBroker<HistoryItemStub> implements
		HistoryBroker {

	protected static final String ALL_PROCESSES = "";
	
	protected HistoryServiceAsync service;
	protected Map<String, List<HistoryDataBrokerClient>> clientProcessRegistrations;
	protected Map<String, Boolean> dataRefreshRequests;
	
	public HistoryBrokerImpl(){
		clientProcessRegistrations = new HashMap<String, List<HistoryDataBrokerClient>>();
		dataRefreshRequests = new HashMap<String, Boolean>();
		service = HistoryService.Util.getInstance();
		
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
	public void registerClient(DataBrokerClient<HistoryItemStub> client,
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
	public void registerClient(DataBrokerClient<HistoryItemStub> client) {
		registerClient(client, ALL_PROCESSES);
	}
	
	@Override
	public void unregisterClient(DataBrokerClient<HistoryItemStub> client) {
		unregisterClient(client, ALL_PROCESSES);
	}

	@Override
	public void unregisterClient(DataBrokerClient<HistoryItemStub> client,
			String processId) {
		if(!clientProcessRegistrations.containsKey(processId))
			return;
		List<HistoryDataBrokerClient> processHistoryItems = clientProcessRegistrations.get(processId);
		processHistoryItems.remove(client);
	}

	@Override
	public void getItems(String processId,
			ResponseHandler<HistoryItemStub[]> handler) {
		handler.onResponse(new HistoryItemStub[0]);
		//TODO FJVC
	}

	@Override
	public void getItem(String itemId, String processId, ResponseHandler<HistoryItemStub> handler) {
		handler.onResponse(new HistoryItemStub());
		/*if(!dataRefreshRequests.containsKey(processId)){
			throw new RuntimeException("The given process id is not managed by the history broker at the moment : " + processId);
		}
		
		boolean isRefreshNeeded = dataRefreshRequests.get(processId);
		
		if(isRefreshNeeded || !cache.contains(itemId)){
			service.
		}else{
			handler.onResponse((ProcessUndoItem) cache.get(itemId));
		}*/ //TODO FJVC
	}

	@Override
	public void undo(String undoItemId, final ResponseHandler<HistoryItemStub> handler) {
		this.service.undo(undoItemId, new BigBangAsyncCallback<HistoryItemStub>() {

			@Override
			public void onSuccess(HistoryItemStub result) {
				if(cache.contains(result.id))
					cache.update(result.id, result);
				handler.onResponse(result);
			}
		});
	}
	
	
}
