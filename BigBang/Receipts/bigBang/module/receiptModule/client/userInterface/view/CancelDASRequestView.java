package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DASRequest.Cancellation;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.DASRequestCancellationForm;
import bigBang.module.receiptModule.client.userInterface.DASRequestCancellationToolbar;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelDASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelDASRequestViewPresenter.Action;

public class CancelDASRequestView extends View implements CancelDASRequestViewPresenter.Display{

	private DASRequestCancellationToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private DASRequestCancellationForm form;
	
	public CancelDASRequestView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new DASRequestCancellationToolbar() {
			
			@Override
			public void onCancelDASRequestRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL_DAS_REQUEST));
				
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
				
			}
		};
		
		form = new DASRequestCancellationForm();
		wrapper.setSize("100%", "100%");
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
	public HasEditableValue<Cancellation> getForm() {
		return form;
	}


}


