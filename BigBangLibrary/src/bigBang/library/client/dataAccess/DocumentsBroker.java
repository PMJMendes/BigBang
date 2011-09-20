package bigBang.library.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Document;

public interface DocumentsBroker extends DataBrokerInterface<Document>{
	
	public void getDocuments(String ownerId, ResponseHandler<Collection<Document>> handler);
	public void getDocument(String id, ResponseHandler<Document> handler);
	public void createDocument(String procId, String opId, ResponseHandler<Document> handler);
	public void updateDocument(String procId, String opId, ResponseHandler<Document> handler);
	public void deleteDocument(String procId, String opId, ResponseHandler<Document> handler);
	
}
