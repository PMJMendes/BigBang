package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DASRequest;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.DASRequestRepetitionToolbar;
import bigBang.module.receiptModule.client.userInterface.form.DASRequestForm;
import bigBang.module.receiptModule.client.userInterface.presenter.RepeatDASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.RepeatDASRequestViewPresenter.Action;

public class RepeatDASRequestView extends View implements RepeatDASRequestViewPresenter.Display{

	private DASRequestForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	private DASRequestRepetitionToolbar toolbar;
	
	public RepeatDASRequestView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new DASRequestRepetitionToolbar(){
			
			@Override
			public void onRepeatDASRequest(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<RepeatDASRequestViewPresenter.Action>(Action.REPEAT_DAS_REQUEST));
			}
			
			@Override
			public void onCancelRequest(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<RepeatDASRequestViewPresenter.Action>(Action.CANCEL));
			}
		};
		
		
		form = new DASRequestForm();
		
		wrapper.setSize("100%", "100%");
		
		wrapper.add(toolbar);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;

	}

	@Override
	public HasEditableValue<DASRequest> getForm() {
		return form;
	}

	@Override
	protected void initializeView() {
		return;
	}

}
