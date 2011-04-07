package bigBang.library.server;

import bigBang.library.shared.Contact;
import bigBang.library.shared.ContactsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ContactsServiceImpl extends RemoteServiceServlet implements ContactsService {

	private static final long serialVersionUID = 1L;

	@Override
	public Contact[] getContacts(String entityTypeId, String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact createContact(String entityTypeId, String entityId,
			Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact saveContact(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteContact(String id) {
		// TODO Auto-generated method stub
		
	}
}
