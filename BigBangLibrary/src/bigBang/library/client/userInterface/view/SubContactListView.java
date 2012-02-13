package bigBang.library.client.userInterface.view;

import java.util.Arrays;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.SubContactListViewPresenter;

public class SubContactListView extends View implements SubContactListViewPresenter.Display{
	
	VerticalPanel wrapper;
	List<Contact> subContacts;
	
	public SubContactListView(){
		
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("410px", "500px");
		
	}

	@Override
	public void setSubContacts(Contact[] contacts) {
	
		for(int i = 0; i<contacts.length; i++){
			subContacts.add(new ListEntry<Contact>(contacts[i]));
		}
		
	}

	@Override
	protected void initializeView() {
		return;
	}

}
