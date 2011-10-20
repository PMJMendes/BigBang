package bigBang.library.client.userInterface.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ChildContactsList;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.ContactView.Action;

public class ContactsNavigationPanel extends NavigationPanel {
	
	public ContactsNavigationPanel(){
		navBar.setText("Ficha de Contacto");
		navBar.setHeight("35px");
	}
	
	public void setMainContact(ContactView view){
		view.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
					navBar.setText("Ficha de Contacto");
				}
			}
		});
		setHomeWidget(view);
		wireView(view);
	}
	
	protected void wireView(final ContactView view) {
		view.registerActionHandler(new ActionInvokedEventHandler<ContactView.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> actionEvent) {
				switch(actionEvent.getAction()) {
					case EDIT:
						view.setReadOnly(false);
						break;
					case SAVE:
						GWT.log("should save now");
						view.getContact();
						break;
					case CANCEL:
						view.setContact(view.getContact());
						view.setReadOnly(true);
						break;
					case SHOW_CHILD_CONTACTS:
						showChildContactsList(view.getContact());
						
						break;
					default:
						break;
				}
			}
		});
	}
	
	protected void showContactView(Contact contact){
		ContactView newView = new ContactView();
		newView.setContact(contact);
		wireView(newView);
		newView.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
					navBar.setText("Ficha de Contacto");
				}
			}
		});
		navigateTo(newView);
	}
	
	protected void showChildContactsList(Contact parentContact){
		ChildContactsList childList = new ChildContactsList(parentContact);
		childList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Contact> selected = (ValueSelectable<Contact>) event.getFirstSelected();
				if(selected != null) {
					showContactView(selected.getValue());
				}
			}
		});
		childList.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
					navBar.setText("Contactos Filho");
				}
			}
		});
		navigateTo(childList);
	}
}
