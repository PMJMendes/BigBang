package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsuranceSubPolicyTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		VALIDATE
	}
	
	public static interface Display {
		HasValue<SubPolicy> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowValidate(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected InsuranceSubPolicyBroker broker;
	protected Display view;
	
	public InsuranceSubPolicyTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY); 
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
		
		view.registerActionHandler(new ActionInvokedEventHandler<InsuranceSubPolicyTasksViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case VALIDATE:
					onValidate();
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
		broker.getSubPolicy(policyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
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
		broker.validateSubPolicy(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice Adesão validada com Sucesso"), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível validar a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

}
