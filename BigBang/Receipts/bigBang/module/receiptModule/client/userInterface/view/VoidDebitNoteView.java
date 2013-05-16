package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.VoidDebitNoteToolbar;
import bigBang.module.receiptModule.client.userInterface.form.VoidDebitNoteForm;
import bigBang.module.receiptModule.client.userInterface.presenter.VoidDebitNoteViewPresenter;

import com.google.gwt.user.client.ui.VerticalPanel;

public class VoidDebitNoteView extends View implements VoidDebitNoteViewPresenter.Display{

	private VoidDebitNoteToolbar toolbar;
	private ActionInvokedEventHandler<VoidDebitNoteViewPresenter.Action> actionHandler;
	private VoidDebitNoteForm form;
	
	public VoidDebitNoteView() {
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new VoidDebitNoteToolbar() {
			
			@Override
			protected void onVoidDebitNote() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<VoidDebitNoteViewPresenter.Action>(VoidDebitNoteViewPresenter.Action.CONFIRM));
				
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<VoidDebitNoteViewPresenter.Action>(VoidDebitNoteViewPresenter.Action.CANCEL));
				
			}
		};
		
		form = new VoidDebitNoteForm();
		wrapper.setSize("100%", "100%");
		wrapper.add(toolbar);
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");	}
	
	@Override
	protected void initializeView() {
		return;		
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<VoidDebitNoteViewPresenter.Action> handler) {
		actionHandler = handler;
		
	}

	@Override
	public HasEditableValue<Receipt.ReturnMessage> getForm() {
		return form;
	}
}
