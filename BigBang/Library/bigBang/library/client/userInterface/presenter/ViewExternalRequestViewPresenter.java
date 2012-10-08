package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.HistoryItemStub;
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
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ViewExternalRequestViewPresenter<T extends ProcessBase> implements ViewPresenter {

	public static enum Action {
		REPLY,
		CONTINUE,
		CLOSE, ON_BACK_BUTTON,
	}
	
	public static interface Display<T> {
		HasValue<ExternalInfoRequest> getForm();
		HasValue<T> getOwnerForm();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowReply(boolean allow);
		void allowContinue(boolean allow);
		void allowClose(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected ExternRequestServiceAsync service;
	protected Display<T> view;
	
	public ViewExternalRequestViewPresenter(Display<T> view){
		setView((UIObject) view);
		service = ExternRequestService.Util.getInstance();
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
		String externalRequestId = parameterHolder.getParameter("externalrequestid");
		
		clearView();
		
		if(externalRequestId == null || externalRequestId.isEmpty()){
			onFailure();
		}else{
			showRequest(externalRequestId);
		}
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ViewExternalRequestViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case CONTINUE:
					onContinue();
					break;
				case REPLY:
					onReply();
					break;
				case CLOSE:
					onClose();
					break;
				case ON_BACK_BUTTON:
					onBackButton();
					break;
				}
			}
		});
		
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selectable = (ValueSelectable<?>) event.getFirstSelected();
				HistoryItemStub item = selectable == null ? null : (HistoryItemStub) selectable.getValue();
				if(item != null) {
					showHistory(view.getForm().getValue().id, item.id);
				}
			}
		});
		
		bound = true;
	}
	
	protected void onBackButton() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("viewexternalrequest");
		NavigationHistoryManager.getInstance().go(item);	
		}

	protected void clearView(){
		view.getForm().setValue(null);
		view.getOwnerForm().setValue(null);
		view.getHistoryList().clearSelection();
	}
	
	protected void showRequest(String requestId){
		service.getRequest(requestId, new BigBangAsyncCallback<ExternalInfoRequest>() {

			@Override
			public void onResponseSuccess(ExternalInfoRequest result) {
				view.clearAllowedPermissions();
				
				view.allowReply(PermissionChecker.hasPermission(result, BigBangConstants.OperationIds.ExternalInfoRequest.REPLY));
				view.allowContinue(PermissionChecker.hasPermission(result, BigBangConstants.OperationIds.ExternalInfoRequest.CONTINUE));
				view.allowClose(PermissionChecker.hasPermission(result, BigBangConstants.OperationIds.ExternalInfoRequest.CLOSE));
				
				view.getForm().setValue(result);
				showOwner(result.parentDataObjectId);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				ViewExternalRequestViewPresenter.this.onFailure();
				super.onResponseFailure(caught);
			}
		});
	}
	
	protected abstract void showOwner(String ownerId);
	
	protected void onContinue(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "continueexternalrequest");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onReply(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "replyexternalrequest");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onClose(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "closeexternalrequest");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected abstract void onFailure();
	
	protected abstract void showHistory(String ownerId, String historyItemId);
	
}
