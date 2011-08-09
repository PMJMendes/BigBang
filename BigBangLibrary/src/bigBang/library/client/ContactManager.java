package bigBang.library.client;

import java.util.ArrayList;
import java.util.List;

import bigBang.definitions.shared.Contact;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContactManager implements HasHandlers {

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


	protected ContactsServiceAsync service;
	protected ArrayList<Contact> contacts;
	protected String entityId;
	protected boolean offlineMode = false;

	public ContactManager(){
		service = ContactsService.Util.getInstance();
		contacts = new ArrayList<Contact>();
	}

	public void setEntityInfo(String instanceId, AsyncCallback<Void> doneCallback) {
		this.entityId = instanceId;
		if(instanceId == null){
			contacts = null;
			doneCallback.onFailure(null);
		}else {
			fetchContacts(doneCallback);
		}
	}

	public void addContact(final Contact c, final AsyncCallback<Contact> callBack) {
		c.ownerId = this.entityId;
		if(offlineMode) {
			this.contacts.add(c);
			callBack.onSuccess(c);
		}else{
			service.createContact(this.entityId, c, new BigBangAsyncCallback<Contact>() {

				@Override
				public void onSuccess(Contact result) {
					contacts.add(c);
					callBack.onSuccess(result);
				}
			});
		}
	}

	public void updateContact(final Contact c, final AsyncCallback<Contact> callBack) {
		c.ownerId = this.entityId;
		if(offlineMode) {
			for(Contact ct : contacts) {
				if(ct.id.equals(c.id)){
					contacts.set(contacts.indexOf(ct), c);
					break;
				}
			}
			callBack.onSuccess(c);
		}else{
			service.saveContact(this.entityId, c, new BigBangAsyncCallback<Contact>() {

				@Override
				public void onSuccess(Contact result) {
					for(Contact ct : contacts) {
						if(ct.id.equals(c.id)){
							contacts.set(contacts.indexOf(ct), result);
							break;
						}
					}
					callBack.onSuccess(result);
				}
			});
		}
	}

	public void deleteContact(final Contact c, final AsyncCallback<Void> callback){
		c.ownerId = this.entityId;
		if(offlineMode) {
			contacts.remove(c);
			callback.onSuccess(null);
		}else{
			service.deleteContact(entityId, c, new BigBangAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					contacts.remove(c);
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

	public List<Contact> getContacts(){
		return this.contacts;
	}
	
	public Contact[] getContactsArray() {
		Contact[] result = new Contact[contacts.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = contacts.get(i);
		}
		return result;
	}

	protected void fetchContacts(final AsyncCallback<Void> callback){
		service.getContacts(this.entityId, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onSuccess(Contact[] result) {
				contacts = new ArrayList<Contact>();
				for(int i = 0; i < result.length; i++) {
					contacts.add(result[i]);
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
