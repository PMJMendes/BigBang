package bigBang.library.client.dataAccess;

import bigBang.library.client.response.ResponseHandler;
import bigBang.library.shared.ProcessUndoItem;

public interface HistoryBroker extends DataBrokerInterface<ProcessUndoItem> {

	/**
	 * Returns whether or not the history data for a process needs to be refreshed
	 * @param processId The id of the process for which to require the data refresh
	 */
	public void requireDataRefresh(String processId);
	
	/**
	 * Registers a client for this data broker for a given process id
	 * @param client The client to be registered
	 * @param processId The id of the process to watch
	 */
	public void registerClient(DataBrokerClient<ProcessUndoItem> client, String processId);
	
	/**
	 * Voids the client registration for a given process
	 * @param client The client to be unregistered
	 * @param processId The watched process id
	 */
	public void unregisterClient(DataBrokerClient<ProcessUndoItem> client, String processId);
	
	/**
	 * Gets the history items for a given process
	 * @param processId The id of the process to which the history items belong
	 * @param handler The handler to be notified on response
	 */
	public void getItems(String processId, ResponseHandler<ProcessUndoItem[]> handler);
	
	/**
	 * Gets a specific history item
	 * @param itemId The id of the item
	 * @param The id of the process to which the item belongs
	 * @param handler The handler to be notified on response 
	 */
	public void getItem(String itemId, String processId, ResponseHandler<ProcessUndoItem> handler);
	
	/**
	 * Reverts a given history item
	 * @param undoItemId The history item
	 * @param handler The handler to be notified on response
	 */
	public void undo(String undoItemId, ResponseHandler<ProcessUndoItem> handler);
	
}
