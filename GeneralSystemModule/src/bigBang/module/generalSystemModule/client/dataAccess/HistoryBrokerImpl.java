package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBroker;
import bigBang.library.client.dataAccess.DataBrokerClient;
import bigBang.library.client.dataAccess.HistoryBroker;
import bigBang.library.client.dataAccess.HistoryDataBrokerClient;
import bigBang.library.client.response.ResponseHandler;
import bigBang.library.interfaces.UndoService;
import bigBang.library.interfaces.UndoServiceAsync;
import bigBang.library.shared.ProcessUndoItem;

public class HistoryBrokerImpl extends DataBroker<ProcessUndoItem> implements
		HistoryBroker {

	protected static final String ALL_PROCESSES = "";
	
	protected UndoServiceAsync service;
	protected Map<String, List<HistoryDataBrokerClient>> clientProcessRegistrations;
	protected Map<String, Boolean> dataRefreshRequests;
	
	public HistoryBrokerImpl(){
		clientProcessRegistrations = new HashMap<String, List<HistoryDataBrokerClient>>();
		dataRefreshRequests = new HashMap<String, Boolean>();
		service = UndoService.Util.getInstance();
		
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
	public void registerClient(DataBrokerClient<ProcessUndoItem> client,
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
	public void registerClient(DataBrokerClient<ProcessUndoItem> client) {
		registerClient(client, ALL_PROCESSES);
	}
	
	@Override
	public void unregisterClient(DataBrokerClient<ProcessUndoItem> client) {
		unregisterClient(client, ALL_PROCESSES);
	}

	@Override
	public void unregisterClient(DataBrokerClient<ProcessUndoItem> client,
			String processId) {
		if(!clientProcessRegistrations.containsKey(processId))
			return;
		List<HistoryDataBrokerClient> processHistoryItems = clientProcessRegistrations.get(processId);
		processHistoryItems.remove(client);
	}

	@Override
	public void getItems(String processId,
			ResponseHandler<ProcessUndoItem[]> handler) {
		handler.onResponse(new ProcessUndoItem[0]);
		//TODO FJVC
	}

	@Override
	public void getItem(String itemId, String processId, ResponseHandler<ProcessUndoItem> handler) {
		handler.onResponse(new ProcessUndoItem());
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
	public void undo(String undoItemId, final ResponseHandler<ProcessUndoItem> handler) {
		this.service.undo(undoItemId, new BigBangAsyncCallback<ProcessUndoItem>() {

			@Override
			public void onSuccess(ProcessUndoItem result) {
				if(cache.contains(result.id))
					cache.update(result.id, result);
				handler.onResponse(result);
			}
		});
	}
	
	
}
