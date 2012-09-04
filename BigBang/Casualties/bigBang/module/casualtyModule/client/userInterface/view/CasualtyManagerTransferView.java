package bigBang.module.casualtyModule.client.userInterface.view;

import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyManagerTransferViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyManagerTransferViewPresenter.Action;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.SingleCasualtyTransferOperationsToolbar;
import bigBang.module.casualtyModule.client.userInterface.TransferCasualtyManagerForm;

import com.google.gwt.user.client.ui.VerticalPanel;

public class CasualtyManagerTransferView extends View implements CasualtyManagerTransferViewPresenter.Display {

	protected TransferCasualtyManagerForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	private SingleCasualtyTransferOperationsToolbar toolbar;

	public CasualtyManagerTransferView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new TransferCasualtyManagerForm();
		form.setSize("100%", "100%");

		this.toolbar = new SingleCasualtyTransferOperationsToolbar() {

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
