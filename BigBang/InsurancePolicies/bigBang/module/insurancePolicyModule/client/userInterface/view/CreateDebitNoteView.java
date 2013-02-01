package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DebitNote;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.CreateDebitNoteToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.form.CreateDebitNoteForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateDebitNoteViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.CreateDebitNoteViewPresenter.Action;

public class CreateDebitNoteView extends View implements CreateDebitNoteViewPresenter.Display {

	private CreateDebitNoteForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public CreateDebitNoteView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		this.form = new CreateDebitNoteForm();
		CreateDebitNoteToolbar toolbar = new CreateDebitNoteToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateDebitNoteViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onCreateDebitNote() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateDebitNoteViewPresenter.Action>(Action.CREATE));
			}
		};
		
		wrapper.add(toolbar);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<DebitNote> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

}
