package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
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
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanelListEntry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CasualtySearchOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE,
		CLOSE,
		CREATE_SUB_CASUALTY,
		TRANSFER_MANAGER, SEND_MESSAGE, RECEIVE_MESSAGE, SUB_CASUALTY_REOPEN, REOPEN
	}

	public interface Display {
		HasValueSelectables<CasualtyStub> getList();
		HasEditableValue<Casualty> getForm();

		//Permissions
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowClose(boolean allow);
		void allowCreateSubCasualty(boolean allow);
		void allowTransferManager(boolean allow);

		//Children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<SubCasualtyStub> getSubCasualtyList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();

		void setSaveModeEnabled(boolean enabled);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void addEntryToList(CasualtySearchPanelListEntry entry);
		void allowSendMessage(boolean b);
		void allowReceiveMessage(boolean b);
		void allowReopen(boolean b);
		void allowReopenSubCasualty(boolean hasPermission);
	}

	protected CasualtyDataBroker broker;
	protected Display view;
	protected boolean bound = false;

	public CasualtySearchOperationViewPresenter(View view) {
		setView(view);
		broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String casualtyId = parameterHolder.getParameter("casualtyid");

		if(casualtyId == null || casualtyId.isEmpty()) {
			clearView();
		}else{
			showCasualty(casualtyId);
		}
	}

	public void bind() {
		if(bound)
			return;

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				CasualtyStub quoteRequest = (CasualtyStub) (selected == null ? null : selected.getValue());

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(quoteRequest == null) {
					item.removeParameter("casualtyid");
				}else{
					item.setParameter("casualtyid", quoteRequest.id);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<CasualtySearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case EDIT:
					onEdit();
					break;
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				case CLOSE:
					onClose();
					break;
				case CREATE_SUB_CASUALTY:
					onCreateSubCasualty();
					break;
				case TRANSFER_MANAGER:
					onTransferManager();
					break;
				case SEND_MESSAGE:
					onSendMessage();
					break;
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case REOPEN:
					onReopen();
					break;
				case SUB_CASUALTY_REOPEN:
					onSubCasualtyReopen();
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				if(selected != null) {				
					if(event.getSource() == view.getSubCasualtyList()) {
						SubCasualtyStub process = (SubCasualtyStub) selected.getValue();
						showSubCasualty(process.id);
					} else if(event.getSource() == view.getSubProcessesList()) {
						BigBangProcess process = (BigBangProcess) selected.getValue();
						showSubProcess(process);
					}else if(event.getSource() == view.getHistoryList()) {
						HistoryItemStub historyItem = (HistoryItemStub) selected.getValue();
						showHistory(historyItem.id);
					}else if(event.getSource() == view.getContactsList()){
						Contact contact = (Contact) selected.getValue();
						showContact(contact.id);
					}else if(event.getSource() == view.getDocumentsList()){
						Document doc = (Document) selected.getValue();
						showDocument(doc.id);
					}
				}
			}
		};
		view.getDocumentsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getContactsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getSubCasualtyList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getSubProcessesList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;
	}

	protected void onSubCasualtyReopen() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "subcasualtyreopen");
		NavigationHistoryManager.getInstance().go(navItem);				
	}

	protected void onReopen() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "reopen");
		NavigationHistoryManager.getInstance().go(navItem);		
	}

	protected void onReceiveMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "receivemessage");
		item.setParameter("ownerid", view.getForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);		
	}

	protected void showDocument(String id) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "documentmanagement");
		navItem.setParameter("ownerid", navItem.getParameter("casualtyid"));
		navItem.setParameter("documentid", id);
		navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CASUALTY);
		NavigationHistoryManager.getInstance().go(navItem);

	}

	protected void showContact(String id) {		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
	navItem.setParameter("show", "contactmanagement");
	navItem.setParameter("ownerid", navItem.getParameter("casualtyid"));
	navItem.setParameter("contactid", id);
	navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CASUALTY);
	NavigationHistoryManager.getInstance().go(navItem);

	}

	protected void onSendMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "sendmessage");
		item.setParameter("ownerid", view.getForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void clearView(){
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.clearAllowedPermissions();
		view.setSaveModeEnabled(false);
		view.getList().clearSelection();
	}

	protected void showCasualty(String quoteRequestId) {
		for(ValueSelectable<CasualtyStub> entry : view.getList().getAll()) {
			CasualtyStub quoteRequest = entry.getValue();
			if(quoteRequest.id.equalsIgnoreCase(quoteRequestId)){
				entry.setSelected(true, false);
			}else if(entry.isSelected()){
				entry.setSelected(false, false);
			}
		}
		broker.getCasualty(quoteRequestId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				view.clearAllowedPermissions();
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);

				view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.CasualtyProcess.UPDATE_CASUALTY));
				view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.CasualtyProcess.DELETE_CASUALTY));
				view.allowClose(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.CasualtyProcess.CLOSE_CASUALTY));
				view.allowCreateSubCasualty(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.CasualtyProcess.CREATE_SUB_CASUALTY));
				view.allowTransferManager(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.CasualtyProcess.CREATE_MANAGER_TRANSFER));
				view.allowSendMessage(PermissionChecker.hasPermission(response,BigBangConstants.OperationIds.CasualtyProcess.CONVERSATION));
				view.allowReceiveMessage(PermissionChecker.hasPermission(response,BigBangConstants.OperationIds.CasualtyProcess.CONVERSATION));
				view.allowReopen(PermissionChecker.hasPermission(response,BigBangConstants.OperationIds.CasualtyProcess.REOPEN));
				view.allowReopenSubCasualty(PermissionChecker.hasPermission(response,BigBangConstants.OperationIds.CasualtyProcess.REOPEN_SUB_CASUALTY));
				view.getForm().setValue(response);
				ensureListedAndSelected(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetCasualtyFailed();
			}
		});
	}

	protected void ensureListedAndSelected(Casualty response) {
		boolean found = false;
		for(ValueSelectable<CasualtyStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equals(response.id)){
				found = true;
				stub.setSelected(true, false);
			}
			else{
				stub.setSelected(false, false);
			}
		}
		
		if(!found){
			CasualtySearchPanelListEntry entry = new CasualtySearchPanelListEntry(response);
			view.addEntryToList(entry);
		}
	}

	protected void onEdit() {
		view.getForm().setReadOnly(false);
		view.setSaveModeEnabled(true);
	}

	protected void onSave() {
		if(view.getForm().validate()) {
			broker.updateCasualty(view.getForm().getInfo(), new ResponseHandler<Casualty>() {

				@Override
				public void onResponse(Casualty response) {
					onSaveSuccess();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onSaveFailed();
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onCancel(){
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onDelete(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "delete");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onClose(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "close");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onCreateSubCasualty(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "subcasualty");
		navItem.setParameter("ownerid", view.getForm().getValue().clientId);
		navItem.setParameter("subcasualtyid", "new");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onTransferManager(){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "managertransfer");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void showSubCasualty(String id){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "subcasualty");
		navItem.setParameter("ownerid", view.getForm().getValue().clientId);
		navItem.setParameter("subcasualtyid", id);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void showSubProcess(BigBangProcess process){
		String type = process.dataTypeId;
		
		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "conversation");
			item.setParameter("conversationid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	protected void showHistory(String historyItemId){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", historyItemId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onGetCasualtyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Apresentar o Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("casualtyid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Sinistro"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Sinistro foi Guardado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

}
