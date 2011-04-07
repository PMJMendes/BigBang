package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.shared.Document;

public interface DocumentServiceAsync extends Service {

	void createDocument(String entityTypeId, String entityId,
			Document document, AsyncCallback<Document> callback);

	void deleteDocument(String id, AsyncCallback<Void> callback);

	void getDocuments(String entityTypeId, String entityId,
			AsyncCallback<Document[]> callback);

	void saveDocument(Document document, AsyncCallback<Document> callback);

}
