package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyManagerTransferViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyManagerTransferViewPresenter.Action;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyTransferOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.TransferInsurancePolicyManagerForm;

import com.google.gwt.user.client.ui.VerticalPanel;

public class InsurancePolicyManagerTransferView extends View implements InsurancePolicyManagerTransferViewPresenter.Display {

	protected TransferInsurancePolicyManagerForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	private InsurancePolicyTransferOperationsToolbar toolbar;

	public InsurancePolicyManagerTransferView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new TransferInsurancePolicyManagerForm();
		form.setSize("100%", "100%");

		this.toolbar = new InsurancePolicyTransferOperationsToolbar() {
			
			@Override
			public void onTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.TRANSFER));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		};
		wrapper.add(toolbar);
		wrapper.setCellHeight(toolbar, "21px");

		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form, "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerEventHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return this.form;
	}

	@Override
	public void allowTransfer(boolean allow) {
		this.toolbar.allowTransfer(allow);
	}
	
}
