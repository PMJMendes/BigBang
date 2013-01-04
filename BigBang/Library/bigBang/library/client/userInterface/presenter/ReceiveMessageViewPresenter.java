package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Kind;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ReceiveMessageViewPresenter<T extends ProcessBase> implements ViewPresenter{

	protected String ownerId;
	//	protected int counter;

	public static enum Action{
		CANCEL,
		CONFIRM, ON_CLICK_BACK
	}

	public static interface Display<T extends ProcessBase>{
		Widget asWidget();
		HasValue<ProcessBase> getOwnerForm();
		HasEditableValue<Conversation> getForm();
		void setToolbarSaveMode(boolean b);
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

	}

	@Override
	public void setParameters(final HasParameters parameterHolder){

		ownerId = parameterHolder.getParameter("ownerid");

		showOwner(ownerId);

		Message newR = new Message();
		Conversation cv = new Conversation();
		cv.parentDataObjectId = ownerId;
		cv.messages = new Message[1];
		newR.kind = Kind.EMAIL;
		cv.messages[0] = newR;

		view.getForm().setValue(cv);
		view.getForm().setReadOnly(false);
		view.setToolbarSaveMode(true);

	}


	protected Display<T> view;
	protected boolean bound = false;


	public ReceiveMessageViewPresenter(Display<T> view){
		setView((UIObject)view);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setView(UIObject view){
		this.view = (Display<T>)view;
	}
	@Override
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind(){
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){



			@Override
			public void onActionInvoked(
					ActionInvokedEvent<Action> action) {

				switch(action.getAction()){

				case CANCEL:{
					navigateBack();
					break;
				}
				case CONFIRM:{
					onReceiveMessage();
					break;
				}
				case ON_CLICK_BACK:{
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.popFromStackParameter("display");
					navig.removeParameter("externalrequestid");	
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}

				}

			}

		});

	}
	
	protected void onReceiveMessage() {
		if(view.getForm().validate()){
			receive();
		}else{
			onFormValidationFailed();
		}
	}

	protected abstract void receive();
	
	protected abstract void showOwner(String ownerId);

	protected void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o processo pai"), TYPE.ALERT_NOTIFICATION));
		navigateBack();
	}

	public void navigateBack() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("externalrequestid");
		NavigationHistoryManager.getInstance().go(item);		
	}
	
	public void onReceiveMessageSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem recebida com sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
	public void onReceiveMessageFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a mensagem"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}
}
