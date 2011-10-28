package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.TipifiedListItem;

/**
 * Interface for a client broker that manages the interaction between a TypifiedListClient and a TypifiedListService.
 * Should manage a number of clients and lists.
 *
 * @author Francisco Cabrita
 * @see TypifiedListClient
 * @see TypifiedListService
 */
public interface TypifiedListBroker {

	/**
	 * @param listId the id for typified list used by this client
	 * @param client the client to register
	 */
	public void registerClient(String listId, TypifiedListClient client);

	/**
	 * Unregisters a client for this broker
	 * @param listId The id of the list from which the client will be unregistered
	 * @param client The client to unregister
	 */
	public void unregisterClient(String listId, TypifiedListClient client);
	
	public boolean isClientRegistered(String listId, TypifiedListClient client);
	
	/**
	 * @param listId the id of list to be refreshed
	 */
	public void refreshListData(String listId);

	/**
	 * @param listId the list from which to get the items
	 * @return the list items
	 */
	public List<TipifiedListItem> getListItems(String listId);

	/**
	 * @param listId the id for the typified list into which the item will be inserted
	 * @param item the item to insert
	 * @param callback the asynchronous handler to be signaled when the operation was completed
	 */
	public void createListItem(String listId, TipifiedListItem item, ResponseHandler<TipifiedListItem> handler);

	/**
	 * @param listId the id for the typified list from which the item will be removed
	 * @param itemId the item to remove
	 * @param callback the asynchronous handler to be signaled when the operation was completed
	 */
	public void removeListItem(String listId, String itemId, ResponseHandler<TipifiedListItem> handler);

	/**
	 * @param listId the id for the typified list where the item is located
	 * @param item the item to update
	 * @param callback the asynchronous handler to be signaled when the operation was completed
	 */
	public void saveListItem(String listId, TipifiedListItem item, ResponseHandler<TipifiedListItem> handler); 

}
