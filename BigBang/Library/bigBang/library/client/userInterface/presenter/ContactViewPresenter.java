package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox;

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
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
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
	private ActionInvokedEventHandler<Action> actionHandler;
	private HasParameters parameterHolder;


	public static enum Action{
		SAVE,
		EDIT,
		CANCEL, 
		CREATE_CHILD_CONTACT,
		ADD_NEW_DETAIL, DELETE, CLOSE_POPUP, CHILD_SELECTED, ERROR_SHOWING_CONTACT, ATTACHED, CONTACT_CREATED
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
		void setSubContacts(Contact[] contacts);
		List<Contact> getSubContactList();
		public void addSubContact(Contact contact);
	}

	public void setContact(Contact contact){

		if(contact == null){
			view.addContactInfo(null);
			view.setSaveMode(true);
			view.setEditable(true);
			return;
		}
		this.contact = contact;
		view.setContact(contact);
		view.setSubContacts(contact.subContacts);

		for(int i = 0; i<contact.info.length; i++){


			view.addContactInfo(contact.info[i]);

		}
		view.addContactInfo(null);
		view.setEditable(false);
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

		this.parameterHolder = parameterHolder;
		view.setSaveMode(false);
		broker.unregisterClient(this);
		ownerId = parameterHolder.getParameter("ownerid");
		contactId = parameterHolder.getParameter("contactid");
		ownerTypeId= parameterHolder.getParameter("ownertypeid");
	//	boolean hasPermissions = parameterHolder.getParameter("editpermission") != null;

		if(ownerId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar um contacto sem cliente associado."), TYPE.ALERT_NOTIFICATION));
			view.getToolbar().lockAll();
			view.setContact(null);
			view.setEditable(false);
			return;
		}
		else{
			broker.registerClient(this, ownerId);

			if(contactId.equalsIgnoreCase("new")){

				setContact(null);
				view.getToolbar().allowEdit(true);
				
			}
			else
			{
				broker.refreshContactsForOwner(ownerId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {

						broker.getContact(contactId, new ResponseHandler<Contact>() {

							@Override
							public void onResponse(Contact response) {
								contact = response;
								setContact(contact);
								view.getToolbar().setSaveModeEnabled(false);
								view.getToolbar().allowEdit(true);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o contacto pedido."), TYPE.ALERT_NOTIFICATION));
								fireAction(Action.ERROR_SHOWING_CONTACT);
							}
						});

					}

					@Override
					public void onError(Collection<ResponseError> errors) {

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

				case ATTACHED:
					parameterHolder.setParameter("contactid", view.getContact().id);
					setParameters(parameterHolder);
					break;

				case CREATE_CHILD_CONTACT: 
					Contact temp2 = view.getContact();
					createChildContact(temp2);
					break;

				case CANCEL:
					if(contactId.equalsIgnoreCase("new") && !ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)){
						NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
						navig.removeParameter("show");
						navig.removeParameter("contactid");
						navig.removeParameter("ownerid");
						navig.removeParameter("ownertypeid");
						NavigationHistoryManager.getInstance().go(navig);
					}
					else{
						fireAction(Action.CANCEL);
					}
					break;

				case EDIT: 
					view.setEditable(true);
					view.setSaveMode(true);
					break;

				case SAVE:
					Contact temp = view.getContact();
					createUpdateContact(temp);
					break;

				case DELETE:{
					MessageBox.confirm("Eliminar contacto", "Tem certeza que pretende eliminar o contacto seleccionado?", new MessageBox.ConfirmationCallback() {

						@Override
						public void onResult(boolean result) {
							if(result){
								deleteContact(contact);
							}

						}

					});
					break;
				}
				}

			}

			private void deleteContact(Contact contact) {

				if(!contactId.equalsIgnoreCase("new")){
					broker.removeContact(contactId, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							fireAction(Action.CLOSE_POPUP);
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Contacto eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o contacto."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
							view.setEditable(true);
						}


					});

				}
				else
					fireAction(Action.CLOSE_POPUP);
			}

			private void createUpdateContact(Contact temp) {

				if(temp.id == null){

					temp.ownerId = ownerId;
					temp.ownerTypeId = ownerTypeId;
					broker.addContact(temp, new ResponseHandler<Contact>() {

						@Override
						public void onResponse(Contact response) {
							if(ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)){
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Contacto criado com sucesso."), TYPE.TRAY_NOTIFICATION));
								ContactViewPresenter.this.setContact(response);
								contactId = response.id;
								view.setSaveMode(false);
								view.setEditable(false);
								fireAction(Action.CONTACT_CREATED);
							}else{
								NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
								navig.removeParameter("show");
								navig.removeParameter("contactid");
								navig.removeParameter("ownerid");
								navig.removeParameter("ownertypeid");
								NavigationHistoryManager.getInstance().go(navig);
							}
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o contacto."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
							view.setEditable(true);
						}
					});
				}

				else
				{
					broker.updateContact(temp, new ResponseHandler<Contact>() {

						@Override
						public void onResponse(Contact response) {
							if(ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)){
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Contacto gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
								ContactViewPresenter.this.setContact(response);
								contactId = response.id;
								view.setSaveMode(false);
								view.setEditable(false);
								fireAction(Action.CONTACT_CREATED);
							}
							else{
								NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
								navig.removeParameter("show");
								navig.removeParameter("contactid");
								navig.removeParameter("ownerid");
								navig.removeParameter("ownertypeid");
								NavigationHistoryManager.getInstance().go(navig);
							}
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o contacto."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
							view.setEditable(true);
						}
					});
				}

			}


		});

		view.getSubContactList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if (event.getSelected().size() > 0)
					fireAction(Action.CHILD_SELECTED);
			}
		});

		bound = true;
	}


	protected void createChildContact(Contact temp) {

		if(temp.id == null){
			temp.ownerId = ownerId;
			temp.ownerTypeId = ownerTypeId;

			broker.addContact(temp, new ResponseHandler<Contact>() {

				@Override
				public void onResponse(Contact response) {
					ContactViewPresenter.this.setContact(response);
					fireAction(Action.CREATE_CHILD_CONTACT);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o sub-contacto."), TYPE.ALERT_NOTIFICATION));
				}

			});
		}else{
			fireAction(Action.CREATE_CHILD_CONTACT);
		}

	}


	public void addNewDetail() {

		ContactEntry temp = view.initializeContactEntry();
		temp.setHeight("40px");
		view.getContactInfoList().remove(view.getContactInfoList().size()-1);
		temp.setEditable(true);
		view.addContactInfo(temp.getValue());
		view.addContactInfo(null);

	}

	public ContactView getView() {
		return (ContactView)view;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {


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
	public void setContacts(String ownerId, java.util.List<Contact> contacts) {


		return;
	}

	@Override
	public void removeContact(String ownerId, String contactId) {

		return;
	}

	@Override
	public void addContact(String ownerId, Contact contact) {

		return;
	}

	@Override
	public void updateContact(String ownerId, Contact contact) {

		return;
	} 

	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action, this));
		}
	}

	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	public HasParameters getParameters() {
		return this.parameterHolder;
	}

}
