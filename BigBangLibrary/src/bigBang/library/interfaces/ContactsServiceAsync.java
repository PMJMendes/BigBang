package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.shared.Contact;

public interface ContactsServiceAsync extends Service {

	void createContact(String entityTypeId, String entityId, Contact contact,
			AsyncCallback<Contact> callback);

	void deleteContact(String id, AsyncCallback<Void> callback);

	void getContacts(String entityTypeId, String entityId,
			AsyncCallback<Contact[]> callback);

	void saveContact(Contact contact, AsyncCallback<Contact> callback);

}
