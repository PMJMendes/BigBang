package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;

public class ViewClientInfoRequestViewPresenter implements ViewPresenter {

	public static enum Action {
		REPEAT_REQUEST,
		RECEIVE_RESPONSE,
		CANCEL_REQUEST
	}
	
	public static interface Display {
		HasValue<InfoOrDocumentRequest> getForm();
		HasValue<Client> getParentForm();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		void clearAllowedPermissions();
		void allowRepeat(boolean allow);
		void allowReceiveReply(boolean allow);
		void allowCancelRequest(boolean allow);
		
		Widget asWidget();
	}
	
	private boolean bound = false;
	private ClientProcessBroker clientBroker;
	private InfoOrDocumentRequestServiceAsync requestService;
	private Display view;
	
	public ViewClientInfoRequestViewPresenter(Display view) {
		setView((UIObject) view);
		clientBroker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		requestService = InfoOrDocumentRequestService.Util.getInstance();
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
		String requestId = parameterHolder.getParameter("requestid");

		clearView();
		if(requestId == null || requestId.isEmpty()) {
			onFailure();
		}else{
			showRequest(requestId);
		}
	}

	private void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ViewClientInfoRequestViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case REPEAT_REQUEST:
					onRepeatRequest();
					break;
				case RECEIVE_RESPONSE:
					onReceiveResponse();
					break;
				case CANCEL_REQUEST:
					onCancelRequest();
					break;
				}
			}
		});
		
		bound = true;
	}
	
	private void clearView(){
		view.clearAllowedPermissions();
		view.getParentForm().setValue(null);
		view.getForm().setValue(null);
	}
	
	private void showRequest(String requestId){
		this.requestService.getRequest(requestId, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				view.allowRepeat(PermissionChecker.hasPermission(result, BigBangConstants.OperationIds.InfoOrDocumentRequest.REPEAT_REQUEST));
				view.allowReceiveReply(PermissionChecker.hasPermission(result, BigBangConstants.OperationIds.InfoOrDocumentRequest.RECEIVE_REPLY));
				view.allowCancelRequest(PermissionChecker.hasPermission(result, BigBangConstants.OperationIds.InfoOrDocumentRequest.CANCEL_REQUEST));
				view.getForm().setValue(result);
				
				showParent(result.parentDataObjectId);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				ViewClientInfoRequestViewPresenter.this.onFailure();
				super.onResponseFailure(caught);
			}
		});
		
		this.view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				HistoryItemStub historyItem = selected == null ? null : (HistoryItemStub) selected.getValue();
				if(historyItem != null) {
					showHistory(view.getForm().getValue().id,historyItem.id);
				}
			}
		});
	}
	
	private void showParent(String parentId){
		clientBroker.getClient(parentId, new ResponseHandler<Client>() {
			
			@Override
			public void onResponse(Client response) {
				view.getParentForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}
	
	private void onRepeatRequest(){
		InfoOrDocumentRequest request = view.getForm().getValue();
		this.requestService.repeatRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				onRepeatSuccess();
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				onRepeatFailed();
				super.onResponseFailure(caught);
			}
			
		});
	}
	
	private void onReceiveResponse(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "repeatinforequest");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onCancelRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "cancelinforequest");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void showHistory(String processId, String historyItemId){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "clienthistory");
		item.setParameter("hisotryownerid", processId);
		item.setParameter("historyitemid", historyItemId);
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresenter o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onRepeatSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido de Informação foi Repetido com Sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
	private void onRepeatFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Repetir o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
	}
	
}
