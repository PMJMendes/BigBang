package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Session;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ClientSearchOperationViewPresenter implements ViewPresenter {

	public static enum Action{
		NEW,
		REFRESH,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE,
		CREATE_POLICY,
		CREATE_RISK_ANALISYS,
		CREATE_QUOTE_REQUEST,
		CREATE_CASUALTY,
		MERGE_WITH_CLIENT,
		TRANSFER_MANAGER,
		REQUIRE_INFO_DOCUMENT
	}

	public interface Display {
		//List
		HasValueSelectables<ClientStub> getList();
		void removeFromList(ValueSelectable<ClientStub> selectable);
		void selectClient(String clientId);

		//Form
		HasEditableValue<Client> getForm();
		void scrollFormToTop();
		boolean isFormValid();

		//Permissions
		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowRequestInfoOrDocument(boolean allow);
		void allowManagerTransfer(boolean allow);
		void allowClientMerge(boolean allow);
		void allowCreatePolicy(boolean allow);
		void allowCreateRiskAnalysis(boolean allow);
		void allowCreateQuoteRequest(boolean allow);
		void allowcreateCasualty(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		//Children Lists
		HasValueSelectables<HistoryItemStub> getHistoryList();
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<InsurancePolicyStub> getPolicyList();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewClient(Client client);

		Widget asWidget();
	}

	private Display view;
	private ClientProcessBroker clientBroker;
	private ClientProcessDataBrokerClient clientBrokerClient;
	private boolean bound = false;

	public ClientSearchOperationViewPresenter(View view){
		this.clientBroker = (ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT);
		this.clientBrokerClient = initializeClientBrokerClient();
		this.clientBroker.registerClient(this.clientBrokerClient);
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override 
	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		setup();
		
		String id = parameterHolder.getParameter("clientid");
		id = id == null ? new String() : id;

		if(inClientCreation()){
			clearNewClient();
		}

		if(id.isEmpty()){
			clearView();
		}else if(id.equalsIgnoreCase("new")) {
			setupNewClient();
		}else{
			showClient(id);
		}
	}

	private void bind() {
		if(bound) {return;}

		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selected = (ValueSelectable<ClientStub>) event.getFirstSelected();
				ClientStub client = selected == null ? null : selected.getValue();
				String clientId = client == null ? new String() : client.id;
				clientId = clientId == null ? new String() : clientId; 

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(clientId.isEmpty()){
					item.removeParameter("clientid");
				}else{
					item.setParameter("clientid", clientId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<ClientSearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				
				switch(action.getAction()){
				case NEW:
					item.setParameter("clientid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case CANCEL_EDIT:
					if(inClientCreation()){
						clearNewClient();
					}else{
						NavigationHistoryManager.getInstance().reload();
					}
					break;
				case SAVE:
					Client info = view.getForm().getInfo();
					view.getForm().setReadOnly(true);
					if(inClientCreation()){
						createClient(info);
					}else{
						saveClient(info);
					}
					break;
				case DELETE:
					if(inClientCreation()){
						clearNewClient();
					}else{
						item.setParameter("show", "delete");
						NavigationHistoryManager.getInstance().go(item);
					}
					break;
				case CREATE_CASUALTY:
					item.pushIntoStackParameter("display", "createcasualty");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_POLICY:
					item.pushIntoStackParameter("display", "createpolicy");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_QUOTE_REQUEST:
					item.pushIntoStackParameter("display", "createquoterequest");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_RISK_ANALISYS:
					item.pushIntoStackParameter("display", "createriskanalisys");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case MERGE_WITH_CLIENT:
					item.pushIntoStackParameter("display", "merge");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case REQUIRE_INFO_DOCUMENT:
					item.pushIntoStackParameter("display", "inforequest");
					item.setParameter("ownerid", view.getForm().getValue().id);
					NavigationHistoryManager.getInstance().go(item);
					break;
				case TRANSFER_MANAGER:
					item.pushIntoStackParameter("display", "managertransfer");
					NavigationHistoryManager.getInstance().go(item);
					break;
				}
			}
		});

		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItemStub> selected = (ValueSelectable<HistoryItemStub>) event.getFirstSelected();
				HistoryItemStub item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.pushIntoStackParameter("display", "clienthistory");
					navItem.setParameter("historyownerid", view.getForm().getValue().id);
					navItem.setParameter("historyItemId", itemId);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Contact> selected = (ValueSelectable<Contact>) event.getFirstSelected();
				Contact item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "contactmanagement");
					navItem.setParameter("ownerid", view.getForm().getValue().id);
					navItem.setParameter("contactid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Document> selected = (ValueSelectable<Document>) event.getFirstSelected();
				Document item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "documentmanagement");
					navItem.setParameter("ownerid", item.ownerId);
					navItem.setParameter("documentid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getPolicyList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> selected = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				InsurancePolicyStub item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("section", "insurancepolicy");
					navItem.popFromStackParameter("display");
					navItem.setParameter("clientid", itemId);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getSubProcessesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<BigBangProcess> selected = (ValueSelectable<BigBangProcess>) event.getFirstSelected();
				BigBangProcess item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.dataId;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					goToSubProcess(item);
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		this.bound = true;
	}

	private void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	private void setup(){
		this.view.getContactsList().clearSelection();
		this.view.getDocumentsList().clearSelection();
		this.view.getPolicyList().clearSelection();
		this.view.getSubProcessesList().clearSelection();
		this.view.getHistoryList().clearSelection();
	}
	
	private void setupNewClient(){
		clearNewClient();
		boolean hasPermission = true; //TODO check permission
		if(hasPermission){
			Client client = new Client();
			client.id = new String("new");
			client.name = new String("Novo Cliente");
			client.clientNumber = new String(" - ");
			client.managerId = Session.getUserId();
			view.getList().clearSelection();
			view.prepareNewClient(client);

			//permissions
			view.clearAllowedPermissions();
			view.allowDelete(true);
			view.allowEdit(true);
			view.setSaveModeEnabled(true);

			view.getForm().setValue(client);
			view.getForm().setReadOnly(false);
		}else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearNewClient(){
		if(inClientCreation()){
			for(ValueSelectable<ClientStub> selected : view.getList().getAll()){
				ClientStub client = selected.getValue();
				if(client == null || client.id.equalsIgnoreCase("new")){
					view.removeFromList(selected);
					break;
				}
			}
			view.clearAllowedPermissions();
			view.getForm().setValue(null);
			view.getForm().setReadOnly(true);
			view.getList().clearSelection();
		}
	}

	private boolean inClientCreation(){
		for(ValueSelectable<ClientStub> selected : view.getList().getAll()){
			ClientStub client = selected.getValue();
			if(client == null || client.id.equalsIgnoreCase("new")){
				return true;
			}
		}
		return false;
	}

	private void showClient(String id){
		for(ValueSelectable<ClientStub> entry : view.getList().getAll()){
			ClientStub listClient = entry.getValue();
			if(listClient.id.equalsIgnoreCase(id) && !entry.isSelected()){
				view.selectClient(id);
				break;
			}
		}		

		this.clientBroker.getClient(id, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.clearAllowedPermissions();

				boolean hasPermissions = true; //TODO IMPORTANT FJVC
				view.allowEdit(hasPermissions);
				view.allowDelete(hasPermissions);
				view.allowCreate(true);
				view.allowEdit(true);
				view.allowDelete(true);
				view.allowRequestInfoOrDocument(true);
				view.allowManagerTransfer(true);
				view.allowClientMerge(true);
				view.allowCreatePolicy(true);
				view.allowCreateRiskAnalysis(true);
				view.allowCreateQuoteRequest(true);
				view.allowcreateCasualty(true);

				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientFailed();
			}
		});
	}

	private void createClient(Client client) {
		clientBroker.addClient(client, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("clientid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cliente criado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateClientFailed();
			}
		});
	}

	private void saveClient(Client client) {
		clientBroker.updateClient(client, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("clientid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cliente guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveClientFailed();
			}
		});
	}

	private void onGetClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter Cliente seleccionado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("clientid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar o Cliente"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onSaveClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações ao Cliente"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	//TODO for when deleted
	private ClientProcessDataBrokerClient initializeClientBrokerClient(){
		return new ClientProcessDataBrokerClient() {

			private int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				this.version = number;
			}

			@Override
			public int getDataVersion(String dataElementId) {
				return this.version;
			}

			@Override
			public void updateClient(Client client) {
				Client currentClient = ClientSearchOperationViewPresenter.this.view.getForm().getValue();
				String clientId = currentClient == null ? null : currentClient.id;

				if(clientId != null && clientId.equalsIgnoreCase(client.id)){
					ClientSearchOperationViewPresenter.this.view.getForm().setValue(client);
					ClientSearchOperationViewPresenter.this.view.getForm().setReadOnly(true);
				}
			}

			@Override
			public void removeClient(String clientId) {
				Client currentClient = ClientSearchOperationViewPresenter.this.view.getForm().getValue();
				String currentClientId = currentClient == null ? null : currentClient.id;

				if(currentClientId != null && currentClientId.equalsIgnoreCase(clientId)){
					ClientSearchOperationViewPresenter.this.view.getForm().setValue(null);
					ClientSearchOperationViewPresenter.this.view.getForm().setReadOnly(true);
				}
			}

			@Override
			public void addClient(Client client) {
				// TODO Auto-generated method stub
			}
		};
	}
	
	private void goToSubProcess(BigBangProcess process){
		String type = process.dataTypeId;
		
		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.MANAGER_TRANSFER)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewmanagertransfer");
			item.setParameter("transferid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "inforequest");
			item.setParameter("requestid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}
	
}
