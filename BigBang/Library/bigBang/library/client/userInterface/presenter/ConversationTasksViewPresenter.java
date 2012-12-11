package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;

public class ConversationTasksViewPresenter implements ViewPresenter, HasOperationPermissions{

	public static enum Action {
		GO_TO_PROCESS
	}
	
	public static interface Display{
		HasEditableValue<Conversation> getForm();
		<T> HasValue<T> getParentForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		Widget asWidget();
		void allowClose(boolean b);
		void allowReceive(boolean b);
		void allowRepeat(boolean b);
		void allowSend(boolean b);
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
		view.clearAllowedPermissions();
		for(String opid : operationIds){
			if(BigBangConstants.OperationIds.ConversationProcess.CLOSE.equalsIgnoreCase(opid)){
				view.allowClose(true);
			}else if(BigBangConstants.OperationIds.ConversationProcess.RECEIVE.equalsIgnoreCase(opid)){
				view.allowReceive(true);
			}else if(BigBangConstants.OperationIds.ConversationProcess.REPEAT.equalsIgnoreCase(opid)){
				view.allowRepeat(true);
			}else if(BigBangConstants.OperationIds.ConversationProcess.SEND.equalsIgnoreCase(opid)){
				view.allowSend(true);
			}
		}
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
		
		if(conversationId != null){
			getConversation();
		}
	}
	
	private void getConversation() {
		broker.getConversation(conversationId, new ResponseHandler<Conversation>() {
			
			@Override
			public void onResponse(Conversation response) {

			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a troca de mensagens"), TYPE.ALERT_NOTIFICATION));

			}
		});
	}
	
}
