package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReopenConversationViewPresenter implements ViewPresenter{
	
	public enum Action {
		REOPEN,
		CANCEL
	}

	public interface Display{

		HasEditableValue<String[]> getForm();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private ConversationBroker broker;
	protected Display view;
	protected boolean bound = false;
	protected String conversationId;
	
	public ReopenConversationViewPresenter(Display view) {
		this.broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
		setView((UIObject)view);
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
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case REOPEN:
					onReopen();
					break;
				case CANCEL:
					onCancel();
					break;
				}				
			}
		});

		bound = true;				
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void onReopen() {
		if(view.getForm().validate()){
			broker.reopenConversation(conversationId, view.getForm().getInfo()[0], Integer.parseInt(view.getForm().getInfo()[1]), new ResponseHandler<Conversation>() {
				
				@Override
				public void onResponse(Conversation response) {
					onSuccess();
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					onReopenFailed();
				}
			});
		}	else{
			onValidationFailed();
		}
	}
	
	private void onValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));						
	}


	protected void onReopenFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível reabrir a Troca de Mensagens"), TYPE.ALERT_NOTIFICATION));		
	}


	private void clearView() {
		view.getForm().setValue(null);		
		
	}

	protected void onSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Reabertura da Troca de Mensagens efectuada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);		
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		
		conversationId = parameterHolder.getParameter("conversationid");
		if(conversationId != null && !conversationId.isEmpty()){
			showReopen();
		}else{
			onReopenFailed();
		}

		
	}

	private void showReopen() {
		this.broker.getConversation(conversationId, new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onReopenFailed();
			}
		});		
	}

}
