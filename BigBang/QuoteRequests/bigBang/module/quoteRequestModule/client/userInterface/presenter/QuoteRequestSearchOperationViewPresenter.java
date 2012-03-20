package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSearchOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE
	}

	public interface Display {
		HasValueSelectables<QuoteRequestStub> getList();
		HasEditableValue<QuoteRequest> getForm();

		//Permissions
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);

		//Children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<InsuredObjectStub> getObjectsList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();

		void setSaveModeEnabled(boolean enabled);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected QuoteRequestBroker broker;
	protected Display view;
	protected boolean bound = false;

	public QuoteRequestSearchOperationViewPresenter(View view) {
		setView(view);
		broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
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
		String quoteRequestId = parameterHolder.getParameter("quoterequestid");

		clearView();

		if(quoteRequestId == null || quoteRequestId.isEmpty()) {
			showQuoteRequest(quoteRequestId);
		}
	}

	public void bind() {
		if(bound)
			return;

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				QuoteRequestStub quoteRequest = (QuoteRequestStub) (selected == null ? null : selected.getValue());

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(quoteRequest == null) {
					item.removeParameter("quoterequestid");
				}else{
					item.setParameter("quoterequestid", quoteRequest.id);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<QuoteRequestSearchOperationViewPresenter.Action>() {

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
				default:
					break;
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				if(selected != null) {				
					if(event.getSource() == view.getObjectsList()){
						InsuredObject object = (InsuredObject) selected.getValue();
						showInsuredObject(object.id);
					}else if(event.getSource() == view.getSubProcessesList()) {
						BigBangProcess process = (BigBangProcess) selected.getValue();
						showSubProcess(process);
					}else if(event.getSource() == view.getHistoryList()) {
						HistoryItemStub historyItem = (HistoryItemStub) selected.getValue();
						showHistory(historyItem.id);
					}
				}
			}
		};
		view.getObjectsList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getSubProcessesList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
		view.getList().clearSelection();
	}

	protected void showQuoteRequest(String quoteRequestId) {
		for(ValueSelectable<QuoteRequestStub> entry : view.getList().getAll()) {
			QuoteRequestStub quoteRequest = entry.getValue();
			if(quoteRequest.id.equalsIgnoreCase(quoteRequestId) && !entry.isSelected()){
				entry.setSelected(true, true);
				return;
			}
		}
		broker.getQuoteRequest(quoteRequestId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				view.clearAllowedPermissions();

				//TODO PERMISSIONS
				view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST));
				view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.DELETE_QUOTE_REQUEST));

				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetQuoteRequestFailed();
			}
		});
	}

	protected void onEdit() {
		view.setSaveModeEnabled(true);
		view.getForm().setReadOnly(false);
	}

	protected void onSave() {
		//TODO
	}

	protected void onCancel(){
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onDelete(){
		//TODO
	}

	protected void showInsuredObject(String objectId) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "viewinsuredobject");
		navItem.setParameter("objectid", objectId);
		NavigationHistoryManager.getInstance().go(navItem);
	}
	
	protected void showSubProcess(BigBangProcess process){
		String type = process.dataTypeId;
		
		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
			showNegotiation(process.dataId);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)) {
			showInfoRequest(process.dataId);
		}
	}
	
	protected void showNegotiation(String negotiationId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("negotiation", negotiationId);
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void showInfoRequest(String requestId) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("viewinforequest", requestId);
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void showHistory(String historyItemId){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", historyItemId);
		NavigationHistoryManager.getInstance().go(navItem);
	}
	
	protected void onGetQuoteRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Apresentar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("quoterequestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Consulta de Mercado foi Guardada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

}
