package bigBang.library.interfaces;

import bigBang.definitions.shared.Document;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExchangeServiceAsync
	extends Service
{
	void getItems(AsyncCallback<ExchangeItemStub[]> callback);
	void getItemsAll(AsyncCallback<ExchangeItemStub[]> callback);
	void getFolder(String id, AsyncCallback<ExchangeItemStub[]> callback);
	void getItem(String folderId, String id, AsyncCallback<ExchangeItem> callback);
	void getAttAsDoc(String emailId, String folderId, String attachmentId, AsyncCallback<Document> callback);
}
