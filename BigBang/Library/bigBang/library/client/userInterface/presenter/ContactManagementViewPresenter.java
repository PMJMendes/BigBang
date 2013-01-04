package bigBang.library.client.userInterface.presenter;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Selectable;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.ListEntry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ContactManagementViewPresenter implements ViewPresenter, ContactsBrokerClient{

	private ContactNavigationViewPresenter navViewPres;
	protected Display view;
	private String ownerId;
	private ContactsBroker broker;
	private boolean bound;
	private String ownerTypeId;

	public interface Display{

		Widget asWidget();
		bigBang.library.client.userInterface.presenter.ContactNavigationViewPresenter.Display getNavView();
		void setContacts(String ownerId, List<Contact> contacts);
		HasValueSelectables<Contact> getContactList();
		void setListOwner(String ownerId);
		HasClickHandlers getCreateButton();
		void setNewContact(ListEntry<Contact> newContact);
		boolean removeNewContact();

	}


	public ContactManagementViewPresenter(Display view) {

		setView((UIObject)view);
		navViewPres = new ContactNavigationViewPresenter(view.getNavView()){
			@Override
			protected void onCancel(ContactViewPresenter presenter,
					HasParameters parameterHolder) {
				super.onCancel(presenter, parameterHolder);
				if(ContactManagementViewPresenter.this.view.removeNewContact()){
					setContact(null);
				}	
			}
		};
		broker = (ContactsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONTACT);
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
		if(bound){
			return;
		}

		view.getCreateButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onNewContact();
			}
		});
		view.getContactList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getSelected() != null && event.getSelected().size()>0){
					for(Selectable cont: event.getSelected()){
						setContact((ListEntry<Contact>) cont);
					}
				}
				
			}
		});


		bound = true;
	}

	protected void setContact(ListEntry<Contact> cont) {
		HasParameters params = new HasParameters();
		params.setParameter("ownerid", ownerId);
		params.setParameter("ownertypeid", ownerTypeId);
		params.setParameter("contactid", cont != null ? cont.getValue().id : null);
		params.setParameter("managementpanel", "yes");
		navViewPres.setParameters(params);
	}

	protected void onNewContact() {
		view.removeNewContact();
		Contact newCont = new Contact();
		newCont.name = "Novo Contacto";
		newCont.id = "new";
		ContactsList.Entry newContact = new ContactsList.Entry(newCont);
		view.setNewContact(newContact);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		ownerId = parameterHolder.getParameter("ownerid");
		ownerTypeId = parameterHolder.getParameter("ownertypeid");
		broker.registerClient(this, ownerId);

		if(ownerId != null){
			broker.getContacts(ownerId, new ResponseHandler<List<Contact>>() {

				@Override
				public void onResponse(List<Contact> response) {
					view.setContacts(ownerId, response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os contactos."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}else{
			onNoOwner();
		}

		navViewPres.setParameters(parameterHolder);
		view.setListOwner(ownerId);
	}

	private void onNoOwner() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar contactos sem processo associado."), TYPE.ALERT_NOTIFICATION));		
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;		
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return 0;		
	}

	@Override
	public int getContactsDataVersionNumber(String ownerId) {
		return 0;		
	}

	@Override
	public void setContactsDataVersionNumber(String ownerId, int number) {
		return;		

	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts) {
		return;		

	}

	@Override
	public void removeContact(String ownerId, String contactId) {
		return;		

	}

	@Override
	public void addContact(String ownerId, Contact contact) {

	}

	@Override
	public void updateContact(String ownerId, Contact contact) {
		return;		

	}

}
