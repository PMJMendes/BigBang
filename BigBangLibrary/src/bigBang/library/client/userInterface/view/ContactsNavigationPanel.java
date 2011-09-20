package bigBang.library.client.userInterface.view;

import com.google.gwt.event.logical.shared.AttachEvent;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.NavigationPanel;

public class ContactsNavigationPanel extends NavigationPanel {
	public ContactsNavigationPanel(){
		navBar.setText("Ficha de Contacto");
		navBar.setHeight("30px");
	}
	
	public void setMainContact(ContactView view){
		setHomeWidget(view);
		wireView(view);
	}
	
	protected void wireView(final ContactView view) {
		view.getChildContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getSelected().size() > 0){
					@SuppressWarnings("unchecked")
					Contact nextContact = ((ValueSelectable<Contact>)event.getFirstSelected()).getValue();
					ContactView nextView = new ContactView() {
						
						@Override
						public void onSaveContact(Contact contact) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onCreateSubContact() {
							// TODO Auto-generated method stub
							
						}
					};
					nextView.setContact(nextContact);
					navigateTo(nextView);
				}
			}
		});
		view.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					view.getChildContactsList().clear();
				}
			}
		});
	}
}
