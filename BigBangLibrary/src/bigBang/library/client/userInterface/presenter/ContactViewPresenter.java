package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ContactOperationsToolBar;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.view.ContactView;
import bigBang.library.client.userInterface.view.ContactView.ContactEntry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ContactViewPresenter implements ViewPresenter, ContactsBrokerClient{

	private Contact contact;
	private boolean bound = false;
	private Display view;
	private ContactsBroker broker;
	private String ownerId;
	private String ownerTypeId;
	private String contactId;


	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		CREATE_CHILD_CONTACT,
		SHOW_CHILD_CONTACTS,
		ADD_NEW_DETAIL,
		DELETE_DETAIL, DELETE
	}

	public ContactViewPresenter(Display view){

		broker = (ContactsBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONTACT);
		this.setView((UIObject) view);

	}


	public interface Display{


		public void setContact(Contact contacto);
		public void addContactInfo(ContactInfo info);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		public void setEditable(boolean b);
		public List<ContactInfo> getContactInfoList();
		public ContactEntry initializeContactEntry();
		public Contact getContact();
		public void setSaveMode(boolean b);
		public void registerDeleteHandler(
				DeleteRequestEventHandler deleteRequestEventHandler);
		ContactOperationsToolBar getToolbar();
	}

	public void setContact(Contact contact){
		
		if(contact == null){
			view.addContactInfo(null);
			view.setSaveMode(true);
			view.setEditable(true);
			view.getToolbar().allowEdit(true);
			return;
		}
		this.contact = contact;
		view.setContact(contact);

		for(int i = 0; i<contact.info.length; i++){


			view.addContactInfo(contact.info[i]);

		}
		view.addContactInfo(null);
		view.setEditable(false);
		view.getToolbar().allowEdit(false);
	}

	@Override
	public void setView(UIObject view) {

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
	public void setParameters(HasParameters parameterHolder) {

		broker.unregisterClient(this);


		ownerId = parameterHolder.getParameter("id");
		contactId = parameterHolder.getParameter("contactid");
		ownerTypeId= parameterHolder.getParameter("ownertypeid");
		boolean hasPermissions = parameterHolder.getParameter("editpermission") != null;

		if(ownerId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar um contacto sem cliente associado."), TYPE.ALERT_NOTIFICATION));
			view.getToolbar().lockAll();
			view.setContact(null);
			view.setEditable(false);
			view.getToolbar().allowEdit(false);
			return;
		}
		else{
			broker.registerClient(this, ownerId);

			if(contactId == null){

				if(hasPermissions){
					setContact(null);
				}
				else{
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o contacto."), TYPE.ALERT_NOTIFICATION));
					view.getToolbar().lockAll();
					view.setContact(null);
					view.setEditable(false);
					view.getToolbar().allowEdit(false);
				}
			}
			else
			{
				if(!hasPermissions){
					view.getToolbar().lockAll();
				}
				broker.refreshContactsForOwner(ownerId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {

						broker.getContact(contactId, new ResponseHandler<Contact>() {

							@Override
							public void onResponse(Contact response) {
								contact = response;
								setContact(contact);
								view.getToolbar().setSaveModeEnabled(false);
								view.getToolbar().allowEdit(false);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
								navig.removeParameter("contactid");
								navig.removeParameter("operation");
								navig.removeParameter("ownertypeid");
								navig.removeParameter("editpermission");
								NavigationHistoryManager.getInstance().go(navig);
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o contacto pedido."), TYPE.ALERT_NOTIFICATION));
							}
						});

					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						// TODO Auto-generated method stub

					}
				});
			}
		}


	}

	public void bind() {
		if(bound){
			return;
		}

		view.registerDeleteHandler(new DeleteRequestEventHandler(){

			@Override
			public void onDeleteRequest(Object object) {

				//TODO APAGAR DA BD
				List<ContactInfo> list = view.getContactInfoList();
				
				for(ValueSelectable<ContactInfo> cont: list){

					if(cont.getValue() == object) {
						list.remove(cont);
						break;
					}

				}


			}


		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){

				case ADD_NEW_DETAIL: 
					addNewDetail();
					break;

				case CREATE_CHILD_CONTACT: 
					break;

				case CANCEL:
					setContact(contact);
					view.setEditable(false);
					view.setSaveMode(false);
					view.getToolbar().allowEdit(false);
					break;

				case EDIT: 
					view.setEditable(true);
					view.setSaveMode(true);
					view.getToolbar().allowEdit(true);
					break;

				case SAVE: 
					Contact temp = view.getContact();
					createUpdateContact(temp);
					break;
				case SHOW_CHILD_CONTACTS: 
					break;
				case DELETE:
					deleteContact(contact);
				}

			}

			private void deleteContact(Contact contact) {
			
				broker.removeContact(contactId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
						navig.removeParameter("contactid");
						navig.removeParameter("operation");
						navig.removeParameter("ownertypeid");
						navig.removeParameter("editpermission");
						NavigationHistoryManager.getInstance().go(navig);
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Contacto eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o contacto."), TYPE.ALERT_NOTIFICATION));
						view.setSaveMode(true);
						view.setEditable(true);
						view.getToolbar().allowEdit(true);
					}
					
				
				});
				
			}

			private void createUpdateContact(Contact temp) {

				if(temp.id == null){

					temp.ownerId = ownerId;
					temp.ownerTypeId = ownerTypeId;
					broker.addContact(temp, new ResponseHandler<Contact>() {

						@Override
						public void onResponse(Contact response) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Contacto criado com sucesso."), TYPE.TRAY_NOTIFICATION));
							NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
							navig.setParameter("contactid", response.id);
							NavigationHistoryManager.getInstance().go(navig);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o contacto."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
							view.setEditable(true);
							view.getToolbar().allowEdit(true);
						}
					});
				}

				else
				{
					broker.updateContact(temp, new ResponseHandler<Contact>() {

						@Override
						public void onResponse(Contact response) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Contacto gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
							NavigationHistoryManager.getInstance().reload();
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o contacto."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
							view.setEditable(true);
							view.getToolbar().allowEdit(true);
						}
					});
				}

			}


		});

	}

	public void addNewDetail() {

		ContactEntry temp = view.initializeContactEntry();
		temp.setHeight("40px");
		view.getContactInfoList().remove(view.getContactInfoList().size()-1);
		temp.setEditable(true);
		view.getToolbar().allowEdit(true);
		view.getContactInfoList().add(temp);
		view.addContactInfo(null);

	}

	public ContactView getView() {
		return (ContactView)view;
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
	public void setContacts(String ownerId, java.util.List<Contact> contacts) {
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

		if(contact.id.equalsIgnoreCase(this.contact.id)){
			NavigationHistoryManager.getInstance().reload();
		}

	} 


}
