package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Contact;

public interface ContactsServiceAsync
	extends Service
{
	void getContacts(String ownerId, AsyncCallback<Contact[]> callback);
	void createContact(Contact contact, AsyncCallback<Contact> callback);
	void saveContact(Contact contact, AsyncCallback<Contact> callback);
	void deleteContact(String id, AsyncCallback<Void> callback);
	void getFlatEmails(String ownerId, AsyncCallback<Contact[]> callback);
}
