package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.dataAccess.SignatureRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SignatureRequest;
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

public class SignatureRequestTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		RECEIVE,
		REPEAT,
		CANCEL
	}
	
	public static interface Display {
		HasValue<SignatureRequest> getForm();
		HasValue<Receipt> getReceiptForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowReceive(boolean allow);
		void allowRepeat(boolean allow);
		void allowCancel(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected SignatureRequestBroker broker;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public SignatureRequestTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (SignatureRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SIGNATURE_REQUEST); 
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
		showSignatureRequest(requestId);
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case RECEIVE:
					onReceive();
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
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.SignatureRequestProcess.RECEIVE_REPLY)) {
				view.allowReceive(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.SignatureRequestProcess.REPEAT_SIGNATURE_REQUEST)) {
				view.allowRepeat(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.SignatureRequestProcess.CANCEL_SIGNATURE_REQUEST)) {
				view.allowCancel(true);
			}
		}
	}
	
	protected void showSignatureRequest(String requestId) {
		broker.getRequest(requestId, new ResponseHandler<SignatureRequest>() {

			@Override
			public void onResponse(SignatureRequest response) {
				view.getForm().setValue(response);
				ReceiptDataBroker receiptBroker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
				receiptBroker.getReceipt(response.receiptId, new ResponseHandler<Receipt>() {
					
					@Override
					public void onResponse(Receipt response) {
						view.getReceiptForm().setValue(response);
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			};

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onReceive(){
		SignatureRequest.Response response = new SignatureRequest.Response();
		response.requestId = view.getForm().getValue().id;
		
		broker.receiveResponse(response, new ResponseHandler<SignatureRequest>() {
			
			@Override
			public void onResponse(SignatureRequest response) {
				onReceiveResponseSuccess();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onReceiveResponseFailed();
			}
		});
	}
	
	protected void onRepeat() {
		broker.repeatRequest(view.getForm().getValue(), new ResponseHandler<SignatureRequest>() {

			@Override
			public void onResponse(SignatureRequest response) {
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
		parameters.setParameter("signaturerequestid", view.getForm().getValue().id);
		parameters.setParameter("show", "cancelsignaturerequest");
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
				}else if(show.equalsIgnoreCase("cancelsignaturerequest")){
					present("CANCEL_SIGNATURE_REQUEST", parameters);
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
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao Receber a Resposta"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onRepeatSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido Repetido com Sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
	protected void onRepeatFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao repetir o pedido"), TYPE.ALERT_NOTIFICATION));
	}
	
}
