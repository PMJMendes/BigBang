package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TipifiedListServiceAsync {

	void deleteListItem(String itemId, AsyncCallback<Void> callback);

	void createListItem(String[] item, AsyncCallback<String[]> callback);

	void getListItems(String listId, AsyncCallback<String[]> callback);

	void saveListItem(String[] item, AsyncCallback<String[]> callback);

}
