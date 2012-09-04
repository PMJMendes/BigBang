package bigBang.library.interfaces;

import bigBang.definitions.shared.HistoryItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HistoryServiceAsync
	extends SearchServiceAsync
{
	void getItem(String undoItemId, AsyncCallback<HistoryItem> callback);
	void undo(String undoItemId, AsyncCallback<HistoryItem> callback);
}
