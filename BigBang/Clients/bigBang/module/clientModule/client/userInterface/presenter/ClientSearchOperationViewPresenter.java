package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
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
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

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
		HasValueSelectables<QuoteRequestStub> getQuoteRequestList();
		HasValueSelectables<CasualtyStub> getCasualtyList();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewClient(Client client);
		void setForCreation(boolean forCreation);

		Widget asWidget();
	}

	private Display view;
	private ClientProcessBroker clientBroker;
	private boolean bound = false;

	public ClientSearchOperationViewPresenter(View view){
		this.clientBroker = (ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT);
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
					item.setParameter("policyid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_QUOTE_REQUEST:
					item.pushIntoStackParameter("display", "createquoterequest");
					item.setParameter("quoterequestid", "new");
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
					item.setParameter("show", "managertransfer");
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
					navItem.setStackParameter("display");
					navItem.pushIntoStackParameter("display", "search");
					navItem.setParameter("policyid", itemId);
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
		view.getQuoteRequestList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<QuoteRequestStub> selected = (ValueSelectable<QuoteRequestStub>) event.getFirstSelected();
				QuoteRequestStub item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					goToQuoteRequest(itemId);
				}
			}
		});
		view.getCasualtyList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<CasualtyStub> selected = (ValueSelectable<CasualtyStub>) event.getFirstSelected();
				CasualtyStub item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					goToCasualty(itemId);
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
		
		view.allowCreate(PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), BigBangConstants.OperationIds.GeneralSystemProcess.CREATE_CLIENT));
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
			view.setForCreation(true);

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
			view.setForCreation(false);
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

				view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.UPDATE_CLIENT));
				view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.DELETE_CLIENT));
				view.allowRequestInfoOrDocument(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_INFO_REQUEST));
				view.allowManagerTransfer(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER));
				view.allowClientMerge(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.MERGE_CLIENT));
				view.allowCreatePolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_POLICY));
				view.allowCreateRiskAnalysis(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_RISK_ANALISYS));
				view.allowCreateQuoteRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_QUOTE_REQUEST));
				view.allowcreateCasualty(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_CASUALTY));

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

	private void goToSubProcess(BigBangProcess process){
		String type = process.dataTypeId;

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.MANAGER_TRANSFER)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewmanagertransfer");
			item.setParameter("transferid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewinforequest");
			item.setParameter("requestid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	private void goToQuoteRequest(String requestId){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("section", "quoterequest");
		navItem.setStackParameter("display");
		navItem.pushIntoStackParameter("display", "search");
		navItem.setParameter("quoterequestid", requestId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	private void goToCasualty(String casualtyId){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("section", "casualty");
		navItem.setStackParameter("display");
		navItem.pushIntoStackParameter("display", "search");
		navItem.setParameter("casualtyid", casualtyId);
		NavigationHistoryManager.getInstance().go(navItem);
	}
}
