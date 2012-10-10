package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsurancePolicy;
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

public class CreateExpenseViewPresenter implements ViewPresenter{

	public enum Action{
		SAVE,
		CANCEL
	}

	public interface Display{
		Widget asWidget();
		HasEditableValue<Expense> getForm();
		HasValue<InsurancePolicy> getInsuranceForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setToolBarSaveMode(boolean b);
		void setFormCreateMode();
	}

	private InsurancePolicyBroker broker;
	private Display view;
	private boolean bound;


	public CreateExpenseViewPresenter(Display view){
		broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((UIObject) view);
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
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<CreateExpenseViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case SAVE:
					onSave();
					break;
				}
			}
		});

		bound = true;
	}

	protected void onSave() {
		if(view.getForm().validate()) {
			broker.createExpense(view.getForm().getInfo(), new ResponseHandler<Expense>() {

				@Override
				public void onResponse(Expense response) {
					onCreateExpenseSuccess();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onCreateExpenseFailed();
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onCreateExpenseFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Despesa de Saúde"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onCreateExpenseSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Despesa de Saúde foi criada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.getForm().setValue(null);
		String policyId = parameterHolder.getParameter("policyid");

		if(policyId != null && !policyId.isEmpty()){
			broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					view.getForm().setReadOnly(false);
					showExpense(response);
					view.getInsuranceForm().setValue(response);
					view.setToolBarSaveMode(true);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onErrorPolicy();
				}
			});
		}
		else{
			onErrorPolicy();
		}
	}

	protected void showExpense(InsurancePolicy response) {
		Expense expense = new Expense();
		expense.clientId = response.clientId;
		expense.clientName = response.clientName;
		expense.clientNumber = response.clientNumber;
		expense.referenceId = response.id;
		expense.referenceNumber = response.number;
		expense.referenceTypeId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		expense.managerId = response.managerId;
		expense.lineName = response.lineName;
		expense.subLineName = response.subLineName;
		expense.categoryName = response.categoryName;
		expense.referenceSubLineId = response.subLineId;
		view.getForm().setValue(expense);
		view.setFormCreateMode();
	}

	protected void onErrorPolicy() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Apólice"), TYPE.ALERT_NOTIFICATION));
	}

}
