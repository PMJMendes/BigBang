package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.HistoryItemStub;
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
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel;
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel.Entry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExpenseSearchOperationViewPresenter implements ViewPresenter {

	public enum Action{
		EDIT, SAVE, CANCEL, DELETE, INFO_FROM_INSURER,
		INFO_OR_DOCUMENT_REQUEST,
		PARTICIPATE_TO_INSURER, NOTIFY_CLIENT, RETURN_TO_CLIENT, CLOSE_PROCESS, RECEIVE_ACCEPTANCE, RECEIVE_REJECTION
	}

	public interface Display {
		Widget asWidget();
		HasValueSelectables<ExpenseStub> getList();
		HasEditableValue<Expense> getForm();
		boolean isFormValid();
		void clearAllowedPermissions();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		void clear();
		void lockOptions();
		void allowReceiveAcceptance(boolean allow);
		void allowInfoFromInsurer(boolean allow);
		void allowInfoOrDocumentRequest(boolean allow);
		void allowNotifyClient(boolean allow);
		void allowParticipateToInsurer(boolean allow);
		void allowReturnToClient(boolean allow);
		void allowReceiveRejection(boolean hasPermission);
		void setEditMode();
		void addEntryToList(Entry entry);
	}

	protected boolean bound = false;
	protected Display view;
	protected ExpenseDataBroker expenseBroker;
	private String expenseId;

	public ExpenseSearchOperationViewPresenter(View view){
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
		expenseBroker = (ExpenseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE);
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
		expenseId = parameterHolder.getParameter("expenseid");

		if(expenseId == null || expenseId.isEmpty()){
			clearView();
		}else{
			showExpense(expenseId);
		}

	}

	private void setup(){
		this.view.getContactsList().clearSelection();
		this.view.getDocumentsList().clearSelection();
		this.view.getSubProcessesList().clearSelection();
		this.view.getHistoryList().clearSelection();
	}

	private void clearView() {
		view.clearAllowedPermissions();
		view.clear();
		view.getForm().setValue(null);
		view.getList().clearSelection();
		view.setSaveModeEnabled(false);
		view.getForm().setReadOnly(true);
	}

	private void showExpense(String expenseId) {

		for(ValueSelectable<?> selectable : view.getList().getAll()){
			ExpenseStub stub = (ExpenseStub) selectable.getValue();
			if(stub.id.equalsIgnoreCase(expenseId)){
				selectable.setSelected(true, false);
			}else if(selectable.isSelected()){
				selectable.setSelected(false, false);
			}
		}

		expenseBroker.getExpense(expenseId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				view.clearAllowedPermissions();
				view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.DELETE_EXPENSE));
				view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.UPDATE_EXPENSE));
				view.allowInfoFromInsurer(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.CREATE_EXTERNAL_REQUEST));
				view.allowNotifyClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.NOTIFY_CLIENT));
				view.allowParticipateToInsurer(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.SEND_NOTIFICATION));
				view.allowReceiveAcceptance(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.RECEIVE_ACCEPTANCE));
				view.allowReceiveRejection(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.RECEIVE_RETURN));
				view.allowReturnToClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.RETURN_TO_CLIENT));
				view.allowInfoOrDocumentRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ExpenseProcess.CREATE_INFO_REQUEST));
				view.setSaveModeEnabled(false);
				view.getForm().setReadOnly(true);
				view.getForm().setValue(response);
				ensureListedAndSelected(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar a Despesa de Saúde"), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("expenseid");
				NavigationHistoryManager.getInstance().go(item);
			}
		});


	}

	protected void ensureListedAndSelected(Expense response) {
		boolean found = false;
		for(ValueSelectable<ExpenseStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equals(response.id)){
				found = true;
				stub.setSelected(true, false);
			}
			else{
				stub.setSelected(false, false);
			}
		}
		
		if(!found){
			ExpenseSearchPanel.Entry entry = new ExpenseSearchPanel.Entry(response);
			view.addEntryToList(entry);
		}
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ExpenseStub> selected = (ValueSelectable<ExpenseStub>) event.getFirstSelected();
				ExpenseStub stub = selected == null ? null : selected.getValue();
				view.setSaveModeEnabled(false);

				if(stub == null || stub.id == null){
					NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
					navigationItem.removeParameter("expenseid");
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else{
					NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
					navigationItem.setParameter("expenseid", stub.id);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<ExpenseSearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CLOSE_PROCESS:
					onCloseProcess();
					break;
				case DELETE:
					onDelete();
					break;
				case EDIT:
					onEdit();
					break;
				case INFO_FROM_INSURER:
					onInfoFromInsurer();
					break;
				case INFO_OR_DOCUMENT_REQUEST:
					onInfoOrDocumentRequest();
					break;
				case NOTIFY_CLIENT:
					onNotifyClient();
					break;
				case PARTICIPATE_TO_INSURER:
					onParticipateToInsurer();
					break;
				case RETURN_TO_CLIENT:
					onReturnToClient();
					break;
				case SAVE:
					onSave();
					break;
				case RECEIVE_ACCEPTANCE:
					onReceiveAcceptance();
					break;
				case RECEIVE_REJECTION:
					onReceiveRejection();
					break;
				}

			}
		});


		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				Contact selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<Contact>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showContact(selectedValue);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				Document selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<Document>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showDocument(selectedValue);
				}
			}
		});
		view.getSubProcessesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				BigBangProcess selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<BigBangProcess>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showSubProcess(selectedValue);
				}
			}
		});
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				HistoryItemStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<HistoryItemStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showHistory(selectedValue);
				}
			}
		});

		bound = true;
	}

	protected void onReceiveRejection() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "receivereturn");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onReceiveAcceptance() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "receiveacceptance");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCancel() {
		view.getForm().revert();
		view.getForm().setReadOnly(true);
		view.setSaveModeEnabled(false);
	}

	protected void onSave() {
		if(view.getForm().validate()) {
			Expense expense = view.getForm().getInfo();

			this.expenseBroker.updateExpense(expense, new ResponseHandler<Expense>() {

				@Override
				public void onResponse(Expense response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Despesa de Saúde foi guardada com sucesso"), TYPE.TRAY_NOTIFICATION));
					NavigationHistoryManager.getInstance().reload();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a Despesa de Saúde"), TYPE.ALERT_NOTIFICATION));
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onReturnToClient() {
		expenseBroker.returnToClient(expenseId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Devolvido ao cliente com sucesso"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível devolver ao Cliente"), TYPE.ALERT_NOTIFICATION));

			}
		});
	}

	protected void onParticipateToInsurer() {
		expenseBroker.sendNotification(expenseId, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Participação à Seguradora enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar a Participação à Seguradora"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void onNotifyClient() {
		expenseBroker.notifyClient(expenseId, new ResponseHandler<Expense>(){

			@Override
			public void onResponse(Expense response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cliente notificado com sucesso"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível notificar o Cliente"), TYPE.ALERT_NOTIFICATION));
			}

		});

	}

	protected void onInfoOrDocumentRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "inforequest");
		item.setParameter("ownerid", view.getForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onInfoFromInsurer() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "externalrequest");
		item.setParameter("externalrequestid", "new");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onEdit() {
		view.getForm().setReadOnly(false);
		view.setSaveModeEnabled(true);
		view.setEditMode();
	}

	protected void onDelete() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "deleteexpense");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCloseProcess() {
		// TODO Auto-generated method stub

	}

	private void showContact(final Contact contact) {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "contactmanagement");
		item.setParameter("ownerid", contact.ownerId);
		item.setParameter("ownertypeid", contact.ownerTypeId);
		item.setParameter("contactid", contact.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showDocument(final Document document){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "documentmanagement");
		item.setParameter("ownerid", document.ownerId);
		item.setParameter("ownertypeid", document.ownerTypeId);
		item.setParameter("documentid", document.id);
		NavigationHistoryManager.getInstance().go(item);
	}


	private void showSubProcess(final BigBangProcess process){
		String type = process.dataTypeId;

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewinforequest");
			item.setParameter("requestid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
		else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.EXTERNAL_INFO_REQUEST)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewexternalrequest");
			item.setParameter("externalrequestid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	private void showHistory(final HistoryItemStub historyItem) {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", view.getForm().getValue().id);
		item.setParameter("historyitemid", historyItem.id);
		NavigationHistoryManager.getInstance().go(item);

	}

}
