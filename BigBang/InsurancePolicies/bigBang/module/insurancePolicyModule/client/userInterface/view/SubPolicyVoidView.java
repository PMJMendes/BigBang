package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.PolicyVoiding;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyVoidForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyVoidToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyVoidViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyVoidViewPresenter.Action;

public class SubPolicyVoidView extends View implements SubPolicyVoidViewPresenter.Display {

	private InsurancePolicyVoidForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public SubPolicyVoidView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		this.form = new InsurancePolicyVoidForm();
		
		InsurancePolicyVoidToolbar toolbar = new InsurancePolicyVoidToolbar(){
			
			@Override
			public void onVoidRequest(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyVoidViewPresenter.Action>(Action.VOID));
			}
			
			@Override
			public void onCancelRequest(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyVoidViewPresenter.Action>(Action.CANCEL));
			}
		};
		
		wrapper.add(toolbar);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<PolicyVoiding> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

}
