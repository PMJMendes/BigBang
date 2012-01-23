package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.shared.TipifiedListItem;

public interface TypifiedTextClient {

	/**
	 * @return the version of the data held by the client
	 */
	public int getTypifiedDataVersionNumber();
	
	/**
	 * @param number the data version number
	 */
	public void setTypifiedDataVersionNumber(int number);
	
	/**
	 * @param items sets the items
	 */
	public void setTypifiedListItems(List<TipifiedListItem> items);
	
	/**
	 * Indicates that the client should remove the item if it is cached.
	 * @param item the item to remove
	 */
	public void removeItem(TipifiedListItem item);
	
	/**
	 * Indicates that the client should take notice of a new item
	 * @param item the item to add
	 */
	public void addItem(TipifiedListItem item);
	
	/**
	 * Indicates that the client should update its cached representation of the item
	 * @param item the item to update
	 */
	public void updateItem(TipifiedListItem item);
	
}
