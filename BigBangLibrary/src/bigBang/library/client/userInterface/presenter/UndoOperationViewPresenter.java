package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class UndoOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		REVERT_OPERATION,
		NAVIGATE_TO_AUXILIARY_PROCESS
	}

	public interface Display {
		//LIST
		HasValueSelectables<HistoryItemStub> getUndoItemList();
		void setObjectId(String objectId);
		void selectItem(String id);

		//FORM
		HasValue<HistoryItem> getForm();

		//PERMISSIONS
		void allowUndo(boolean allow);
		void allowNavigateToAuxiliaryProcess(boolean allow);

		void confirmUndo(ResponseHandler<Boolean> handler);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();
	}

	private Display view;
	protected HistoryBroker historyBroker;
	private boolean bound = false;

	public UndoOperationViewPresenter(Display view) {
		this.historyBroker = (HistoryBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.HISTORY);
		setView((View) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		if(!bound)
			bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String objectId = parameterHolder.getParameter("id");
		objectId = objectId == null ? new String() : objectId;
		String itemId = parameterHolder.getParameter("historyitemid");
		itemId = itemId == null ? new String() : itemId;

		if(objectId.isEmpty()){
			clearView();
		}else{
			showHistory(objectId, itemId);
		}
	}

	private void bind() {
		if(bound)
			return;
		view.registerActionHandler(new ActionInvokedEventHandler<UndoOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case REVERT_OPERATION:
					onRevertOperation();
					break;
				case NAVIGATE_TO_AUXILIARY_PROCESS:
					onNavigateToAuxiliaryProcess();
					break;
				}
			}
		});
		view.getUndoItemList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItemStub> selectedItem = (ValueSelectable<HistoryItemStub>) event.getFirstSelected();
				HistoryItemStub item = selectedItem == null ? null : selectedItem.getValue();
				String itemId = item == null ? null : item.id;

				NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
				if(itemId == null){
					navigationItem.removeParameter("historyitemid");
				}else{
					navigationItem.setParameter("historyItemid", itemId);
				}
				NavigationHistoryManager.getInstance().go(navigationItem);
			}
		});

		//APPLICATION-WIDE EVENTS

		bound = true;
	}

	private void clearView(){
		view.getForm().setValue(null);
		view.setObjectId(null);
		view.allowUndo(false);
		view.allowNavigateToAuxiliaryProcess(false);
	}

	private void showHistory(String objectId, String itemId){
		view.setObjectId(objectId);
		this.historyBroker.requireDataRefresh(objectId);
		historyBroker.getItem(itemId, objectId, new ResponseHandler<HistoryItem>() {

			@Override
			public void onResponse(HistoryItem response) {
				view.getForm().setValue(response);
				view.allowNavigateToAuxiliaryProcess(response.otherObjectId != null && response.otherObjectTypeId != null);
				view.allowUndo(response.canUndo);
				view.selectItem(response.id);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onShowHistoryFailed();
			}
		});
	}

	private void onRevertOperation(){
		view.confirmUndo(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					historyBroker.undo(view.getForm().getValue().id, new ResponseHandler<HistoryItem>() {

						@Override
						public void onResponse(HistoryItem response) {
							onRevertOperationSuccess();
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.removeParameter("historyItemId");
							NavigationHistoryManager.getInstance().go(item);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onRevertOperationFailed();
						}
					});
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onRevertOperationFailed();
			}
		});
	}

	private void onNavigateToAuxiliaryProcess(){
		HistoryItem historyItem = view.getForm().getValue();
		String auxObjectType = historyItem.otherObjectTypeId;
		String auxObjectId = historyItem.otherObjectId;

		if(auxObjectType == null || auxObjectId == null){
			onNavigateToAuxiliaryProcess();
		}else{
			String section = null;
			if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
				section = new String("client");
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
				section = new String("insurancepolicy");
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
				section = new String("receipt");
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
				section = new String("casualty");
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)){
				section = new String("quoterequest");
			}if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.COMPLAINT)){
				section = new String("complaint");
			}if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
				section = new String("expense");
			}if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.RISK_ANALISYS)){
				section = new String("riskanalisys");
			}else{
				onNavigateToAuxiliaryProcessFailed();
				return;
			}
			NavigationHistoryItem navigationItem = new NavigationHistoryItem();
			navigationItem.setParameter("section", section);
			navigationItem.setParameter("id", auxObjectId);
			NavigationHistoryManager.getInstance().go(navigationItem);
		}
	}
	
	private void onRevertOperationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A operação foi revertida com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onShowHistoryFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o histórico, neste momento."), TYPE.ALERT_NOTIFICATION));
	}

	private void onNavigateToAuxiliaryProcessFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onRevertOperationFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível reverter a operação"), TYPE.ALERT_NOTIFICATION));
	}

}
