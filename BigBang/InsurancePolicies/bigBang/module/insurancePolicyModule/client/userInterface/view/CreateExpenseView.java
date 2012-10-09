package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;
import bigBang.module.insurancePolicyModule.client.userInterface.CreateExpenseOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateExpenseViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateExpenseViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateExpenseView extends View implements CreateExpenseViewPresenter.Display{

	protected InsurancePolicyForm policyForm;
	protected ExpenseForm form;
	protected ActionInvokedEventHandler<Action> handler;
	protected CreateExpenseOperationsToolbar toolbar;

	public CreateExpenseView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel parentWrapper = new VerticalPanel();
		parentWrapper.setSize("100%", "100%");
		wrapper.addWest(parentWrapper, 600);

		ListHeader parentHeader = new ListHeader("Apólice");
		parentHeader.setHeight("30px");
		parentWrapper.add(parentHeader);

		policyForm = new InsurancePolicyForm();
		policyForm.setHeaderFormVisible(false);
		policyForm.setReadOnly(true);
		policyForm.setSize("100%", "100%");
		parentWrapper.add(policyForm);
		parentWrapper.setCellHeight(policyForm, "100%");

		VerticalPanel expenseWrapper = new VerticalPanel();
		expenseWrapper.setSize("100%", "100%");
		wrapper.add(expenseWrapper);

		ListHeader expenseHeader = new ListHeader("Despesa de Saúde");
		expenseHeader.setHeight("30px");
		expenseWrapper.add(expenseHeader);

		toolbar = new CreateExpenseOperationsToolbar() {

			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateExpenseViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateExpenseViewPresenter.Action>(Action.CANCEL));
			}
		};

		expenseWrapper.add(toolbar);

		form = new ExpenseForm();
		form.setSize("100%", "100%");
		expenseWrapper.add(form);
		expenseWrapper.setCellHeight(form, "100%");

	}

	@Override
	public HasEditableValue<Expense> getForm() {
		return form;
	}

	@Override
	public HasValue<InsurancePolicy> getInsuranceForm() {
		return policyForm;
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setToolBarSaveMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
	}

	@Override
	public void setFormCreateMode() {
		form.setNewExpenseMode();
	}

}
