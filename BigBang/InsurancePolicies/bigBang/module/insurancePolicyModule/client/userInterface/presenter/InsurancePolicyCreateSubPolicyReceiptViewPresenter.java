package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DebitNoteBatch;
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

public class InsurancePolicyCreateSubPolicyReceiptViewPresenter implements ViewPresenter{

	public enum Action {
		CONFIRM,
		CANCEL
	}

	public interface Display {

		HasEditableValue<DebitNoteBatch> getForm();

		void registerEventHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();

	}

	private Display view;
	private InsurancePolicyBroker broker;
	private boolean bound = false;
	private String currentPolicyId;

	public InsurancePolicyCreateSubPolicyReceiptViewPresenter(
			Display view) {
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

	private void bind() {
		if(bound){
			return;
		}

		view.registerEventHandler(new ActionInvokedEventHandler<InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CONFIRM:
					onConfirm();
					break;
				}
			}
		});

		bound = true;

	}

	protected void onConfirm() {
		if(view.getForm().validate()){
			broker.createSubPolicyReceipts(view.getForm().getInfo(), new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Notas de débito criadas com sucesso"), TYPE.TRAY_NOTIFICATION));
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar as notas de débito"), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		else{
			onValidationFailed();
		}
	}

	private void onValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));		
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.currentPolicyId = parameterHolder.getParameter("policyid");
		this.currentPolicyId = this.currentPolicyId == null ? new String() : this.currentPolicyId;

		if(this.currentPolicyId.isEmpty()){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Problema ao obter a apólice."), TYPE.ALERT_NOTIFICATION));
		}else{
			showCreateDebitNote(this.currentPolicyId);
		}

	}

	private void showCreateDebitNote(String currentPolicyId2) {
		DebitNoteBatch batch = new DebitNoteBatch();
		batch.policyId = currentPolicyId2;
		view.getForm().setValue(batch);
	}
}
