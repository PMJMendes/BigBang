package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuranceSubPolicyTasksOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuranceSubPolicyTasksViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsuranceSubPolicyTasksViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InsuranceSubPolicyTasksView extends View implements InsuranceSubPolicyTasksViewPresenter.Display{

	protected SubPolicyForm form;
	protected InsuranceSubPolicyTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	public InsuranceSubPolicyTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new InsuranceSubPolicyTasksOperationsToolbar() {
			
			@Override
			public void onValidate() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.VALIDATE));
			}

			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<InsuranceSubPolicyTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
		};
		wrapper.add(toolbar);
		
		form = new SubPolicyForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		wrapper.add(form);
		wrapper.setCellHeight(form, "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<SubPolicy> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.hideAll();
		toolbar.setGoToProcessVisible();
	}

	@Override
	public void allowValidate(boolean allow) {
		toolbar.allowValidate(allow);
	}

}
