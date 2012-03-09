package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;
import java.util.concurrent.CancellationException;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.User;
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.interfaces.ContactsService;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class NegotiationCancellationViewPresenter implements ViewPresenter{

	private NegotiationBroker broker;
	private UserBroker userBroker;
	private Display view;
	private boolean bound;

	public enum Action{
		CANCEL_NEGOTIATION,
		CANCEL
	}

	public interface Display{

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		HasEditableValue<Cancellation> getForm();

		void setAvailableContacts(Contact[] result);

		void setUserList(String[] displayNames);

		void clear();

	}

	public NegotiationCancellationViewPresenter(Display view){
		this.broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		userBroker = (UserBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.USER);
		setView((UIObject)view);
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

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.clear();
		Cancellation toCancel = new Cancellation();
		toCancel.negotiationId = parameterHolder.getParameter("negotiationid");
		view.getForm().setValue(toCancel);
		setContactsForNegotiation(toCancel.negotiationId);
		setUserList();
	}


	private void setUserList() {
		userBroker.getUsers(new ResponseHandler<User[]>() {
			
			@Override
			public void onResponse(User[] response) {
				String[] displayNames = new String[response.length];
				
				for(int i = 0; i<response.length; i++){
					displayNames[i] = response[i].name;
				}
				view.setUserList(displayNames);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não vai ser possível fazer forward da mensagem para outros utilizadores do sistema."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
		
	}

	private void setContactsForNegotiation(String negotiationId) {
		ContactsService.Util.getInstance().getFlatEmails(negotiationId, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onResponseSuccess(Contact[] result) {
				view.setAvailableContacts(result);
			}
		});
	}

	public void bind(){
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<NegotiationCancellationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){
				case CANCEL:{
					onCancel();
					break;
				}
				case CANCEL_NEGOTIATION:{
					onCancelNegotiation();
					break;
				}
				}

			}


		});

	}

	protected void onCancelNegotiation() {
		broker.cancelNegotiation(view.getForm().getInfo(), new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação cancelada com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível cancelar a negociação."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}



}
