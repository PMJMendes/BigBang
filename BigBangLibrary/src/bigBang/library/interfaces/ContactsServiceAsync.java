package bigBang.library.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.shared.Contact;

public interface ContactsServiceAsync
	extends Service
{
	void getContacts(String ownerId, AsyncCallback<Contact[]> callback);
	void createContact(String opInstanceId, Contact contact, AsyncCallback<Contact> callback);
	void saveContact(String opInstanceId, Contact contact, AsyncCallback<Contact> callback);
	void deleteContact(String opInstanceId, Contact contact, AsyncCallback<Void> callback);
}
