package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter.Action;

public class SubPolicyView extends View implements SubPolicyViewPresenter.Display {

	private ActionInvokedEventHandler<Action> actionHandler;
	private SubPolicyForm form;
	private SubPolicyOperationsToolbar toolbar;
//	private SubPolicyChildrenPanel childrenPanel;
	
	public SubPolicyView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		toolbar = new SubPolicyOperationsToolbar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CANCEL_EDIT));
			}
			
		};
		
		form = new SubPolicyForm();
		form.setSize("100%", "100%");
		
		formWrapper.add(toolbar);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");
		
		wrapper.add(formWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<SubPolicy> getForm() {
		return this.form;
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

}
