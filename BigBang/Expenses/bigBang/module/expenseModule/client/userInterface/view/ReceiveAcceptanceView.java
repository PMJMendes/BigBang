package bigBang.module.expenseModule.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.ReceiveAcceptanceForm;
import bigBang.module.expenseModule.client.userInterface.ReceiveResponseToolbar;
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
				confirmResponse();
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiveAcceptanceViewPresenter.Action>(Action.CANCEL));				
			}
		};

		wrapper.add(toolbar);
	}
	
	
	protected void confirmResponse() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void clearForms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeView() {
		return;
	}

}
