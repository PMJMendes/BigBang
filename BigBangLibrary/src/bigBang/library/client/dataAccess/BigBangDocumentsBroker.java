package bigBang.library.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Document;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;

public class BigBangDocumentsBroker extends DataBroker<Document> implements DocumentsBroker {

	public static class Util {
		private static DocumentsBroker instance;
		public DocumentsBroker getInstance(){
			if(instance == null){
				instance = new BigBangDocumentsBroker();
			}
			return instance;
		}
	}
	
	protected ContactsServiceAsync service;
	
	public BigBangDocumentsBroker(){
		this.service = ContactsService.Util.getInstance();
	}
	
	@Override
	public void getDocuments(String ownerId,
			ResponseHandler<Collection<Document>> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getDocument(String id, ResponseHandler<Document> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDocument(String procId, String opId,
			ResponseHandler<Document> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDocument(String procId, String opId,
			// TODO Auto-generated method stub
			ResponseHandler<Document> handler) {
		
	}

	@Override
	public void deleteDocument(String procId, String opId,
			ResponseHandler<Document> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requireDataRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub
		
	}

}
