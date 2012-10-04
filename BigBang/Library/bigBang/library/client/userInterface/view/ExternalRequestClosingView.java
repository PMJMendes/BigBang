package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExternalRequestClosingToolbar;
import bigBang.library.client.userInterface.form.ExternalRequestClosingForm;
import bigBang.library.client.userInterface.presenter.ExternalRequestClosingViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalRequestClosingViewPresenter.Action;

public class ExternalRequestClosingView extends View implements ExternalRequestClosingViewPresenter.Display {

	private ExternalRequestClosingForm form;
	private ActionInvokedEventHandler<Action> handler;
	
	public ExternalRequestClosingView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		ExternalRequestClosingToolbar toolbar = new ExternalRequestClosingToolbar(){

			@Override
			public void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<ExternalRequestClosingViewPresenter.Action>(Action.CLOSE));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ExternalRequestClosingViewPresenter.Action>(Action.CANCEL));
			}
			
		};
		wrapper.add(toolbar);
		
		form = new ExternalRequestClosingForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Closing> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}
