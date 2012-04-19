package bigBang.module.expenseModule.client.userInterface.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.ReceiveResponseToolbar;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveResponseViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ReceiveResponseViewPresenter.Action;

public class ReceiveResponseView extends View implements ReceiveResponseViewPresenter.Display{

	private ReceiveResponseToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	//FORMS
	private RadioButtonFormField choice;
	
	public ReceiveResponseView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new ReceiveResponseToolbar() {
			
			@Override
			protected void onConfirmResponse() {
				confirmResponse();
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiveResponseViewPresenter.Action>(Action.CANCEL));				
			}
		};
		
		choice = new RadioButtonFormField();
		choice.addOption("ACCEPT", "Aceite");
		choice.addOption("REFUSED", "Não aceite");
		wrapper.add(toolbar);
		wrapper.add(choice);
		
		choice.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equalsIgnoreCase("ACCEPT")){
					//TODO
				}else{
					//TODO
				}
			}
		});
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
