package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.SignatureRequest.Cancellation;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.SignatureRequestCancellationForm;
import bigBang.module.receiptModule.client.userInterface.SignatureRequestCancellationToolbar;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CancelSignatureRequestViewPresenter.Action;

public class CancelSignatureRequestView extends View implements CancelSignatureRequestViewPresenter.Display{

	private SignatureRequestCancellationToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	private SignatureRequestCancellationForm form;
	
	public CancelSignatureRequestView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new SignatureRequestCancellationToolbar() {
			
			@Override
			public void onCancelSignatureRequestRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CancelSignatureRequestViewPresenter.Action>(Action.CANCEL_SIGNATURE_REQUEST));
				
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CancelSignatureRequestViewPresenter.Action>(Action.CANCEL));
				
			}
		};
		
		form = new SignatureRequestCancellationForm();
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
	public HasEditableValue<Cancellation> getForm() {
		return form;
	}

	@Override
	protected void initializeView() {
		return;
	}

}
