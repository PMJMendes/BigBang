package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExternalRequestContinuationForm;
import bigBang.library.client.userInterface.ExternalRequestContinuationOperationsToolbar;
import bigBang.library.client.userInterface.presenter.ExternalRequestContinuationViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalRequestContinuationViewPresenter.Action;

public class ExternalRequestContinuationView extends View implements ExternalRequestContinuationViewPresenter.Display {

	private ExternalRequestContinuationForm form;
	private ActionInvokedEventHandler<Action> handler;
	
	public ExternalRequestContinuationView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setWidth("800px");
		
		ExternalRequestContinuationOperationsToolbar toolbar = new ExternalRequestContinuationOperationsToolbar() {
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ExternalRequestContinuationViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			protected void onConfirm() {
				handler.onActionInvoked(new ActionInvokedEvent<ExternalRequestContinuationViewPresenter.Action>(Action.CONFIRM));
			}
		};
		wrapper.add(toolbar);
		
		form = new ExternalRequestContinuationForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Incoming> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}
