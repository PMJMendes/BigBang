package bigBang.library.client.userInterface.view;

import java.util.List;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.ContactManagementContactList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ContactManagementViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactNavigationViewPresenter.Display;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HorizontalPanel;


public class ContactManagementView extends View implements ContactManagementViewPresenter.Display{

	private ContactManagementContactList list;
	private ContactNavigationView navView;

	public ContactManagementView() {
		HorizontalPanel panel = new HorizontalPanel();
		initWidget(panel);
		panel.setSize("905px", "680px");

		list = new ContactManagementContactList();
		navView = new ContactNavigationView();
		panel.add(list);
		panel.add(navView);
		list.allowCreation(true);

	}

	@Override
	protected void initializeView() {
		return;		
	}

	@Override
	public Display getNavView() {
		return navView;
	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts){
		list.setContacts(ownerId, contacts);
	}

	@Override
	public HasValueSelectables<Contact> getContactList() {
		return list;
	}

	@Override
	public void setListOwner(String ownerId){
		list.setOwner(ownerId);
	}

	@Override
	public HasClickHandlers getCreateButton(){
		return list.getCreateNewButton();
	}

	@Override
	public void setNewContact(ListEntry<Contact> newContact) {
		list.add(0,newContact);
		list.clearSelection();
		newContact.setSelected(true);
	}

	@Override
	public boolean removeNewContact() {
		return list.removeContactWithId("new");
	}
}
