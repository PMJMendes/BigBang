package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.view.ContactView2.ContactEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public abstract class ContactViewPresenter implements ViewPresenter{

	private Contact contact;
	private boolean bound = false;
	private Display view;


	public interface Display{

		
		public void setContact(Contact contacto);
		public void addContactInfo(ContactInfo info);
		Widget asWidget();
	}

	@Override
	public void setService(Service service) {
		// TODO Auto-generated method stub

	}
	
	public void setContact(Contact contact){
		
		this.contact = contact;
		view.setContact(contact);
		ContactEntry temp;
		
		for(int i = 0; i<contact.info.length; i++){


			view.addContactInfo(contact.info[i]);

		}
		view.addContactInfo(null);
		
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setView(View view) {
		
		this.view = (Display)view;

	}

	@Override
	public void go(HasWidgets container) {
		bind();
		bound = true;
		container.clear();
		container.add(this.view.asWidget());

	}

	@Override
	public void bind() {
		if(bound){
			return;
		}

	}


	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

}
