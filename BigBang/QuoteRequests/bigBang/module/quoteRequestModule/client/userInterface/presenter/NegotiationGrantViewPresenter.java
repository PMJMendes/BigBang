package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.User;
import bigBang.definitions.shared.Negotiation.Grant;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.interfaces.ContactsService;

public class NegotiationGrantViewPresenter implements ViewPresenter{

	private NegotiationBroker broker;
	private Display view;
	private boolean bound;
	private UserBroker userBroker;

	public interface Display{
		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		HasEditableValue<Grant> getForm();

		void clear();

		void setAvailableContacts(Contact[] result);

		void setUserList(String[] displayNames);
	}

	public enum Action{
		CANCEL,
		GRANT_NEGOTIATION
	}

	public NegotiationGrantViewPresenter(Display view) {
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

	private void bind() {

		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<NegotiationGrantViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;

				case GRANT_NEGOTIATION:
					onGrantNegotiation();
					break;
				}

			}
		});


		bound = true;
	}

	protected void onGrantNegotiation() {
		broker.grantNegotiation(view.getForm().getInfo(), new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação adjudicada com sucesso."), TYPE.TRAY_NOTIFICATION));

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível adjudicar a negociação."), TYPE.ALERT_NOTIFICATION));

			}
		});


	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.clear();
		Grant grant = new Grant();
		grant.negotiationId = parameterHolder.getParameter("negotiationid");
		setContactsForNegotiation(grant.negotiationId);
		setUserList();
		view.getForm().setValue(grant);

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

}
