package bigBang.library.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.form.ReopenConversationForm;
import bigBang.library.client.userInterface.form.ReopenConversationToolbar;
import bigBang.library.client.userInterface.presenter.ReopenConversationViewPresenter;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ReopenConversationView extends View implements ReopenConversationViewPresenter.Display{

	private ActionInvokedEventHandler<ReopenConversationViewPresenter.Action> actionHandler;
	ReopenConversationForm form;
	ReopenConversationToolbar toolbar;
	
	public ReopenConversationView() {
	
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		wrapper.setSize("100%", "100%");
		
		toolbar = new ReopenConversationToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReopenConversationViewPresenter.Action>(ReopenConversationViewPresenter.Action.CANCEL));
			}
			
			@Override
			public void onReopen() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReopenConversationViewPresenter.Action>(ReopenConversationViewPresenter.Action.REOPEN));
			}
		};
		
		wrapper.add(toolbar);
		
		form = new ReopenConversationForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
		
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	
	@Override
	public HasEditableValue<String[]> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<ReopenConversationViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}


}
