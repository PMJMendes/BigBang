package bigBang.library.client.userInterface.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.ContactsBrokerClient;

public class SubContactListViewPresenter implements  ViewPresenter,ContactsBrokerClient{
	
	public interface Display{
		
		public void setSubContacts(Contact[] contact);
		
	}
	
	

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getContactsDataVersionNumber(String ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setContactsDataVersionNumber(String ownerId, int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeContact(String ownerId, String contactId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addContact(String ownerId, Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateContact(String ownerId, Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(UIObject view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}

}
