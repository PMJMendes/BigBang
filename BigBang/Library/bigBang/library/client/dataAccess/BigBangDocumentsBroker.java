package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Document;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.interfaces.DocumentServiceAsync;
import bigBang.library.interfaces.FileService;

public class BigBangDocumentsBroker extends DataBroker<Document> implements DocumentsBroker {

	public static class Util {
		private static DocumentsBroker instance;
		public static DocumentsBroker getInstance(){
			if(instance == null){
				instance = new BigBangDocumentsBroker();
			}
			return instance;
		}
	}

	protected static final int NO_DATA_VERSION = 0;

	protected DocumentServiceAsync service;
	private Map<String, List<Document>> documents;
	private Map<String, Boolean> dataRefreshRequirements;
	private Map<String, List<DocumentsBrokerClient>> clients;
	private Map<String, Integer> dataVersions;

	public BigBangDocumentsBroker(){
		this.service = DocumentService.Util.getInstance();
		this.dataElementId = BigBangConstants.EntityIds.DOCUMENT;
		dataVersions = new HashMap<String, Integer>();
		documents = new HashMap<String, List<Document>>();
		dataRefreshRequirements = new HashMap<String, Boolean>();
		clients = new HashMap<String, List<DocumentsBrokerClient>>();
	}

	@Override
	public int incrementDataVersion(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		int newDataVersion = new Integer(dataVersions.get(ownerId).intValue() + 1);
		dataVersions.put(ownerId, newDataVersion);
		return newDataVersion;
	}

	@Override
	public boolean checkClientDataVersions() {
		boolean result = true;
		for(String o : clients.keySet()){
			result &= checkClientDataVersions(o);
		}
		return result;
	}

	@Override
	public boolean checkClientDataVersions(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		for(DocumentsBrokerClient c : clients.get(ownerId)) {
			if(dataVersions.get(ownerId) != c.getDocumentsDataVersionNumber(ownerId)){
				return false;
			}
		}
		return true;  
	}

	@Override
	public int getCurrentDataVersion(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		return dataVersions.get(ownerId);
	}

