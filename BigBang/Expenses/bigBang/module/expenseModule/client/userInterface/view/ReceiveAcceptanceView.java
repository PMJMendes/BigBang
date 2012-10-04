package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.ReceiveResponseToolbar;
import bigBang.module.expenseModule.client.userInterface.form.ReceiveAcceptanceForm;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveAcceptanceViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveAcceptanceViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiveAcceptanceView extends View implements ReceiveAcceptanceViewPresenter.Display{

	private ReceiveResponseToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private ReceiveAcceptanceForm form;
	
	public ReceiveAcceptanceView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new ReceiveResponseToolbar() {
			
			@Override
			protected void onConfirmResponse() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiveAcceptanceViewPresenter.Action>(Action.ACCEPT));				
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiveAcceptanceViewPresenter.Action>(Action.CANCEL));				
			}
		};
		
		wrapper.add(toolbar);
		form = new ReceiveAcceptanceForm();
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
	public HasEditableValue<Acceptance> getForm() {
		return form;
	}

}
