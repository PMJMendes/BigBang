package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.HasNavigationStateChangedHandlers;
import bigBang.library.client.event.NavigationStateChangedEvent;
import bigBang.library.client.event.NavigationStateChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.shared.DocuShareItem;
import bigBang.module.expenseModule.shared.ExpensePolicyWrapper;
import bigBang.module.receiptModule.client.userInterface.presenter.PolicyChoiceFromListViewPresenter;
import bigBang.module.receiptModule.client.userInterface.view.PolicyChoiceFromListView;

public class SerialExpenseCreationViewPresenter implements ViewPresenter{

	public enum Action{
		SAVE,
		CANCEL, 
		MARK_EXPENSE,
		POLICY_NUMBER_CHANGED, 
		VERIFY_POLICY, SUB_POLICY_CHANGED, NO_SUB_POLICY_CHANGED_STATE
	}

	private Display view;
	private boolean bound = false;
	private InsurancePolicyBroker policyBroker;
	private InsuranceSubPolicyBroker subPolicyBroker;
	private ExpenseDataBroker expenseBroker;

	private PopupPanel popup;
	private ExpensePolicyWrapper expensePolicyWrapper;
	private PolicyChoiceFromListView policyView;
	private PolicyChoiceFromListViewPresenter policyPresenter;
	private boolean editing;


	public SerialExpenseCreationViewPresenter(Display view) {
		policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		expenseBroker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
		subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		setView((UIObject)view);

		popup = new PopupPanel();

		policyView = (PolicyChoiceFromListView) GWT.create(PolicyChoiceFromListView.class);
		policyPresenter = new PolicyChoiceFromListViewPresenter(policyView){

			@Override
			protected void onCancel() {
				SerialExpenseCreationViewPresenter.this.view.enableMarkExpense(true);
				popup.hidePopup();
			}

			@Override
			protected void onMark() {
				SerialExpenseCreationViewPresenter.this.onMarkExpense();
			}

			@Override
			public void setInsurancePolicys(
					Collection<InsurancePolicyStub> stubs) {
				policyView.fillList(stubs);
			}

			@Override
			public void getSelectedInsurancePolicy() {
				SerialExpenseCreationViewPresenter.this.getPolicy(policyView.getForm().getValue().id);
				popup.hidePopup();
			}

		};

		policyPresenter.go(popup);
	}


	public interface Display{
		Widget asWidget();

		void registerActionHandler(ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		HasNavigationStateChangedHandlers getNavigationPanel();

		HasEditableValue<ExpensePolicyWrapper> getForm();

		DocuShareItem getSelectedDocuShareItem();

		void enableMarkExpense(boolean b);

		void clear();

		String getPolicyNumber();

		void clearPolicy();

		void setPolicyNumber(String policyNumber);

		void setSubPolicies(String id);

		void removeDocuShareItem(DocuShareHandle handle);

		void panelNavigateBack();

		void enablePolicyNumber(boolean b);

		void setSubPolicyEnabled(boolean b);

		String getSubPolicyId();

		void setExpenseEnabled(boolean b);

		void clearSubPolicy();

		void enableToolbar(boolean b);

		void setPolicyNumberProblem(boolean b);

		boolean getSubPolicyDisabled();

		void clearExpense();

		boolean isSubPolicy();

		void markExpense(DocuShareItem currentItem);
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

	private void bind() {
		if(bound){
			return;
		}
		view.registerActionHandler(new ActionInvokedEventHandler<SerialExpenseCreationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case VERIFY_POLICY:
					onVerifyPolicy();
					break;
				case MARK_EXPENSE: 
					onMarkExpense();
					break;
				case POLICY_NUMBER_CHANGED:
					onPolicyNumberChanged();
					break;
				case SAVE:
					onSave();
					break;
				case SUB_POLICY_CHANGED:
					onSubPolicyChanged();
					break;
				case NO_SUB_POLICY_CHANGED_STATE:
					noSubPolicyChangedState();
					break;
				}

			}
		});
		
		view.getNavigationPanel().registerNavigationStateChangedHandler(new NavigationStateChangedEventHandler() {
			
			@Override
			public void onNavigationStateChanged(NavigationStateChangedEvent event) {
				if(event.getObject() instanceof ImageHandlerPanel){
					view.enablePolicyNumber(true);
				}
				else{
					view.clear();
				}
			}
		});
		
