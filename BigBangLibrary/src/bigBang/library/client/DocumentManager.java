package bigBang.library.client;

import java.util.ArrayList;
import java.util.List;

import bigBang.definitions.shared.Document;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.interfaces.DocumentServiceAsync;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentManager implements HasHandlers {

	public static class EntityChangedEvent extends GwtEvent<EntityChangedEventHandler>{

		public static Type<EntityChangedEventHandler> TYPE = new Type<EntityChangedEventHandler>();
		
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<EntityChangedEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(EntityChangedEventHandler handler) {
			handler.onEntityChanged(this);
		}
		
	}
	
	public interface EntityChangedEventHandler extends EventHandler {
		void onEntityChanged(EntityChangedEvent e);
	}


	protected DocumentServiceAsync service;
	protected ArrayList<Document> documents;
	protected String entityId;
	protected boolean offlineMode = false;

	public DocumentManager(){
		service = DocumentService.Util.getInstance();
		documents = new ArrayList<Document>();
	}

	public void setEntityInfo(String instanceId, AsyncCallback<Void> doneCallback) {
		this.entityId = instanceId;
		if(instanceId == null){
			documents = null;
			doneCallback.onFailure(null);
		}else {
			fetchDocuments(doneCallback);
		}
	}

	public void addDocument(final Document c, final AsyncCallback<Document> callBack) {
		c.ownerId = this.entityId;
		if(offlineMode) {
			this.documents.add(c);
			callBack.onSuccess(c);
		}else{
			service.createDocument(this.entityId, c, new BigBangAsyncCallback<Document>() {

				@Override
				public void onSuccess(Document result) {
					documents.add(c);
					callBack.onSuccess(result);
				}
			});
		}
	}

	public void updateDocument(final Document c, final AsyncCallback<Document> callBack) {
		c.ownerId = this.entityId;
		if(offlineMode) {
			for(Document ct : documents) {
				if(ct.id.equals(c.id)){
					documents.set(documents.indexOf(ct), c);
					break;
				}
			}
			callBack.onSuccess(c);
		}else{
			service.saveDocument(this.entityId, c, new BigBangAsyncCallback<Document>() {

				@Override
				public void onSuccess(Document result) {
					for(Document ct : documents) {
						if(ct.id.equals(c.id)){
							documents.set(documents.indexOf(ct), result);
							break;
						}
					}
					callBack.onSuccess(result);
				}
			});
		}
	}

	public void deleteDocument(final Document c, final AsyncCallback<Void> callback){
		c.ownerId = this.entityId;
		if(offlineMode) {
			documents.remove(c);
			callback.onSuccess(null);
		}else{
			//TODO
			service.deleteDocument(entityId, null, new BigBangAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					documents.remove(c);
					callback.onSuccess(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(null);
					super.onFailure(caught);
				}
			});
		}
	}

	public List<Document> getDocuments(){
		return this.documents;
	}
	
	public Document[] getDocumentsArray() {
		Document[] result = new Document[documents.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = documents.get(i);
		}
		return result;
	}

	protected void fetchDocuments(final AsyncCallback<Void> callback){
		service.getDocuments(this.entityId, new BigBangAsyncCallback<Document[]>() {

			@Override
			public void onSuccess(Document[] result) {
				documents = new ArrayList<Document>();
				for(int i = 0; i < result.length; i++) {
					documents.add(result[i]);
				}
				callback.onSuccess(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				callback.onFailure(null);
			}
			
		});
	}

	public void setOfflineMode(boolean offline) {
		this.offlineMode = offline;
	}

	public void addEntityChangedEventHandler(EntityChangedEventHandler h){
		
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		
	}
	
}
