package bigBang.module.expenseModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DocuShareHandle;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.HasNavigationStateChangedHandlers;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.shared.DocuShareItem;
import bigBang.module.expenseModule.client.userInterface.ExpenseImagePanel;
import bigBang.module.expenseModule.client.userInterface.ExpensePolicyWrapper;
import bigBang.module.expenseModule.client.userInterface.SerialExpenseCreationToolbar;
import bigBang.module.expenseModule.client.userInterface.form.SerialExpenseCreationForm;
import bigBang.module.expenseModule.client.userInterface.presenter.SerialExpenseCreationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.SerialExpenseCreationViewPresenter.Action;


public class SerialExpenseCreationView extends View implements SerialExpenseCreationViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private ExpenseImagePanel expensePanel;
	private SerialExpenseCreationForm form;
	private SerialExpenseCreationToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;

	public SerialExpenseCreationView() {
		wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel listWrapper = new VerticalPanel();

		listWrapper.setSize("100%", "100%");
		expensePanel = new ExpenseImagePanel();
		listWrapper.add(expensePanel);
		expensePanel.setSize("100%", "100%");
		listWrapper.setCellHeight(expensePanel, "100%");
		wrapper.addWest(listWrapper, 600);

		VerticalPanel right = new VerticalPanel();
		ListHeader rightHeader = new ListHeader("Despesa de Saúde");

		toolbar = new SerialExpenseCreationToolbar(){

			@Override
			public void saveExpense() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.CANCEL));
			}


		};

		right.setSize("100%", "100%");
		form = new SerialExpenseCreationForm(){

			@Override
			protected void onClickMarkAsInvalid() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.MARK_EXPENSE));
			}
			@Override
			protected void onChangedPolicyNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.POLICY_NUMBER_CHANGED));
			}
			@Override
			protected void onClickVerifyPolicyNumber() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.VERIFY_POLICY));
			}
			@Override
			protected void onSubPolicyValueChanged() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.SUB_POLICY_CHANGED));
			}
			@Override
			protected void noSubPolicyChangedState() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SerialExpenseCreationViewPresenter.Action>(Action.NO_SUB_POLICY_CHANGED_STATE));
			}

		};

		right.add(rightHeader);
		right.add(toolbar);
		right.add(form); 
		right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		right.setCellHeight(form, "100%");
		toolbar.setEnabled(false);
		form.setReadOnly(true);
		expensePanel.navigateToDirectoryList(null);

		wrapper.add(right);

	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;

	}

	@Override
	public HasNavigationStateChangedHandlers getNavigationPanel() {
		return expensePanel.getNavigationPanel();
	}

	@Override
	public HasEditableValue<ExpensePolicyWrapper> getForm() {
		return form;
	}

	@Override
	public DocuShareItem getSelectedDocuShareItem() {
		return expensePanel.getCurrentItem();
	}

	@Override
	protected void initializeView() {}

	@Override
	public void enableMarkExpense(boolean b) {
		form.enableMarkAsInvalid(b);
	}

	@Override
	public void clear() {
		form.showPolicyProblemLabel(false);
		form.clearExpense();
		form.clearPolicy();
		form.clearSubPolicy();
		form.enableMarkAsInvalid(false);
		form.setReadOnly(true);
		toolbar.setEnabled(false);
		form.enablePolicy(false);
	}

	@Override
	public String getPolicyNumber() {
		return form.getPolicyNumber();
	}

	@Override
	public void clearPolicy() {
		form.clearPolicy();
	}

	@Override
	public void setPolicyNumber(String policyNumber) {
		form.setPolicyNumber(policyNumber);
	}

	@Override
	public void setSubPolicies(String id) {
		form.setSubPolicies(id);
	}

	@Override
	public void removeDocuShareItem(DocuShareHandle handle) {
		expensePanel.removeSelected(handle.handle);
	}

	@Override
	public void panelNavigateBack() {
		expensePanel.getNavigationPanel().navigateBack();
	}

	@Override
	public void enablePolicyNumber(boolean b) {
		form.enablePolicy(b);
	}

	@Override
	public void setSubPolicyEnabled(boolean b) {
		form.setSubPolicyEnabled(b);
	}

	@Override
	public String getSubPolicyId() {
		return form.getSubPolicyId();
	}

	@Override
	public void setExpenseEnabled(boolean b) {
		form.setExpenseEnabled(b);
	}

	@Override
	public void clearSubPolicy() {
		form.clearSubPolicy();
	}

	@Override
	public void enableToolbar(boolean b) {
		toolbar.setEnabled(b);
	}

	@Override
	public void setPolicyNumberProblem(boolean b) {
		form.isPolicyNumberProblem(b);
		form.showPolicyProblemLabel(b);
		if(b){
			form.enableMarkAsInvalid(b);
		}
	}

	@Override
	public boolean getSubPolicyDisabled() {
		return form.getSubPolicyDisabled();
	}

	@Override
	public void clearExpense() {
		form.clearExpense();		
	}

	@Override
	public boolean isSubPolicy() {
		return form.isSubPolicy();
	}

	@Override
	public void markExpense(DocuShareItem currentItem) {
		expensePanel.markExpense(currentItem.handle);
		
	}

}
