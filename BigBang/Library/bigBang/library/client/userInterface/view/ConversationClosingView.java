package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExternalRequestClosingToolbar;
import bigBang.library.client.userInterface.form.ConversationClosingForm;
import bigBang.library.client.userInterface.presenter.ConversationClosingViewPresenter;
import bigBang.library.client.userInterface.presenter.ConversationClosingViewPresenter.Action;

public class ConversationClosingView extends View implements ConversationClosingViewPresenter.Display {

	private ConversationClosingForm form;
	private ActionInvokedEventHandler<Action> handler;
	
	public ConversationClosingView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		ExternalRequestClosingToolbar toolbar = new ExternalRequestClosingToolbar(){

			@Override
			public void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationClosingViewPresenter.Action>(Action.CLOSE));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationClosingViewPresenter.Action>(Action.CANCEL));
			}
			
		};
		wrapper.add(toolbar);
		
		form = new ConversationClosingForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}
