package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SubCasualtyDeleteViewPresenter implements ViewPresenter {

	public static enum Action {
		DELETE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected SubCasualtyDataBroker broker;
	protected Display view;
	protected boolean bound = false;
	protected String requestId;

	public SubCasualtyDeleteViewPresenter(Display view){
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		setView((UIObject)view);
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
		clearView();

		requestId = parameterHolder.getParameter("subcasualtyid");
		if(requestId != null && !requestId.isEmpty()){
			showDelete();
		}else{
			onDeleteFailed();
		}
	}

	protected void bind() {
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<SubCasualtyDeleteViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case DELETE:
					onDelete();
					break;
				case CANCEL:
					onCancel();
					break;
				}				
			}
		});

		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
	}

	protected void showDelete(){
		this.broker.getSubCasualty(requestId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SubCasualtyProcess.DELETE_SUB_CASUALTY)){
					onDeleteFailed();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onDeleteFailed();
			}
		});
	}

	protected void onDelete(){
		this.broker.getSubCasualty(requestId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SubCasualtyProcess.DELETE_SUB_CASUALTY)){
					onDeleteFailed();
				}else{
					if(view.getForm().validate()) {
						broker.deleteSubCasualty(response.id, view.getForm().getInfo(), new ResponseHandler<Void>() {

							@Override
							public void onResponse(Void response) {
								onDeleteSuccess();
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onDeleteFailed();
							}
						});
					}
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onDeleteFailed();
			}
		});
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDeleteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro eliminado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("subcasualty");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
