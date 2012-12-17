package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.Message.AttachmentUpgrade;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView.EmailEntry;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class EmailViewPresenter implements ViewPresenter{

	public interface Display{

		Widget asWidget();

		void setTypifiedListItems(List<TipifiedListItem> conversations);

		HasSelectables<ValueSelectable<ExchangeItemStub>> getEmailList();

		HasEditableValue<ExchangeItem> getForm();

		void setAttachments(AttachmentStub[] attachments);

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		void enableGetAll(boolean b);

		void addEmailEntry(ExchangeItemStub email);

		void clear();

		AttachmentUpgrade[] getChecked();

		Integer getReplyLimit();

		String getParentType();

		String getParentId();

		void clearList();

		HasEditableValue<Message> getMessageForm();

		void enableAllToolbar();

		String getRequestType();

	}

	public enum Action{
		GET_ALL_EMAILS, CANCEL, CONFIRM

	}

	private Display view;
	private boolean bound = false;
	private ConversationBroker broker;
	ExchangeServiceAsync service;


	public EmailViewPresenter(Display view) {
		setView((UIObject) view);
		broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
		service = ExchangeService.Util.getInstance();
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound ){
			return;
		}

		view.getEmailList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {

				if(view.getEmailList().getSelected().size() > 0){
					service.getItem(((EmailEntry) ((HasValueSelectables<ExchangeItemStub>)event.getSource()).getSelected().toArray()[0]).getValue().id, new BigBangAsyncCallback<ExchangeItem>() {



						@Override
						public void onResponseSuccess(ExchangeItem result) {
							view.getForm().setValue(result);
							view.setAttachments(result.attachments);
							view.getMessageForm().setReadOnly(false);
							view.enableAllToolbar();
						}

						@Override
						public void onResponseFailure(Throwable caught) {
							super.onResponseFailure(caught);
						};
					});

				}

			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CONFIRM:
					onConfirm();
					break;
				case GET_ALL_EMAILS:
					getAllEmails();
				}
			}
		});
		bound = true;		
	}

	protected void getAllEmails() {
		view.clearList();
		service.getItemsAll(new BigBangAsyncCallback<ExchangeItemStub[]>() {

			@Override
			public void onResponseSuccess(ExchangeItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				view.enableGetAll(true);				
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os emails."), TYPE.ALERT_NOTIFICATION));
				super.onResponseFailure(caught);
			}
		});			}

	protected void onConfirm() {
		AttachmentUpgrade[] checked = view.getChecked();

		for(int i = 0; i<checked.length; i++){
			if(checked[i].docTypeId == null || checked[i].name == null){
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Todos os anexos a promover para documento devem ter nome e tipo."), TYPE.ALERT_NOTIFICATION));
				return;
			}
		}	
		if(view.getMessageForm().validate()){
			ExchangeItem item = view.getForm().getInfo();

			Conversation conversation = new Conversation();
			conversation.messages = new Message[]{view.getMessageForm().getInfo()};
			conversation.replylimit = view.getReplyLimit();

			conversation.id = conversation.messages[0].conversationId;

			conversation.messages[0].emailId = item.id;
			conversation.parentDataObjectId = view.getParentId();
			conversation.parentDataTypeId =  view.getParentType();
			conversation.requestTypeId = view.getRequestType();

			if(conversation.id == null){

				broker.createConversationFromEmail(conversation, new ResponseHandler<Conversation>() {

					@Override
					public void onResponse(Conversation response) {
						view.clear();
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem recebida com sucesso"), TYPE.TRAY_NOTIFICATION));
						view.clearList();
						getEmails();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
					}
				});

			}
			else{
				broker.receiveMessage(conversation.messages[0], view.getReplyLimit(), new ResponseHandler<Conversation>() {

					@Override
					public void onResponse(Conversation response) {
						view.clear();
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem recebida com sucesso"), TYPE.TRAY_NOTIFICATION));
						view.clearList();
						getEmails();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a mensagem"), TYPE.ALERT_NOTIFICATION));

					}
				});
			}
		}

		else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onCancel() {
		view.clear();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.clear();
		view.getMessageForm().setReadOnly(true);
		view.enableGetAll(false);
		getConversations();
		getEmails();

	}

	private void getEmails() {
		view.clearList();
		service.getItems(new BigBangAsyncCallback<ExchangeItemStub[]>() {

			@Override
			public void onResponseSuccess(ExchangeItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				view.enableGetAll(true);				
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os emails."), TYPE.ALERT_NOTIFICATION));
				super.onResponseFailure(caught);
			}
		});		
	}

	private void getConversations() {

		broker.getConversations(BigBangConstants.EntityIds.CONVERSATION, null, new ResponseHandler<List<TipifiedListItem>>() {

			@Override
			public void onResponse(List<TipifiedListItem> response) {
				view.setTypifiedListItems(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter as trocas de conversas."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

}
