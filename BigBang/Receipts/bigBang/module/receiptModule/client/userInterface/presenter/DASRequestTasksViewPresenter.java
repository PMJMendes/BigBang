package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.DASRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DASRequest.Response;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class DASRequestTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		RECEIVE_RESPONSE,
		REPEAT,
		CANCEL
	}
	
	public static interface Display {
		HasValue<DASRequest> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowReceiveResponse(boolean allow);
		void allowRepeat(boolean allow);
		void allowCancel(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected DASRequestBroker broker;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public DASRequestTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (DASRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DAS_REQUEST); 
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
		
		String dasRequestId = parameterHolder.getParameter("id");
		showDASRequest(dasRequestId);
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
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.DASRequestProcess.RECEIVE_REPLY)) {
				view.allowReceiveResponse(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.DASRequestProcess.REPEAT_DAS_REQUEST)) {
				view.allowRepeat(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.DASRequestProcess.CANCEL_DAS_REQUEST)) {
				view.allowCancel(true);
			}
		}
	}
	
	protected void showDASRequest(String dasRequestId) {
		broker.getRequest(dasRequestId, new ResponseHandler<DASRequest>() {

			@Override
			public void onResponse(DASRequest response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onReceiveResponse(){
		DASRequest.Response response = new Response();
		response.requestId = view.getForm().getValue().id;
		
		broker.receiveResponse(response, new ResponseHandler<DASRequest>() {

			@Override
			public void onResponse(DASRequest response) {
				onReceiveResponseSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onReceiveResponseFailed();
			}
		});
	}
	
	protected void onRepeat(){
		broker.repeatRequest(view.getForm().getValue(), new ResponseHandler<DASRequest>() {

			@Override
			public void onResponse(DASRequest response) {
				onRepeatSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onRepeatFailed();
			}
		});
	}
	
	protected void onCancel(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("dasrequestid", view.getForm().getValue().id);
		parameters.setParameter("show", "canceldasrequest");
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
				}else if(show.equalsIgnoreCase("canceldasrequest")){
					present("CANCEL_DAS_REQUEST", parameters);
					view.showOverlayViewContainer(true);
				}
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}
	
	protected void onReceiveResponseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Resposta Recebida com Sucesso"), TYPE.TRAY_NOTIFICATION));				
	}
	
	protected void onReceiveResponseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Receber a Resposta"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onRepeatSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido de DAS foi Repetido com Sucesso"), TYPE.TRAY_NOTIFICATION));				
	}
	
	protected void onRepeatFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Repetir o Pedido"), TYPE.ALERT_NOTIFICATION));
	}

}
