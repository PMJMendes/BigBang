package bigBang.library.client.userInterface;

import com.google.gwt.event.logical.shared.AttachEvent;

import bigBang.definitions.shared.Contact;

public class ChildContactsList extends FilterableList<Contact> {
	
	public static class Entry extends NavigationListEntry<Contact>{

		public Entry(Contact value) {
			super(value);
		}
		
		public <I extends Object> void setInfo(I info) {
			Contact c = (Contact) info;
			this.setTitle(c.name);
		};
		
	}
	
	public ChildContactsList(Contact parentContact){
		showFilterField(false);
		showSearchField(true);
		
		Contact[] childContacts = parentContact.subContacts == null ? new Contact[0] : parentContact.subContacts;
		for(int i = 0; i < childContacts.length; i++) {
			Entry entry = new Entry(childContacts[i]);
			add(entry);
		}
		
		this.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					clearSelection();
				}
			}
		});
	}
}
