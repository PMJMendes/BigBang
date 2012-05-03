package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;

public class ExternalInfoRequestTasksViewPresenter implements ViewPresenter,
		HasOperationPermissions {
	
	public static enum Action {
		SEND_RESPONSE,
		CONTINUE,
		CLOSE
	}
	
	public static interface Display {
		HasValue<ExternalInfoRequest> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		void allowSendResponse(boolean allow);
		void allowContinue(boolean allow);
		void allowClose(boolean allow);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected ExternRequestServiceAsync service;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public ExternalInfoRequestTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.service = ExternRequestService.Util.getInstance();
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
				case SEND_RESPONSE:
					onSendResponse();
					break;
				case CONTINUE:
					onContinue();
					break;
				case CLOSE:
					onClose();
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
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.ExternalInfoRequest.REPLY)) {
				view.allowSendResponse(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.ExternalInfoRequest.CONTINUE)) {
				view.allowContinue(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.ExternalInfoRequest.CLOSE)) {
				view.allowClose(true);
			}
		}
	}
	
	protected void showRequest(String requestId) {
		service.getRequest(requestId, new BigBangAsyncCallback<ExternalInfoRequest>() {

			@Override
			public void onResponseSuccess(ExternalInfoRequest result) {
				view.getForm().setValue(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				super.onResponseFailure(caught);
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onSendResponse(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("externalrequestid", view.getForm().getValue().id);
		parameters.setParameter("show", "sendresponse");
		this.overlayController.onParameters(parameters);
	}
	
	protected void onContinue(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("externalrequestid", view.getForm().getValue().id);
		parameters.setParameter("show", "continue");
		this.overlayController.onParameters(parameters);
	}
	
	protected void onClose(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("externalrequestid", view.getForm().getValue().id);
		parameters.setParameter("show", "close");
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
				}else if(show.equalsIgnoreCase("sendresponse")){
					present("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_REPLY", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("continue")){
					present("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_CONTINUATION", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("close")){
					present("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_CLOSING", parameters);
					view.showOverlayViewContainer(true);
				}
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				view.showOverlayViewContainer(false);
			}
		};
	}

}
