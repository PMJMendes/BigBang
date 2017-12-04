package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.Message.Attachment;
import bigBang.definitions.shared.Message.MsgAddress;
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
import bigBang.library.client.userInterface.view.MailItemSelectionView.EmailEntry;
import bigBang.library.interfaces.MailService;
import bigBang.library.interfaces.MailServiceAsync;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.MailItem;
import bigBang.library.shared.MailItemStub;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class EmailReceiverViewPresenter implements ViewPresenter{

	public interface Display{

		Widget asWidget();

		void setTypifiedListItems(List<TipifiedListItem> conversations);

		HasSelectables<ValueSelectable<MailItemStub>> getEmailList();

		HasEditableValue<MailItem> getForm();

		void setAttachments(AttachmentStub[] attachments);

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		void enableGetAll(boolean b);

		void addEmailEntry(MailItemStub email);

		void clear();

		Attachment[] getChecked();

		void clearList();

		HasEditableValue<Message> getMessageForm();

		void enableAllToolbar();

		void enableRefresh(boolean b);

		void removeSelected();

		Conversation getConversationFields();

	}

	public enum Action{
		GET_ALL_EMAILS, CANCEL, CONFIRM, REFRESH

	}

	private Display view;
	private boolean bound = false;
	private ConversationBroker broker;
	MailServiceAsync service;


	public EmailReceiverViewPresenter(Display view) {
		setView((UIObject) view);
		broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
		service = MailService.Util.getInstance();
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
		if(bound){
			return;
		}

		view.getEmailList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {

				if(view.getEmailList().getSelected().size() > 0){
					MailItemStub stub = ((EmailEntry) ((HasValueSelectables<MailItemStub>)event.getSource()).getSelected().toArray()[0]).getValue();
					if ( stub.isFolder )
					{
						if (stub.parentFolderId != null) {
							getBack();
						} else {
							view.clear();
							
							int nrOfMails = view.getEmailList().getAll().size() - 2;
							if (nrOfMails<0) nrOfMails=0;
							
							if (!stub.isMoreMailsButton) {
								view.clearList();
								nrOfMails = 0;
							}
							
							view.enableGetAll(false);
							view.enableRefresh(false);
							
							service.getFolder(stub, nrOfMails, new BigBangAsyncCallback<MailItemStub[]>() {
	
								@Override
								public void onResponseSuccess(MailItemStub[] result) {
									for(int i=0; i<result.length; i++){
										view.addEmailEntry(result[i]);
									}
									view.enableGetAll(true);
									view.enableRefresh(true);
								}
								
								public void onResponseFailure(Throwable caught) {
									EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível o conteúdo da pasta."), TYPE.ERROR_NOTIFICATION));
									view.enableRefresh(true);
								};
							});
						}
					}
					else
					{
						service.getItem(stub.folderId, stub.id, new BigBangAsyncCallback<MailItem>() {

							@Override
							public void onResponseSuccess(MailItem result) {
								view.getForm().setValue(result);
								view.setAttachments(result.attachments);
								view.getMessageForm().setReadOnly(false);
								view.enableAllToolbar();
							}

							@Override
							public void onResponseFailure(Throwable caught) {
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o e-mail."), TYPE.ERROR_NOTIFICATION));
							};
						});
					}
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
					break;
				case REFRESH:
					getEmails();
					break;
				}
			}
		});
		bound = true;		
	}

	protected void getAllEmails() {
		view.clearList();
		view.clear();
		view.enableGetAll(false);
		view.enableRefresh(false);
		service.getItemsAll(new BigBangAsyncCallback<MailItemStub[]>() {

			@Override
			public void onResponseSuccess(MailItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				view.enableGetAll(true);		
				view.enableRefresh(true);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os emails."), TYPE.ALERT_NOTIFICATION));
				super.onResponseFailure(caught);
			}
		});			
	}

	protected void onConfirm() {
		Attachment[] checked = view.getChecked();

		for(int i = 0; i<checked.length; i++){
			if( checked[i].promote && (checked[i].docTypeId == null || checked[i].name == null) ){
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Todos os anexos a promover para documento devem ter nome e tipo."), TYPE.ALERT_NOTIFICATION));
				return;
			}
		}	
		if(view.getMessageForm().validate()){
			MailItem item = view.getForm().getInfo();

			Conversation conversation = view.getConversationFields();
			conversation.startDir = ( view.getForm().getValue().isFromMe ?
					ConversationStub.Direction.OUTGOING : ConversationStub.Direction.INCOMING );
			conversation.messages = new Message[]{view.getMessageForm().getInfo()};

			conversation.id = conversation.messages[0].conversationId;
			
			conversation.messages[0].emailId = item.id;
			conversation.messages[0].folderId = item.folderId;
			conversation.messages[0].attachments = view.getChecked();
			conversation.messages[0].direction = ( view.getForm().getValue().isFromMe ?
					ConversationStub.Direction.OUTGOING : ConversationStub.Direction.INCOMING );
			conversation.messages[0].subject = view.getForm().getValue().subject;
			
			String [] addresses = item.from.split(";");
			if (addresses.length>0) {
				conversation.messages[0].addresses = new Message.MsgAddress[addresses.length];
			}
			for (int u=0; u<addresses.length; u++) {
				Message.MsgAddress tmp = new Message.MsgAddress();
				tmp.address = addresses[u];
				tmp.display = addresses[u];
				conversation.messages[0].addresses[u] = tmp;
				conversation.messages[0].addresses[u].usage = MsgAddress.Usage.FROM;
			}

			if(conversation.id == null){

				broker.createConversationFromEmail(conversation, new ResponseHandler<Conversation>() {

					@Override
					public void onResponse(Conversation response) {
						view.clear();
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem arquivada com sucesso"), TYPE.TRAY_NOTIFICATION));
						removeSelectedItem();
					//	getConversations();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						String error = "";
						for (ResponseError tmp : errors) {
							error = error + tmp.description;
						}
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem. " + error), TYPE.ERROR_NOTIFICATION));
					}
				});

			}
			else{
				broker.receiveMessage(conversation.messages[0], conversation.replylimit, new ResponseHandler<Conversation>() {

					@Override
					public void onResponse(Conversation response) {
						view.clear();
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem arquivada com sucesso"), TYPE.TRAY_NOTIFICATION));
						removeSelectedItem();
					//	getConversations();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						String error = "";
						for (ResponseError tmp : errors) {
							error = error + tmp.description;
						}
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem " + error), TYPE.ALERT_NOTIFICATION));

					}
				});
			}
		}

		else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void removeSelectedItem() {
		view.removeSelected();
	}

	protected void onCancel() {
		view.clear();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.clear();
		view.getMessageForm().setReadOnly(true);
		view.enableGetAll(false);
		view.enableRefresh(false);
//		getConversations();
		getEmails();

	}
	
	protected void getBack() {
		
		view.clear();
		view.clearList();
		view.enableGetAll(false);
		view.enableRefresh(false);

		service.getStoredFolders(new BigBangAsyncCallback<MailItemStub[]>() {

			@Override
			public void onResponseSuccess(MailItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				view.enableGetAll(true);		
				view.enableRefresh(true);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os emails."), TYPE.ALERT_NOTIFICATION));
				view.enableRefresh(true);
				super.onResponseFailure(caught);
			}
		});		
	}

	private void getEmails() {
		view.clear();
		view.clearList();
		view.enableGetAll(false);
		view.enableRefresh(false);

		service.getItems(new BigBangAsyncCallback<MailItemStub[]>() {

			@Override
			public void onResponseSuccess(MailItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				view.enableGetAll(true);		
				view.enableRefresh(true);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os emails."), TYPE.ALERT_NOTIFICATION));
				view.enableRefresh(true);
				super.onResponseFailure(caught);
			}
		});		
	}

	/*private void getConversations() {

		broker.getConversations(BigBangConstants.EntityIds.CONVERSATION, null, new ResponseHandler<List<TipifiedListItem>>() {

			@Override
			public void onResponse(List<TipifiedListItem> response) {
				view.setTypifiedListItems(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter as trocas de mensagens."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}*/

}
