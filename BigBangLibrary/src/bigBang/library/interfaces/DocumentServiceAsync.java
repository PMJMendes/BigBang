package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Document;

public interface DocumentServiceAsync
	extends Service
{
	void getDocuments(String ownerId, AsyncCallback<Document[]> callback);
	void createDocument(Document document, AsyncCallback<Document> callback);
	void saveDocument(Document document, AsyncCallback<Document> callback);
	void deleteDocument(String id, AsyncCallback<Void> callback);
}
