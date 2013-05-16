package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicyTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		VALIDATE,
		GO_TO_PROCESS
	}

	public static interface Display {
		HasValue<InsurancePolicy> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		//PERMISSIONS
		void clearAllowedPermissions();
		void allowValidate(boolean allow);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected InsurancePolicyBroker broker;
	protected Display view;

	public InsurancePolicyTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY); 
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

		String policyId = parameterHolder.getParameter("id");
		showPolicy(policyId);
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<InsurancePolicyTasksViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case VALIDATE:
					onValidate();
					break;
				case GO_TO_PROCESS:
					NavigationHistoryManager.getInstance().NavigateToProcess(BigBangConstants.EntityIds.INSURANCE_POLICY,view.getForm().getValue().id);
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
	}

	@Override
	public void setPermittedOperations(String[] operationIds) {
		view.clearAllowedPermissions();
		for(String opid : operationIds) {
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.InsurancePolicyProcess.VALIDATE_POLICY)) {
				view.allowValidate(true);
			}
		}
	}

	protected void showPolicy(String policyId) {
		broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	//OPERATION ACTIONS

	protected void onValidate(){
		broker.validatePolicy(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice validada com Sucesso"), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				for(ResponseError error : errors){
					onValidationFailed(error.description.replaceAll("(\r\n|\n)", "<br />"));
				}
			}
		});
	}

	private void onValidationFailed(String message){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice falhou a validação :<br><br>" + message), TYPE.ALERT_NOTIFICATION));
	}

}
