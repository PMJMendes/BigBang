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
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class SubPolicyTransferToPolicyViewPresenter implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<String> getForm();
		Widget asWidget();
	}

	protected boolean bound = false;
	protected InsuranceSubPolicyBroker broker;
	protected String subPolicyId;
	protected Display view;

	public SubPolicyTransferToPolicyViewPresenter(Display view) {
		broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		setView((UIObject) view);
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
		this.subPolicyId = parameterHolder.getParameter("subpolicyid");
		if(this.subPolicyId != null && !this.subPolicyId.isEmpty()) {
			clearView();
		}else{
			onTransferToPolicyFailed();
			onCancelTransfer();
		}
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<SubPolicyTransferToPolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onConfirmTransfer();
					break;
				case CANCEL:
					onCancelTransfer();
					break;
				}
			}
		});

		bound = true;
	}
	
	protected void clearView(){
		view.getForm().setValue(null);
	}
	
	protected void onConfirmTransfer(){
		String policyId = view.getForm().getInfo();
		this.broker.transferToInsurancePolicy(this.subPolicyId, policyId, new ResponseHandler<SubPolicy>() {
			
			@Override
			public void onResponse(SubPolicy response) {
				onTransferToPolicySuccess();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onTransferToPolicyFailed();
			}
		});
	}
	
	protected void onCancelTransfer(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onTransferToPolicySuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Apólice Adesão foi transferida com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	} 
	
	protected void onTransferToPolicyFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível transferir a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
	}

}