	@Override
	public void registerClient(final DocumentsBrokerClient client, final String ownerId) {
		if(!clients.containsKey(ownerId)){
			List<DocumentsBrokerClient> clientList = new ArrayList<DocumentsBrokerClient>();
			clients.put(ownerId, clientList);
			//documents.put(ownerId, new ArrayList<Document>());
			requireDataRefresh(ownerId);
			dataVersions.put(ownerId, NO_DATA_VERSION);	
			getDocuments(ownerId, new ResponseHandler<Collection<Document>>() {

				@Override
				public void onResponse(Collection<Document> response) {
					clients.get(ownerId).add(client);
					updateClient(ownerId, client);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
		}else{
			clients.get(ownerId).add(client);
			updateClient(ownerId, client);
		}
	}

	@Override
	public void unregisterClient(DocumentsBrokerClient client) {
		for(String ownerId : clients.keySet()){
			List<DocumentsBrokerClient> clientList = clients.get(ownerId);
			if(clientList.contains(client)){
				unregisterClient(client, ownerId);
			}
		}
	}

	@Override
	public void unregisterClient(DocumentsBrokerClient client, String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		List<DocumentsBrokerClient> clientList = clients.get(ownerId);
		clientList.remove(client);
	}

	protected void clean(){
		Set<String> keys = this.clients.keySet();
		for(String listId : keys) {
			List<DocumentsBrokerClient> clientList = this.clients.get(listId);
			if(clientList.isEmpty()){
				dataVersions.remove(listId);
				documents.remove(listId);
				dataRefreshRequirements.remove(listId);
				clients.remove(listId);
			}
		}
	}

	@Override
	public Collection<DataBrokerClient<Document>> getClients() {
		Collection<DataBrokerClient<Document>> result = new ArrayList<DataBrokerClient<Document>>();
		for(List<DocumentsBrokerClient> clientList : clients.values()){
			result.addAll(clientList);
		}
		return result;
	}

	@Override
	public Collection<DocumentsBrokerClient> getClients(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		return clients.get(ownerId);
	}

	@Override
	public void requireDataRefresh() {
		for(String ownerId : dataRefreshRequirements.keySet()){
			requireDataRefresh(ownerId);
		}
	}

	@Override
	public void requireDataRefresh(String ownerId) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The contacts for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		dataRefreshRequirements.put(ownerId, true);
	}

	protected boolean requiresDataRefresh(String ownerId){
		return this.dataRefreshRequirements.get(ownerId);
	}

	@Override
	public void getDocuments(final String ownerId,
			final ResponseHandler<Collection<Document>> handler) {
		if(ownerId.equalsIgnoreCase("new")){
			handler.onResponse(null);
			return;
		}
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		if(requiresDataRefresh(ownerId)){
			service.getDocuments(ownerId, new BigBangAsyncCallback<Document[]>() {

				@Override
				public void onResponseSuccess(Document[] result) {
					List<Document> documentsList = new ArrayList<Document>();
					for(int i = 0; i < result.length; i++) {
						documentsList.add(result[i]);
					}
					documents.put(ownerId, documentsList);
					incrementDataVersion(ownerId);
					updateClients(ownerId);
					handler.onResponse(documentsList);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the documents for the specified owner id")
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			handler.onResponse(documents.get(ownerId));
		}
	}

	@Override
	public void getDocument(final String ownerId, final String documentId,
			final ResponseHandler<Document> handler) {
		if(!clients.containsKey(ownerId)){
			throw new RuntimeException("The documents for the owner with id " + ownerId + " are not being managed by this broker.");
		}
		if(!documents.containsKey(ownerId)){
			getDocuments(ownerId, new ResponseHandler<Collection<Document>>() {
				@Override
				public void onResponse(Collection<Document> response) {
					getDocument(ownerId, documentId, handler);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					handler.onError(errors);
				}
			});
		}else{
			Collection<Document> documentsCollection = documents.get(ownerId);
			boolean hasDocument = false;
			for(Document c : documentsCollection) {
				if(c.id.equalsIgnoreCase(documentId)){
					service.getDocument(documentId, new BigBangAsyncCallback<Document>() {

						@Override
						public void onResponseSuccess(Document result) {
							handler.onResponse(result);
						}

						@Override
						public void onResponseFailure(Throwable caught) {
							handler.onError(new String[]{
									new String("Could not get the document")	
							});
							super.onResponseFailure(caught);
						}
					});
					hasDocument = true;
					break;
				}
			}
			if(!hasDocument){
				handler.onError(new String[]{
						new String("Could not get the required document")
				});
			}
		}
	}

	@Override
	public void createDocument(Document document, final ResponseHandler<Document> handler) {
		service.createDocument(document, new BigBangAsyncCallback<Document>() {

			@Override
			public void onResponseSuccess(Document result) {
				Collection<Document> documentsList = documents.get(result.ownerId);
				documentsList.add(result);
				incrementDataVersion(result.ownerId);
				updateClients(result.ownerId);
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the document")
				});
				super.onResponseFailure(caught);
			}

		});
	}
	
	@Override
	public void createDocumentSerial(Document document, final ResponseHandler<Document> handler){
		service.createDocument(document, new BigBangAsyncCallback<Document>() {

			@Override
			public void onResponseSuccess(Document result) {
				handler.onResponse(result);				
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create the document")
				});
				super.onResponseFailure(caught);
			}
		
		});
	}


	@Override
	public void updateDocument(final Document document, final ResponseHandler<Document> handler) {
		getDocument(document.ownerId, document.id, new ResponseHandler<Document>() {

			@Override
			public void onResponse(final Document response) {
				service.saveDocument(document, new BigBangAsyncCallback<Document>() {

					@Override
					public void onResponseSuccess(Document result) {
						if(response.fileStorageId != document.fileStorageId || 
								(response.fileStorageId != null && document.fileStorageId != null && !document.fileStorageId.equalsIgnoreCase(response.fileStorageId))){
							FileService.Util.getInstance().Discard(response.fileStorageId, new BigBangAsyncCallback<Void>() {

								@Override
								public void onResponseSuccess(Void result) {
									return;
								}
							});
						}

						List<Document> documentsList = documents.get(result.ownerId);
						for(Document document : documentsList) {
							if(document.id.equalsIgnoreCase(result.id)){
								documentsList.set(documentsList.indexOf(document), result);
								break;
							}
						}
						updateClients(result.ownerId);
						handler.onResponse(result);
					}

					@Override
					public void onResponseFailure(Throwable caught) {
						handler.onError(new String[]{
								new String("Could not save the document")
						});
						super.onResponseFailure(caught);
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the original document")	
				});
			}
		});
	}

	@Override
	public void deleteDocument(final String documentId, final ResponseHandler<Void> handler) {
		service.deleteDocument(documentId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				for(Collection<Document> collection : documents.values()){
					for(Document document : collection){
						if(document.id.equalsIgnoreCase(documentId)){
							collection.remove(document);
							incrementDataVersion(document.ownerId);
							for(DocumentsBrokerClient client : clients.get(document.ownerId)){
								client.removeDocument(document.ownerId, document);
								client.setDataVersionNumber(documentId, getCurrentDataVersion(document.ownerId));
							}
							handler.onResponse(null);
							return;
						}
					}
				}
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not delete the document")
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void closeDocumentResource(String ownerId, String documentId, final ResponseHandler<Void> handler) {
		getDocument(ownerId, documentId, new ResponseHandler<Document>() {

			@Override
			public void onResponse(Document response) {
				if(response.fileStorageId != null){
					FileService.Util.getInstance().Discard(response.fileStorageId, new BigBangAsyncCallback<Void>() {

						@Override
						public void onResponseSuccess(Void result) {
							handler.onResponse(null);
						}

						@Override
						public void onResponseFailure(Throwable caught) {
							handler.onError(new String[]{
									new String("Could not close the document file resource")	
							});
							super.onResponseFailure(caught);
						}

					});
				}else{
					handler.onResponse(null);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not find the document locally")	
				});
			}
		});
	}

	/**
	 * Updates the clients to the latest version of the info
	 * @param ownerId The id of the owner
	 */
	protected void updateClients(String ownerId) {
		List<DocumentsBrokerClient> clientList = clients.get(ownerId);
		int currentVersion = dataVersions.get(ownerId);
		for(DocumentsBrokerClient c : clientList){
			if(c.getDocumentsDataVersionNumber(ownerId) != currentVersion){
				updateClient(ownerId, c);
			}
		}
	}

	/**
	 * Updates a given client to the latest version of the contact information
	 * @param ownerId The owner of the contacts
	 * @param client The client to be updated
	 */
	protected void updateClient(String ownerId, DocumentsBrokerClient client) {
		int currentVersion = dataVersions.get(ownerId);
		client.setDocuments(ownerId, documents.get(ownerId));
		client.setDocumentsDataVersionNumber(ownerId, currentVersion);
	}

	@Override
	public void notifyItemCreation(String itemId) {
	}

	@Override
	public void notifyItemDeletion(String itemId) {
	}

	@Override
	public void notifyItemUpdate(String itemId) {
	}

}
