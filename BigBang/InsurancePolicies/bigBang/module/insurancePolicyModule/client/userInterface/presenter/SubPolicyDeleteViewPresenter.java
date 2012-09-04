package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
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

public class SubPolicyDeleteViewPresenter implements ViewPresenter {

	public static enum Action {
		DELETE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected InsuranceSubPolicyBroker broker;
	protected Display view;
	protected boolean bound = false;
	protected String subPolicyId;

	public SubPolicyDeleteViewPresenter(Display view){
		this.broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
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

		subPolicyId = parameterHolder.getParameter("subpolicyid");
		if(subPolicyId != null && !subPolicyId.isEmpty()){
			showDelete();
		}else{
			onDeleteFailed();
		}
	}

	protected void bind() {
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<SubPolicyDeleteViewPresenter.Action>() {

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
		this.broker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY)){
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
		this.broker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY)){
					onDeleteFailed();
				}else{
					if(view.getForm().validate()) {
						broker.removeSubPolicy(response.id, view.getForm().getInfo(), new ResponseHandler<String>() {

							@Override
							public void onResponse(String response) {
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
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice Adesão eliminada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("subpolicyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
