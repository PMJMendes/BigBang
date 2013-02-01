package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DebitNoteBatch;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyCreateSubPolicyReceiptToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.form.CreateSubPolicyReceiptForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyCreateSubPolicyReceiptViewPresenter;

public class InsurancePolicyCreateSubPolicyReceiptView extends View implements InsurancePolicyCreateSubPolicyReceiptViewPresenter.Display{

	protected CreateSubPolicyReceiptForm form;
	private ActionInvokedEventHandler<InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action> actionHandler;
	private InsurancePolicyCreateSubPolicyReceiptToolbar toolbar;
	
	public InsurancePolicyCreateSubPolicyReceiptView() {
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new CreateSubPolicyReceiptForm();
		
		this.toolbar = new InsurancePolicyCreateSubPolicyReceiptToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action>(InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action.CANCEL));
			}
			
			@Override
			protected void onCreateDebitNote() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action>(InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action.CONFIRM));
			}
		};
		
		wrapper.add(toolbar);
		wrapper.setCellHeight(toolbar, "21px");
		
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerEventHandler(ActionInvokedEventHandler<InsurancePolicyCreateSubPolicyReceiptViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasEditableValue<DebitNoteBatch> getForm() {
		return this.form;
	}

}
