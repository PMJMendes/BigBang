package bigBang.library.interfaces;

import bigBang.definitions.shared.Document;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExchangeServiceAsync
	extends Service
{
	void getItems(boolean sent, AsyncCallback<ExchangeItemStub[]> callback);
	void getItemsAll(boolean sent, AsyncCallback<ExchangeItemStub[]> callback);
	void getFolder(String id, AsyncCallback<ExchangeItemStub[]> callback);
	void getItem(String id, AsyncCallback<ExchangeItem> callback);
	void getAttAsDoc(String emailId, String attachmentId, AsyncCallback<Document> callback);
}
