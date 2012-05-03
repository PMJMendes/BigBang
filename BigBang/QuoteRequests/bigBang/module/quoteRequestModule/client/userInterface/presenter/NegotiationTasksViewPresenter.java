package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation;
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

public class NegotiationTasksViewPresenter implements ViewPresenter,
		HasOperationPermissions {

	public static enum Action {
		GRANT,
		CANCEL,
		RECEIVE_QUOTE,
		REPEAT_QUOTE_REQUEST
	}
	
	public static interface Display {
		HasValue<Negotiation> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowGrant(boolean allow);
		void allowCancel(boolean allow);
		void allowReceiveQuote(boolean allow);
		void allowRepeatQuoteRequest(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected NegotiationBroker broker;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public NegotiationTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION); 
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
		showNegotiation(requestId);
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case GRANT:
					onGrant();
					break;
				case CANCEL:
					onCancel();
					break;
				case RECEIVE_QUOTE:
					onReceiveQuote();
					break;
				case REPEAT_QUOTE_REQUEST:
					onRepeatQuoteRequest();
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
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.NegotiationProcess.GRANT_NEGOTIATION)) {
				view.allowGrant(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.NegotiationProcess.CANCEL_NEGOTIATION)) {
				view.allowCancel(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.NegotiationProcess.RECEIVE_QUOTE)) {
				view.allowReceiveQuote(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.NegotiationProcess.REPEAT_QUOTE_REQUEST)) {
//				view.allowRepeatQuoteRequest(true); TODO
			}
		}
	}
	
	protected void showNegotiation(String negotiationId) {
		broker.getNegotiation(negotiationId, new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onGrant(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("negotiationid", view.getForm().getValue().id);
		parameters.setParameter("show", "grantnegotiation");
		this.overlayController.onParameters(parameters);
	};
	
	protected void onReceiveQuote(){
		Negotiation.Response response = new Negotiation.Response();
		response.negotiationId = view.getForm().getValue().id;
		
		broker.receiveResponse(response, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				onReceiveResponseSuccess();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onReceiveResponseFailed();
			}
		});
	}
	
	protected void onRepeatQuoteRequest() {
//		broker.repeatRequest(view.getForm().getValue(), new ResponseHandler<SignatureRequest>() { TODO
//
//			@Override
//			public void onResponse(SignatureRequest response) {
//				onRepeatSuccess();
//			}
//
//			@Override
//			public void onError(Collection<ResponseError> errors) {
//				onRepeatFailed();
//			}
//		});
	}
	
	protected void onCancel(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("negotiationid", view.getForm().getValue().id);
		parameters.setParameter("show", "cancelnegotiation");
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
				}else if(show.equalsIgnoreCase("cancelnegotiation")){
					present("NEGOTIATION_CANCEL", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("grantnegotiation")){
					present("NEGOTIATION_GRANT", parameters);
					view.showOverlayViewContainer(true);
				}
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				view.showOverlayViewContainer(false);
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
