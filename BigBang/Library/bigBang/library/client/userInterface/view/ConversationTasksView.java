package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Conversation;
import bigBang.library.client.HasEditableValue;
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

			@Override
			protected void onClose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onRepeat() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onSend() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onReceive() {
				// TODO Auto-generated method stub
				
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
		toolbar.lockAll();
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Conversation> getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> HasValue<T> getParentForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void allowClose(boolean b) {
		toolbar.allowClose(b);
	}

	@Override
	public void allowReceive(boolean b) {
		toolbar.allowReceive(b);
	}

	@Override
	public void allowRepeat(boolean b) {
		toolbar.allowRepeat(b);
	}

	@Override
	public void allowSend(boolean b) {
		toolbar.allowSend(b);
	}

}
