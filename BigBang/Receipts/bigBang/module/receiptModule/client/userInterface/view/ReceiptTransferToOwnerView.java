package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.OwnerRef;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.TransferToOwnerOperationsToolbar;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptTransferToOwnerForm;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToOwnerViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptTransferToOwnerViewPresenter.Action;

public class ReceiptTransferToOwnerView extends View implements ReceiptTransferToOwnerViewPresenter.Display{

	private ReceiptTransferToOwnerForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public ReceiptTransferToOwnerView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		TransferToOwnerOperationsToolbar toolbar = new TransferToOwnerOperationsToolbar() {
			
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
		
		form = new ReceiptTransferToOwnerForm();
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;
		
	}

	@Override
	public HasEditableValue<OwnerRef> getForm() {
		return form;
	}

	@Override
	protected void initializeView() {
		return;
	}

}
