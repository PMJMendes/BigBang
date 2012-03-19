package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ViewInfoOrDocumentRequestViewPresenter<T extends ProcessBase> implements ViewPresenter {

	public static enum Action {
		REPEAT_REQUEST,
		RECEIVE_RESPONSE,
		CANCEL_REQUEST
	}
	
	public static interface Display<T extends ProcessBase> {
		HasValue<InfoOrDocumentRequest> getForm();
		HasValue<T> getParentForm();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		void clearAllowedPermissions();
		void allowRepeat(boolean allow);
		void allowReceiveReply(boolean allow);
		void allowCancelRequest(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected InfoOrDocumentRequestServiceAsync requestService;
	protected Display<T> view;
	
	public ViewInfoOrDocumentRequestViewPresenter(Display<T> view) {
		setView((UIObject) view);
		requestService = InfoOrDocumentRequestService.Util.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setView(UIObject view) {
		this.view = (Display<T>) view;
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

	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ViewInfoOrDocumentRequestViewPresenter.Action>() {
			
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
	
	protected void clearView(){
		view.clearAllowedPermissions();
		view.getParentForm().setValue(null);
		view.getForm().setValue(null);
	}
	
	protected void showRequest(String requestId){
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
				ViewInfoOrDocumentRequestViewPresenter.this.onFailure();
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
	
	protected abstract void showParent(String parentId);
	
	protected void onRepeatRequest(){
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
	
	protected void onReceiveResponse(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "replyinforequest");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onCancelRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "cancelinforequest");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected abstract void showHistory(String processId, String historyItemId);
	
	protected abstract void onFailure();
	
	protected abstract void onRepeatSuccess();
	
	protected abstract void onRepeatFailed();

}
