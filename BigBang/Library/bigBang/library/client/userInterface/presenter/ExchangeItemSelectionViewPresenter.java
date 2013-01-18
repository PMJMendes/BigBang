package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Attachment;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView.EmailEntry;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExchangeItemSelectionViewPresenter implements ViewPresenter,
HasValue<Message> {

	public enum Action{
		CANCEL,
		CONFIRM, GET_ALL_EMAILS, REFRESH
	}

	HandlerManager manager;
	protected Display view;
	private boolean bound = false;
	ExchangeServiceAsync service;

	public static interface Display{

		Widget asWidget();

		void addEmailEntry(ExchangeItemStub email);
		HasValueSelectables<ExchangeItemStub> getEmailList();
		void clear();
		HasValue<ExchangeItem> getForm();

		HasValueSelectables<AttachmentStub> getAttachmentList();

		void setAttachments(AttachmentStub[] attachments);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		bigBang.definitions.shared.Message.Attachment[] getChecked();

		void enableGetAll(boolean b);

		void enableRefresh(boolean b);

		void clearList();


	}

	public ExchangeItemSelectionViewPresenter(Display view){
		setView((UIObject)view);
		manager = new HandlerManager(this);
		service = ExchangeService.Util.getInstance();
	}
	@Override
	public void setView(UIObject view) {

		this.view = (Display)view;
	}

	public void bind(){
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
						}

						@Override
						public void onResponseFailure(Throwable caught) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o e-mail."), TYPE.ERROR_NOTIFICATION));
						};
					});

				}

			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<ExchangeItemSelectionViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:{
					ValueChangeEvent.fire(ExchangeItemSelectionViewPresenter.this, null);
					break;
				}
				case CONFIRM:{
					Attachment[] checked = view.getChecked();

					for(int i = 0; i<checked.length; i++){
						if(checked[i].docTypeId == null || checked[i].name == null){
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Todos os anexos a promover para documento devem ter nome e tipo."), TYPE.ALERT_NOTIFICATION));
							return;
						}
					}

					ValueChangeEvent.fire(ExchangeItemSelectionViewPresenter.this, getValue());
					break;
				}
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

	protected void getEmails() {
		view.clear();
		view.clearList();
		view.enableGetAll(false);
		view.enableRefresh(false);
		
		service.getItems(new BigBangAsyncCallback<ExchangeItemStub[]>() {

			@Override
			public void onResponseSuccess(ExchangeItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				view.enableGetAll(true);
				view.enableRefresh(true);

			}
			

			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os e-mails."), TYPE.ERROR_NOTIFICATION));
				view.enableRefresh(true);
			};
		});
		
	}
	protected void getAllEmails() {
		view.clearList();
		view.clear();
		view.enableGetAll(false);
		view.enableRefresh(false);
		
		service.getItemsAll(new BigBangAsyncCallback<ExchangeItemStub[]>() {

			@Override
			public void onResponseSuccess(ExchangeItemStub[] result) {

				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
			}

			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os e-mails."), TYPE.ERROR_NOTIFICATION));
				view.enableRefresh(true);
			};
		});
	}
	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.clear();
		view.enableGetAll(false);
		view.enableRefresh(false);
		getEmails();

	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Message> handler) {
		return manager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		manager.fireEvent(event);		
	}

	@Override
	public Message getValue() {
		Message newMessage = new Message();

		newMessage.emailId = view.getForm().getValue().id;
		newMessage.kind = Message.Kind.EMAIL;
		newMessage.text = null;
		newMessage.attachments = view.getChecked();

		return newMessage;
	}


	@Override
	public void setValue(Message value) {
		return;
	}

	@Override
	public void setValue(Message value, boolean fireEvents) {
		return;
	}

}
