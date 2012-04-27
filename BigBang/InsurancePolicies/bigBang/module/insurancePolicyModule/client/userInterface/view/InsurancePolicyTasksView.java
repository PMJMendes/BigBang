package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyTasksOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTasksViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyTasksViewPresenter.Action;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class InsurancePolicyTasksView extends View implements InsurancePolicyTasksViewPresenter.Display{

	protected InsurancePolicyForm form;
	protected InsurancePolicyTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	public InsurancePolicyTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new InsurancePolicyTasksOperationsToolbar() {
			
			@Override
			public void onValidate() {
				handler.onActionInvoked(new ActionInvokedEvent<InsurancePolicyTasksViewPresenter.Action>(Action.VALIDATE));
			}
		};
		wrapper.add(toolbar);
		
		form = new InsurancePolicyForm(){
			
			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		};
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
	public HasValue<InsurancePolicy> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.hideAll();
	}

	@Override
	public void allowValidate(boolean allow) {
		toolbar.allowValidate(allow);
	}

}
