package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DASRequest;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.DASRequestCreationToolbar;
import bigBang.module.receiptModule.client.userInterface.DASRequestForm;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateDASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateDASRequestViewPresenter.Action;

public class CreateDASRequestView extends View implements CreateDASRequestViewPresenter.Display{

	private DASRequestForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	private DASRequestCreationToolbar toolbar;
	
	public CreateDASRequestView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new DASRequestCreationToolbar(){
			
			@Override
			public void onCreateDASRequest(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateDASRequestViewPresenter.Action>(Action.CREATE_DAS_REQUEST));
			}
			
			@Override
			public void onCancelRequest(){
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateDASRequestViewPresenter.Action>(Action.CANCEL));
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
