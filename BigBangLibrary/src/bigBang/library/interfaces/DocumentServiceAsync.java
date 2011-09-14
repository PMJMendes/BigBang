package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Document;

public interface DocumentServiceAsync
	extends Service
{
	void getDocuments(String ownerId, AsyncCallback<Document[]> callback);
	void createDocument(String procId, String opId, Document document, AsyncCallback<Document> callback);
	void saveDocument(String procId, String opId, Document document, AsyncCallback<Document> callback);
	void deleteDocument(String procId, String opId, String id, AsyncCallback<Void> callback);
}
