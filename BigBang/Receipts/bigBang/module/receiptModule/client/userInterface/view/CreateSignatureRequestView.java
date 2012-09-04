package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.receiptModule.client.userInterface.SignatureRequestCreationToolbar;
import bigBang.module.receiptModule.client.userInterface.SignatureRequestForm;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateSignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.CreateSignatureRequestViewPresenter.Action;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;

public class CreateSignatureRequestView extends View implements CreateSignatureRequestViewPresenter.Display{

	private SignatureRequestForm form;
	private ActionInvokedEventHandler<Action> actionHandler;
	private SignatureRequestCreationToolbar toolbar;
	
	public CreateSignatureRequestView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new SignatureRequestCreationToolbar() {
			
			@Override
			public void onCreateSignatureRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateSignatureRequestViewPresenter.Action>(Action.CREATE_SIGNATURE_REQUEST));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateSignatureRequestViewPresenter.Action>(Action.CANCEL));
				
			}
		};
		
		form = new SignatureRequestForm();
		
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
	public HasEditableValue<SignatureRequest> getForm() {
		return form;
	}

	@Override
	protected void initializeView() {
		return;		
	}

}
