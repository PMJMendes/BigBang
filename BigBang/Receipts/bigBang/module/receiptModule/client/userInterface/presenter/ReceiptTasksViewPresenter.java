package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ReceiptTasksViewPresenter implements ViewPresenter,
		HasOperationPermissions {

	public static enum Action {
		CREATE_DAS_REQUEST,
		MARK_DAS_UNNECESSARY
	}
	
	public static interface Display {
		HasValue<Receipt> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		void allowCreateDASRequest(boolean allow);
		void allowMarkDASUnnecessary(boolean allow);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected ReceiptDataBroker broker;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public ReceiptTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
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
		
		String receiptId = parameterHolder.getParameter("id");
		showReceipt(receiptId);
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case CREATE_DAS_REQUEST:
					onCreateDASRequest();
					break;
				case MARK_DAS_UNNECESSARY:
					onMarkDASUnnecessary();
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
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.CREATE_DAS_REQUEST)) {
				view.allowCreateDASRequest(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.ReceiptProcess.SET_DAS_NOT_NECESSARY)) {
				view.allowMarkDASUnnecessary(true);
			}
		}
	}
	
	protected void showReceipt(String receiptId) {
		broker.getReceipt(receiptId, new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				view.getForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onCreateDASRequest(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("receiptid", view.getForm().getValue().id);
		parameters.setParameter("show", "createdasrequest");
		this.overlayController.onParameters(parameters);
	}
	
	protected void onMarkDASUnnecessary(){
		broker.setDASNotNecessary(view.getForm().getValue().id, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "DAS marcada como desnecessária"), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Marcar a DAS como Desnecessária"), TYPE.ALERT_NOTIFICATION));
			}
		});
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
				}else if(show.equalsIgnoreCase("createdasrequest")){
					present("CREATE_DAS_REQUEST", parameters);
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
