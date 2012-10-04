package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.CancelInfoOrDocumentRequestToolbar;
import bigBang.library.client.userInterface.form.CancelInfoOrDocumentRequestForm;
import bigBang.library.client.userInterface.presenter.CancelInfoOrDocumentRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.CancelInfoOrDocumentRequestViewPresenter.Action;

public class CancelInfoOrDocumentRequestView extends View implements CancelInfoOrDocumentRequestViewPresenter.Display {

	private CancelInfoOrDocumentRequestForm form;
	private ActionInvokedEventHandler<Action> handler;
	private CancelInfoOrDocumentRequestToolbar toolbar;
	
	public CancelInfoOrDocumentRequestView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new CancelInfoOrDocumentRequestToolbar() {
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CancelInfoOrDocumentRequestViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onConfirm() {
				handler.onActionInvoked(new ActionInvokedEvent<CancelInfoOrDocumentRequestViewPresenter.Action>(Action.CONFIRM));
			}
		};
		wrapper.add(toolbar);
		
		form = new CancelInfoOrDocumentRequestForm();
		wrapper.add(form.getNonScrollableContent());
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Cancellation> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}
