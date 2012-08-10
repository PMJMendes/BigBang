package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
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
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class SubCasualtyTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		CLOSE,
		REJECT_CLOSE,
		GO_TO_PROCESS
	}
	
	public static interface Display {
		HasValue<SubCasualty> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowClose(boolean allow);
		void allowRejectClose(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected SubCasualtyDataBroker broker;
	protected Display view;
	protected ViewPresenterController overlayController;
	
	public SubCasualtyTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY); 
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
		
		String subCasualtyId = parameterHolder.getParameter("id");
		showSubCasualty(subCasualtyId);
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<SubCasualtyTasksViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case CLOSE:
					onClose();
					break;
				case REJECT_CLOSE:
					onRejectClose();
				case GO_TO_PROCESS:
					NavigationHistoryManager.getInstance().NavigateToProcess(BigBangConstants.EntityIds.SUB_CASUALTY, view.getForm().getValue().id);
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
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.SubCasualtyProcess.CLOSE_SUB_CASUALTY)) {
				view.allowClose(true);
			}else if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.SubCasualtyProcess.REJECT_CLOSE_SUB_CASUALTY)) {
				view.allowRejectClose(true);
			}
		}
	}
	
	protected void showSubCasualty(String subCasualtyId) {
		broker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onClose(){
		broker.closeSubCasualty(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Encerrado com Sucesso"), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Encerrar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}
	
	protected void onRejectClose(){
		HasParameters parameters = new HasParameters();
		parameters.setParameter("subcasualtyid", view.getForm().getValue().id);
		parameters.setParameter("show", "subcasualtyrejectclose");
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
				}else if(show.equalsIgnoreCase("subcasualtyrejectclose")){
					present("SUB_CASUALTY_REJECT_CLOSING", parameters);
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
