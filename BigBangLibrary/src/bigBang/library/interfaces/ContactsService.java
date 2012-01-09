package bigBang.library.interfaces;

import bigBang.definitions.shared.Contact;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ContactsService")
public interface ContactsService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ContactsServiceAsync instance;
		public static ContactsServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ContactsService.class);
			}
			return instance;
		}
	}

	public Contact[] getContacts(String ownerId) throws SessionExpiredException, BigBangException;
	public Contact createContact(Contact contact) throws SessionExpiredException, BigBangException;
	public Contact saveContact(Contact contact) throws SessionExpiredException, BigBangException;
	public void deleteContact(String id) throws SessionExpiredException, BigBangException;
}
