package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.receiptModule.client.userInterface.ReceiptReturnToolbar;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptReturnForm;
import  bigBang.module.receiptModule.client.userInterface.presenter.ReceiptReturnViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptReturnViewPresenter.Action;
import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class ReceiptReturnView extends View implements ReceiptReturnViewPresenter.Display{

	public ReceiptReturnForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public ReceiptReturnView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new ReceiptReturnForm();
		
		ReceiptReturnToolbar toolbar = new ReceiptReturnToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptReturnViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onSetForReturn() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptReturnViewPresenter.Action>(Action.CONFIRM));
				
			}
		};
		toolbar.setHeight("21px");
		wrapper.add(toolbar);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
		
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;
	}

	@Override
	public HasEditableValue<ReturnMessage> getForm() {
		return form;
	}

}
