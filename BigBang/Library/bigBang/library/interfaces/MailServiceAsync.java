package bigBang.library.interfaces;

import bigBang.definitions.shared.Document;
import bigBang.library.shared.MailItem;
import bigBang.library.shared.MailItemStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailServiceAsync
	extends Service
{
	void getItems(AsyncCallback<MailItemStub[]> callback);
	void getItemsAll(AsyncCallback<MailItemStub[]> callback);
	void getFolder(String id, AsyncCallback<MailItemStub[]> callback);
	void getItem(String folderId, String id, AsyncCallback<MailItem> callback);
	void getAttAsDoc(String emailId, String folderId, String attachmentId, AsyncCallback<Document> callback);
}
