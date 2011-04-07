package bigBang.library.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ContactsService")
public interface ContactsService extends RemoteService {
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
	
	public Contact[] getContacts(String entityTypeId, String entityId);
	
	public Contact createContact(String entityTypeId, String entityId, Contact contact);
	
	public Contact saveContact(Contact contact);
	
	public void deleteContact(String id);
	
}
