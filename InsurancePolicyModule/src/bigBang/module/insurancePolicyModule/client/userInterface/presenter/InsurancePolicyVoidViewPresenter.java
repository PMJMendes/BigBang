package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicyVoidViewPresenter implements ViewPresenter {

	public static enum Action {
		VOID,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<PolicyVoiding> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private InsurancePolicyBroker broker;
	private Display view;
	private boolean bound;

	public InsurancePolicyVoidViewPresenter(Display view){
		this.broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String ownerId = parameterHolder.getParameter("id");
		ownerId = ownerId == null ? new String() : ownerId;
		
		clearView();
		
		if(ownerId.isEmpty()){
			onGetOwnerFailed();
		}else{
			showVoidPolicy(ownerId);
		}
	}

	private void bind(){
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<InsurancePolicyVoidViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case VOID:
					onVoidPolicy();
					break;
				case CANCEL:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
					break;
				}
			};
		});

		bound = true;
	}
	
	private void clearView(){
		view.getForm().setValue(null);
	}
	
	private void showVoidPolicy(String ownerId){
		
		PolicyVoiding voiding = new PolicyVoiding();
		voiding.policyId = ownerId;
		view.getForm().setValue(voiding);
	}

	private void onVoidPolicy(){
		String policyId = view.getForm().getInfo().policyId;
		broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {
			
			@Override
			public void onResponse(InsurancePolicy response) {
				boolean hasPermission = PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.VOID_POLICY);
				
				if(hasPermission){
					broker.voidPolicy(view.getForm().getValue(), new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							onVoidPolicySuccess();
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.removeParameter("show");
							NavigationHistoryManager.getInstance().go(item);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onVoidPolicyFailed();
							NavigationHistoryManager.getInstance().reload();
						}
					});
				}else{
					onUserLacksPermission();
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
				}
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
			}
		});
	}

	private void onVoidPolicySuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Apólice foi anulada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onVoidPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível anular a apólice"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível anular a apólice"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onUserLacksPermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para anular a apólice"), TYPE.ALERT_NOTIFICATION));
	}

}
