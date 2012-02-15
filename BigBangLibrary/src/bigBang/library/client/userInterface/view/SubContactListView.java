package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;
import bigBang.library.client.userInterface.presenter.SubContactListViewPresenter;

public class SubContactListView extends View implements SubContactListViewPresenter.Display{
	
	VerticalPanel wrapper;
	List<Contact> subContacts;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public SubContactListView(){
		
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("410px", "500px");
		
		subContacts = new List<Contact>();
		wrapper.add(subContacts.getScrollable());
		
		
	}

	@Override
	public void setSubContacts(Contact[] contacts) {
	
		
		ListEntry<Contact> temp;
		Label tempLabel;
		for(int i = 0; i<contacts.length; i++){
			temp = new ListEntry<Contact>(contacts[i]);
			tempLabel = new Label(temp.getValue().name);
			temp.setWidget(tempLabel);
			subContacts.add(temp);
		}
		
	}

	@Override
	protected void initializeView() {
		return;
	}
	

@Override
public List<Contact> getList() {
	return subContacts;
}

}
