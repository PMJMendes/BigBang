package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ConversationTasksOperationsToolbar;
import bigBang.library.client.userInterface.presenter.ConversationTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.ConversationTasksViewPresenter.Action;

public class ConversationTasksView extends View implements ConversationTasksViewPresenter.Display{

	protected ConversationTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<ConversationTasksViewPresenter.Action> handler;
	public ConversationTasksView() {
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new ConversationTasksOperationsToolbar() {
			
			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
		};
		
		wrapper.add(toolbar);
		
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initializeView() {
		return;
	}

}
