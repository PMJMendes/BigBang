package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.SingleClientTransferOperationsToolbar;
import bigBang.module.clientModule.client.userInterface.form.TransferClientManagerForm;
import bigBang.module.clientModule.client.userInterface.form.TransferClientManagerFormValidator;
import bigBang.module.clientModule.client.userInterface.presenter.SingleClientManagerTransferViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.SingleClientManagerTransferViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleClientManagerTransferView extends View implements SingleClientManagerTransferViewPresenter.Display {

	protected TransferClientManagerForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	private SingleClientTransferOperationsToolbar toolbar;

	public SingleClientManagerTransferView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new TransferClientManagerForm();
		form.setValidator(new TransferClientManagerFormValidator(form));
		form.setSize("100%", "100%");

		this.toolbar = new SingleClientTransferOperationsToolbar() {

			@Override
			public void onTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SingleClientManagerTransferViewPresenter.Action>(Action.TRANSFER));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SingleClientManagerTransferViewPresenter.Action>(Action.CANCEL));
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
