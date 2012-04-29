package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InfoRequestTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		RECEIVE_RESPONSE,
		REPEAT,
		CANCEL
	}
	
	public static interface Display {
		HasValue<InfoOrDocumentRequest> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		void allowReceiveResponse(boolean allow);
		void allowRepeat(boolean allow);
		void allowCancel(boolean allow);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected InfoOrDocumentRequestServiceAsync service;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public InfoRequestTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.service = InfoOrDocumentRequestService.Util.getInstance();
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
		initController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		
		String requestId = parameterHolder.getParameter("id");
		showRequest(requestId);
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case RECEIVE_RESPONSE:
					onReceiveResponse();
					break;
				case REPEAT:
					onRepeat();
					break;
				case CANCEL:
					onCancel();
					break;
				default:
					break;
				}
			}
		});
		
		bound = true;
	}
	
	protected void clearView(){
		view.getForm().setValue(null);
		view.clearAllowedPermissions();
		overlayController.onParameters(new HasParameters());
	}

	@Override
	public void setPermittedOperations(String[] operationIds) {
		view.clearAllowedPermissions();
		for(String opid : operationIds) {
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.InfoOrDocumentRequest.RECEIVE_REPLY)) {
				view.allowReceiveResponse(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.InfoOrDocumentRequest.REPEAT_REQUEST)) {
				view.allowRepeat(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.InfoOrDocumentRequest.CANCEL_REQUEST)) {
				view.allowCancel(true);
			}
		}
	}
	
	protected void showRequest(String requestId) {
		service.getRequest(requestId, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				view.getForm().setValue(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				super.onResponseFailure(caught);
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onReceiveResponse(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("requestid", view.getForm().getValue().id);
		parameters.setParameter("show", "receiveresponse");
		this.overlayController.onParameters(parameters);
	}
	
	protected void onRepeat(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("requestid", view.getForm().getValue().id);
		parameters.setParameter("show", "repeat");
		this.overlayController.onParameters(parameters);
	}
	
	protected void onCancel(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("requestid", view.getForm().getValue().id);
		parameters.setParameter("show", "cancel");
		this.overlayController.onParameters(parameters);
	}

	protected void initController(){
		this.overlayController = new ViewPresenterController(view.getOverlayViewContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				String show = parameters.getParameter("show");
				show = show == null ? new String() : show;

				if(show.isEmpty()){
					view.showOverlayViewContainer(false);

				//OVERLAY VIEWS
				}else if(show.equalsIgnoreCase("receiveresponse")){
					present("INFO_OR_DOCUMENT_REQUEST_REPLY", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("cancel")){
					present("INFO_OR_DOCUMENT_REQUEST_CANCELLATION", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("repeat")){
					present("INFO_OR_DOCUMENT_REQUEST_REPEAT", parameters);
					view.showOverlayViewContainer(true);
				}
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

}
