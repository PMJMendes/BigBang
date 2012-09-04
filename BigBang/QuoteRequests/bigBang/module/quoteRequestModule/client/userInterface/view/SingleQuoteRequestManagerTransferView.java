package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.SingleQuoteRequestTransferOperationsToolbar;
import bigBang.module.quoteRequestModule.client.userInterface.TransferQuoteRequestManagerForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.SingleQuoteRequestManagerTransferViewPresenter;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SingleQuoteRequestManagerTransferView extends View implements SingleQuoteRequestManagerTransferViewPresenter.Display {

	protected TransferQuoteRequestManagerForm form;
	private ActionInvokedEventHandler<SingleQuoteRequestManagerTransferViewPresenter.Action> actionHandler;
	private SingleQuoteRequestTransferOperationsToolbar toolbar;

	public SingleQuoteRequestManagerTransferView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new TransferQuoteRequestManagerForm();
		form.setSize("100%", "100%");

		this.toolbar = new SingleQuoteRequestTransferOperationsToolbar() {

			@Override
			public void onTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SingleQuoteRequestManagerTransferViewPresenter.Action>(SingleQuoteRequestManagerTransferViewPresenter.Action.TRANSFER));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SingleQuoteRequestManagerTransferViewPresenter.Action>(SingleQuoteRequestManagerTransferViewPresenter.Action.CANCEL));
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
	public void registerEventHandler(ActionInvokedEventHandler<SingleQuoteRequestManagerTransferViewPresenter.Action> handler) {
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
