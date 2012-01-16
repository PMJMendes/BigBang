package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Contact;

public interface ContactsServiceAsync
	extends Service
{
	void getContacts(String ownerId, AsyncCallback<Contact[]> callback);
	void createContact(String procId, String opId, Contact contact, AsyncCallback<Contact> callback);
	void saveContact(String procId, String opId, Contact contact, AsyncCallback<Contact> callback);
	void deleteContact(String procId, String opId, Contact contact, AsyncCallback<Void> callback);
}
