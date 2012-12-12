package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
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
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ConversationClosingViewPresenter implements ViewPresenter {

	public static enum Action {
		CLOSE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected ConversationBroker broker;
	protected Display view;
	private String conversationId;

	public ConversationClosingViewPresenter(Display view) {
		setView((UIObject) view);
		broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
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

	@Override
	public void setParameters(HasParameters parameterHolder) {
		conversationId = parameterHolder.getParameter("conversationid");

		clearView();

		if(conversationId == null || conversationId.isEmpty()) {
			onFailure();
		}
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<ConversationClosingViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case CLOSE:
					onClose();
					break;
				case CANCEL:
					onCancel();
				}
			}
		});

		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
	}
	
	protected void onClose(){
		if(view.getForm().validate()) {
			
			broker.closeConversation(conversationId, view.getForm().getInfo(), new ResponseHandler<Void>() {
				
				@Override
				public void onResponse(Void response) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ConversationProcess.CLOSE, conversationId));
					onCloseSuccess();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onCloseFailed();
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCloseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido foi Encerrado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("conversationid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCloseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Encerrar o Pedido"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Encerrar o Pedido"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
