package bigBang.library.interfaces;

import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TipifiedListServiceAsync
	extends Service
{
	void getListItems(String listId, AsyncCallback<TipifiedListItem[]> callback);
	void getListItemsFilter(String listId, String filterId, AsyncCallback<TipifiedListItem[]> callback);
	void createListItem(String listId, TipifiedListItem item, AsyncCallback<TipifiedListItem> callback);
	void saveListItem(String listId, TipifiedListItem item, AsyncCallback<TipifiedListItem> callback);
	void deleteListItem(String listId, String itemId, AsyncCallback<Void> callback);
}
