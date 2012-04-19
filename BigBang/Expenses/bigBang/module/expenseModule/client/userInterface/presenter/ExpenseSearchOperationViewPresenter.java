package bigBang.module.expenseModule.client.userInterface.presenter;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExpenseSearchOperationViewPresenter implements ViewPresenter {

	public enum Action{
		EDIT, SAVE, CANCEL, DELETE, INFO_FROM_INSURER,
		INFO_OR_DOCUMENT_REQUEST, VALIDATE, 
		PARTICIPATE_TO_INSURER, NOTIFY_CLIENT, RETURN_TO_CLIENT, CLOSE_PROCESS, RECEIVE_RESPONSE
	}

	public interface Display {
		Widget asWidget();
		HasValueSelectables<?> getList();
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
		void allowReceiveResponse(boolean allow);
		void allowInfoFromInsurer(boolean allow);
		void allowInfoOrDocumentRequest(boolean allow);
		void allowNotifyClient(boolean allow);
		void allowParticipateToInsurer(boolean allow);
		void allowReturnToClient(boolean allow);
		void allowValidate(boolean allow);
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
		expenseId = parameterHolder.getParameter("expenseId");

		if(expenseId == null || expenseId.isEmpty()){
			clearView();
		}else{
			showExpense(expenseId);
		}
		
	}

	private void clearView() {
		//TODO view.clearAllowedPermissions();
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
		
		
		//TODO BROKER GET CENAS
		
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub

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
				case VALIDATE:
					onValidate();
					break;
				case RECEIVE_RESPONSE:
					onReceiveResponse();
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

	protected void onReceiveResponse() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "receiveresponse");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCancel() {
		view.getForm().revert();
		view.getForm().setReadOnly(true);
		view.setSaveModeEnabled(false);
	}

	protected void onValidate() {
		// TODO Auto-generated method stub

	}

	protected void onSave() {
		// TODO Auto-generated method stub

	}

	protected void onReturnToClient() {
		// TODO Auto-generated method stub

	}

	protected void onParticipateToInsurer() {
		// TODO Auto-generated method stub

	}

	protected void onNotifyClient() {
		// TODO Auto-generated method stub

	}

	protected void onInfoOrDocumentRequest() {
		// TODO Auto-generated method stub

	}

	protected void onInfoFromInsurer() {
		// TODO Auto-generated method stub

	}

	protected void onEdit() {
		view.getForm().setReadOnly(false);
		view.setSaveModeEnabled(true);
	}

	protected void onDelete() {
		// TODO Auto-generated method stub

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
	//	String type = process.dataTypeId;
		//NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		//TODO
	}

	private void showHistory(final HistoryItemStub historyItem) {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", view.getForm().getValue().id);
		item.setParameter("historyitemid", historyItem.id);
		NavigationHistoryManager.getInstance().go(item);

	}

}