		bound = true;
	}

	protected void noSubPolicyChangedState() {
		if(!view.isSubPolicy()){
			expensePolicyWrapper.expense = new Expense();
			expensePolicyWrapper.expense.managerId = expensePolicyWrapper.policy.managerId;
			expensePolicyWrapper.expense.referenceId = expensePolicyWrapper.policy.id;
			expensePolicyWrapper.expense.referenceTypeId = BigBangConstants.EntityIds.INSURANCE_POLICY;
			view.enableToolbar(true);
		}else{
			expensePolicyWrapper.expense = new Expense();
			view.enableToolbar(false);
		}
		view.getForm().setValue(expensePolicyWrapper);
	}

	protected void onSubPolicyChanged() {
		String subPolicyId = view.getSubPolicyId();
		
		if(subPolicyId != null){
		
			subPolicyBroker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {
				
				@Override
				public void onResponse(SubPolicy response) {
					expensePolicyWrapper.subPolicy = response;
					expensePolicyWrapper.expense = new Expense();
					expensePolicyWrapper.expense.clientId = response.clientId;
					expensePolicyWrapper.expense.clientName = response.clientName;
					expensePolicyWrapper.expense.referenceId = response.id;
					expensePolicyWrapper.expense.clientNumber = response.clientNumber;
					expensePolicyWrapper.expense.referenceNumber = response.number;
					expensePolicyWrapper.expense.referenceTypeId = BigBangConstants.EntityIds.INSURANCE_SUB_POLICY;
					expensePolicyWrapper.expense.managerId = response.managerId;
					expensePolicyWrapper.expense.lineName = response.inheritLineName;
					expensePolicyWrapper.expense.subLineName = response.inheritSubLineName;
					view.getForm().setValue(expensePolicyWrapper);
					view.setExpenseEnabled(true);
					view.enableToolbar(true);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice adesão."), TYPE.ALERT_NOTIFICATION));				
				}
			});
		}else{
			expensePolicyWrapper.subPolicy = new SubPolicy();
			view.clearExpense();
			view.setExpenseEnabled(false);
		}
	}

	protected void onSave() {
		ExpensePolicyWrapper toSave = view.getForm().getInfo();
		final DocuShareHandle handle = new DocuShareHandle();
		
		handle.handle = view.getSelectedDocuShareItem().handle;
		
		expenseBroker.serialCreateExpense(toSave.expense, handle, new ResponseHandler<Expense> () {
			
			@Override
			public void onResponse(Expense response) {
				editing = false;
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Despesa de saúde criada com sucesso."), TYPE.TRAY_NOTIFICATION));
				view.removeDocuShareItem(handle);
				view.panelNavigateBack();
				expensePolicyWrapper = new ExpensePolicyWrapper();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a despesa de saúde."), TYPE.ALERT_NOTIFICATION));				
			}
			
		});
	}

	protected void onPolicyNumberChanged() {
		if(editing){
			expensePolicyWrapper = new ExpensePolicyWrapper();
			String tempPolicy = view.getPolicyNumber();
			view.clearSubPolicy();
			view.setSubPolicyEnabled(false);
			view.setExpenseEnabled(false);
			view.setPolicyNumber(tempPolicy);
			editing = false;
			view.enableToolbar(false);
		}
	}

	protected void onMarkExpense() {
		
		DocuShareItem currentItem = view.getSelectedDocuShareItem();
		view.enablePolicyNumber(false);
		view.markExpense(currentItem);
		view.enableMarkExpense(false);
		view.clear();
		if(popup.isAttached()){
			popup.hidePopup();
		}
		view.clear();
	}

	protected void onVerifyPolicy() {
		final String policyNumber = view.getPolicyNumber();
		editing = true;
		view.clearPolicy();
		view.enableMarkExpense(false);
		view.setPolicyNumber(policyNumber);

		policyBroker.getInsurancePoliciesWithNumber(policyNumber, new ResponseHandler<Collection<InsurancePolicyStub>>() {

			@Override
			public void onResponse(Collection<InsurancePolicyStub> response) {
				if(response.size() > 1){
					policyPresenter.setParameters(null);
					policyPresenter.fillList(response);
					popup.center();
					view.setPolicyNumberProblem(false);
					view.enableMarkExpense(true);
				}
				else if(response.size() == 1){
					getPolicy(((InsurancePolicyStub)response.toArray()[0]).id);
					view.setPolicyNumberProblem(false);
					view.enableMarkExpense(true);
				}
				else{
					view.setPolicyNumberProblem(true);
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter as apólices."), TYPE.ALERT_NOTIFICATION));				
			}
		});
	}

	protected void onCancel() {
		view.clear();
		view.panelNavigateBack();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		expensePolicyWrapper = new ExpensePolicyWrapper();
		expensePolicyWrapper.policy = new InsurancePolicy();
		expensePolicyWrapper.expense = new Expense();
		expensePolicyWrapper.subPolicy = new SubPolicy();
		view.clear();
	}

	public void getPolicy(String id){
		policyBroker.getPolicy(id, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				if(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_HEALTH_EXPENSE)){
					expensePolicyWrapper.policy = response;
					view.setSubPolicies(response.id);
					view.setSubPolicyEnabled(true);
				}
				else{
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Esta apólice não permite a criação de despesas de saúde."), TYPE.ALERT_NOTIFICATION));
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}


}
