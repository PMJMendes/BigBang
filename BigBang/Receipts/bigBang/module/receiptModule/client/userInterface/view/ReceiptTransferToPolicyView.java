package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.TransferToPolicyOperationsToolbar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptTransferToPolicyForm;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToPolicyViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToPolicyViewPresenter.Action;

public class ReceiptTransferToPolicyView extends View implements ReceiptTransferToPolicyViewPresenter.Display{

	private ReceiptTransferToPolicyForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public ReceiptTransferToPolicyView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		TransferToPolicyOperationsToolbar toolbar = new TransferToPolicyOperationsToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
			
			@Override
			public void onTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CONFIRM));
			}
		};
		wrapper.add(toolbar);
		
		form = new ReceiptTransferToPolicyForm();
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;
		
	}

	@Override
	public HasEditableValue<String> getForm() {
		return form;
	}

	@Override
	protected void initializeView() {
		return;
	}

}
