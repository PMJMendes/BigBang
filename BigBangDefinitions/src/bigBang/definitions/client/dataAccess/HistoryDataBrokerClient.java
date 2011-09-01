package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.HistoryItem;

public interface HistoryDataBrokerClient extends DataBrokerClient<HistoryItem> {

	/**
	 * Sets the History items for a given process
	 * @param processId The id of the process
	 * @param items The history items
	 */
	public void setHistoryItems(String processId, HistoryItem[] items);
	
	/**
	 * Adds a process item to the client
	 * @param processId The id of the process to which the item belongs
	 * @param item The history item
	 */
	public void addHistoryItem(String processId, HistoryItem item);
	
	/**
	 * Updates an item in the client
	 * @param processId The id of the process to which the item belongs 
	 * @param item The item to be updated
	 */
	public void updateHistoryItem(String processId, HistoryItem item);
	
	/**
	 * Removes an item form the client
	 * @param processId The id of the process to which the item belongs
	 * @param item The item to be removed
	 */
	public void removeHistoryItem(String processId, HistoryItem item);

}
