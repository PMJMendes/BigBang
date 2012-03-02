package bigBang.library.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Document;

public interface DocumentsBroker extends DataBrokerInterface<Document>{
	/**
	 * Sets the current data version to the next possible and valid value.
	 * @param ownerId The id of the owner of the contacts
	 */
	public int incrementDataVersion(String ownerId);
	
	/**
	 * Checks if all registered clients have the correct version number.
	 * @return true if all clients have the correct data version and false otherwise.
	 */
	public boolean checkClientDataVersions();
	
	/**
	 * Checks if all registered clients for the contacts of a given owner have the correct version number.
	 * @param ownerId The id of the owner of the contacts
	 * @return true if all clients have the correct data version and false otherwise.
	 */
	public boolean checkClientDataVersions(String ownerId);
	
	/**
	 * Gets the current data version being held by the broker for a given owner
	 * @param ownerId The is of the owner of the contacts
	 * @return The data version number
	 */
	public int getCurrentDataVersion(String ownerId);
	
	/**
	 * Registers a client for this data broker.
	 * @param client The client to be registered
	 * @param ownerId The id of the owner of the documents
	 */
	public void registerClient(DocumentsBrokerClient client, String ownerId);
	
	/**
	 * Voids the client registration
	 * @param client The client to be unregistered
	 */
	public void unregisterClient(DocumentsBrokerClient client);
	
	/**
	 * Voids the client registration for the contacts of a given owner
	 * @param client The client to be unregistered
	 * @param ownerId The id of the owner of the documents
	 */
	public void unregisterClient(DocumentsBrokerClient client, String ownerId);
	
	/**
	 * Gets the clients for the data broker for a given document owner
	 * @param ownerId The id of the owner of the documents
	 * @return A collection with the registered clients in the data broker
	 */
	public Collection<DocumentsBrokerClient> getClients(String ownerId);
	
	/**
	 * Requires que data broker to refresh its currently held data
	 */
	public void requireDataRefresh();
	
	/**
	 * Requires que data broker to refresh its currently held data for a given contact owner
	 * @param ownerId The id of the owner of the contacts
	 */
	public void requireDataRefresh(String ownerId);
	
	public void getDocuments(String ownerId, ResponseHandler<Collection<Document>> handler);
	public void getDocument(String ownerId, String documentId, ResponseHandler<Document> handler);
	public void createDocument(Document document, ResponseHandler<Document> handler);
	public void updateDocument(Document document, ResponseHandler<Document> handler);
	public void deleteDocument(String documentId, ResponseHandler<Void> handler);
	public void closeDocumentResource(String ownerId, String documentId, ResponseHandler<Void> handler);
	
}
