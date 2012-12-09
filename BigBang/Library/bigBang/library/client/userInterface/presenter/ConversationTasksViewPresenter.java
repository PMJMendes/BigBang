package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;

public class ConversationTasksViewPresenter implements ViewPresenter, HasOperationPermissions{

	public static enum Action {
		GO_TO_PROCESS
	}
	
	public static interface Display{
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected ConversationBroker broker;
	protected Display view;
	String conversationId;
	
	public ConversationTasksViewPresenter(Display view) {
		setView((UIObject) view);
		this.broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
	}
	@Override
	public void setPermittedOperations(String[] operationIds) {
		//TODO
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ConversationTasksViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case GO_TO_PROCESS:
					NavigationHistoryManager.getInstance().NavigateToProcess(BigBangConstants.EntityIds.CONVERSATION,conversationId);
					break;
				}
			}
		});
		
		bound = true;
	}
	@Override
	public void setParameters(HasParameters parameterHolder) {
		conversationId = parameterHolder.getParameter("id");
	}
	
}
