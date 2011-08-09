package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Document;

public interface DocumentServiceAsync
	extends Service
{
	void getDocuments(String ownerId, AsyncCallback<Document[]> callback);
	void createDocument(String opInstanceId, Document document, AsyncCallback<Document> callback);
	void saveDocument(String opInstanceId, Document document, AsyncCallback<Document> callback);
	void deleteDocument(String opInstanceId, String id, AsyncCallback<Void> callback);
}
