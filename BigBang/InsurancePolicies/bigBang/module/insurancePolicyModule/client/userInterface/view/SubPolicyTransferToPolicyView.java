package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyTransferToPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyTransferToPolicyOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyTransferToPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyTransferToPolicyViewPresenter.Action;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class SubPolicyTransferToPolicyView extends View implements SubPolicyTransferToPolicyViewPresenter.Display{

	protected SubPolicyTransferToPolicyForm form;
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public SubPolicyTransferToPolicyView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		SubPolicyTransferToPolicyOperationsToolbar toolbar = new SubPolicyTransferToPolicyOperationsToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyTransferToPolicyViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyTransferToPolicyViewPresenter.Action>(Action.CONFIRM));
			}
		};
		wrapper.add(toolbar);
		
		form = new SubPolicyTransferToPolicyForm();
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return this.form;
	}

}
