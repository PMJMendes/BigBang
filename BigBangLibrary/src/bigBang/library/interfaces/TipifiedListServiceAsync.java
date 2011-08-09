package bigBang.library.interfaces;

import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TipifiedListServiceAsync
	extends Service
{
	void deleteListItem(String listId, String itemId, AsyncCallback<Void> callback);
	void createListItem(String listId, TipifiedListItem item, AsyncCallback<TipifiedListItem> callback);
	void getListItems(String listId, AsyncCallback<TipifiedListItem[]> callback);
	void saveListItem(String listId, TipifiedListItem item, AsyncCallback<TipifiedListItem> callback);
}
