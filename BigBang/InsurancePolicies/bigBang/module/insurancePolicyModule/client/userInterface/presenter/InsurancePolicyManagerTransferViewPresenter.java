package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Session;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicyManagerTransferViewPresenter implements ViewPresenter {

	public static enum Action {
		TRANSFER,
		CANCEL
	} 

	public static interface Display {
		HasEditableValue<String> getForm();

		void allowTransfer(boolean allow);

		void registerEventHandler(ActionInvokedEventHandler<Action> action);
		Widget asWidget();
	}

	private Display view;
	private InsurancePolicyBroker broker;
	private boolean bound =  false;
	private String currentPolicyId;

	public InsurancePolicyManagerTransferViewPresenter(Display view){
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
		this.currentPolicyId = parameterHolder.getParameter("policyid");
		this.currentPolicyId = this.currentPolicyId == null ? new String() : this.currentPolicyId;

		if(this.currentPolicyId.isEmpty()){
			clearView();
		}else{
			showManagerTransfer(this.currentPolicyId);
		}
	}

	private void bind(){
		if(bound){return;}

		view.registerEventHandler(new ActionInvokedEventHandler<InsurancePolicyManagerTransferViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TRANSFER:
					if(view.getForm().validate()) {
						transferClient(InsurancePolicyManagerTransferViewPresenter.this.currentPolicyId, view.getForm().getInfo());
					}else{
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
					}
					break;
				case CANCEL:
					onManagerTransferCancelled();
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS

		bound = true;
	}

	private void clearView(){
		view.allowTransfer(false);
		view.getForm().setValue(null);
	}

	private void showManagerTransfer(String policyId){
		broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				//TODO check permissions FJVC
				view.getForm().setValue(Session.getUserId());
				view.getForm().setReadOnly(false);
				view.allowTransfer(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientFailed();
			}
		});
	}

	private void transferClient(String policyId, String newManagerId){
		this.broker.createManagerTransfer(new String[]{policyId}, newManagerId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onManagerTransferSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onManagerTransferFailed();
			}
		});
	}

	private void onManagerTransferSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Transferência de Gestor criada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	} 

	private void onManagerTransferFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}

	private void onManagerTransferCancelled(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
