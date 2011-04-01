package bigBang.library.client.userInterface;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditableList<T> extends List {

	public class EntryCreatedEvent extends GwtEvent<EntryCreatedEventHandler> {

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<EntryCreatedEventHandler> getAssociatedType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void dispatch(EntryCreatedEventHandler handler) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public interface EntryCreatedEventHandler extends EventHandler {
		public void onEntryCreated();
	}
	
	
	protected ListEntry<T> temporaryEntry;
	
	public EditableList() {
		super();
	}
	
	/*public void createEntry(){
		temporaryEntry = new ListEntry<T>(null);
		temporaryEntry.setText("-");
		addListEntry(temporaryEntry);
		select(temporaryEntry);
		scrollPanel.scrollToBottom();
	}
	
	public boolean hasTemporaryEntry(){
		return temporaryEntry != null;
	}
	
	@Override
	public void select(ListEntry<T> entry) {
		if(entry != temporaryEntry)
			removeListEntry(temporaryEntry);
		super.select(entry);
	}*/

}
