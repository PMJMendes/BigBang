package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;

public class SubContactListViewPresenter implements  ViewPresenter,ContactsBrokerClient{
	
	private ContactsBroker broker;
	private String ownerId;
	private String contactId;
	private Display view;
	private Contact contact;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public interface Display{
		
		public void setSubContacts(Contact[] contact);
		public Widget asWidget();
		public List<Contact> getList();
		
	}
	
	public SubContactListViewPresenter(Display view){
		
		broker = (ContactsBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONTACT);
		this.setView((UIObject) view);
		 
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
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				fireAction(Action.CHILD_SELECTED);
			}
		});
		
	}


	public Display getView(){
		return view;
	}

	protected void fireAction(Action action) {
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}
	
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;

	}



	@Override
	public void setParameters(HasParameters parameterHolder) {
		

		contactId = parameterHolder.getParameter("contactid");
		ownerId = parameterHolder.getParameter("ownerid");
		
		broker.refreshContactsForOwner(ownerId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {

				broker.getContact(contactId, new ResponseHandler<Contact>() {

					@Override
					public void onResponse(Contact response) {
						contact = response;
						view.setSubContacts(contact.subContacts);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar os sub-contactos."), TYPE.ALERT_NOTIFICATION));
					}
				});

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				
				
			}
		});
		
	}



	@Override
	public void setContacts(String ownerId, java.util.List<Contact> contacts) {
		// TODO Auto-generated method stub
		
	}

}
