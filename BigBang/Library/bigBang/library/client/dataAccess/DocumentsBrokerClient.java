package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.Document;

public interface DocumentsBrokerClient extends DataBrokerClient<Document> {
	public int getDocumentsDataVersionNumber(String ownerId);
	
	public void setDocumentsDataVersionNumber(String ownerId, int number);
	
	public void setDocuments(String ownerId, List<Document> documents);
	
	public void removeDocument(String ownerId, Document document);
	
	public void addDocument(String ownerId, Document document);
	
	public void updateDocument(String ownerId, Document document);
}
