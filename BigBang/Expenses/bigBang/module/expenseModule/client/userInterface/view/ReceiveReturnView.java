package bigBang.module.expenseModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.ReceiveResponseToolbar;
import bigBang.module.expenseModule.client.userInterface.form.ReceiveReturnForm;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveReturnViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveReturnViewPresenter.Action;;

public class ReceiveReturnView  extends View implements ReceiveReturnViewPresenter.Display{

	private ReceiveResponseToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private ReceiveReturnForm form;
	
	public ReceiveReturnView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new ReceiveResponseToolbar() {
			
			@Override
			protected void onConfirmResponse() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ACCEPT));				
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));				
			}
		};

		wrapper.add(toolbar);
		form = new ReceiveReturnForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");

	}
	


	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void clearForms() {
		form.setValue(null);
	}

	@Override
	protected void initializeView() {
		return;
	}


	@Override
	public HasEditableValue<ReturnEx> getForm() {
		return form;
	}

}
