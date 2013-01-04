package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Session;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class CreateCasualtyViewPresenter implements ViewPresenter {

	public static enum Action {
		SAVE,
		CANCEL
	}

	public static interface Display {
		HasValue<Client> getClientForm();
		HasEditableValue<Casualty> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected ClientProcessBroker broker;

	public CreateCasualtyViewPresenter(Display view) {
		setView((UIObject) view);
		this.broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
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
		String parentId = parameterHolder.getParameter("clientid");

		if(parentId == null || parentId.isEmpty()) {
			onFailure();
		}else{
			showCreateCasualty(parentId);
		}
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<CreateCasualtyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	protected void clearView(){
		view.getClientForm().setValue(null);
		view.getForm().setValue(null);
		view.setSaveModeEnabled(true);
	}

	protected void showCreateCasualty(String clientId){
		broker.getClient(clientId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				Casualty casualty = new Casualty();
				casualty.clientId = response.id;
				casualty.clientNumber = response.clientNumber;
				casualty.clientName = response.name;
				casualty.managerId = Session.getUserId();

				view.setSaveModeEnabled(true);
				view.getForm().setValue(casualty);
				view.getClientForm().setValue(response);

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}

	protected void onSave(){
		if(view.getForm().validate()) {
			broker.createCasualty(view.getForm().getInfo(), new ResponseHandler<Casualty>() {

				@Override
				public void onResponse(Casualty response) {
					onSaveSuccess(response.id);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onSaveFailed();
				}
			});
		}else{
			onFormValidationFailed();
		}
	}

	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveSuccess(String casualtyId){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Sinistro foi Criado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = new NavigationHistoryItem();
		item.setParameter("section", "casualty");
		item.setStackParameter("display");
		item.pushIntoStackParameter("display", "search");
		item.setParameter("casualtyid", casualtyId);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar o Sinistro"), TYPE.ALERT_NOTIFICATION));
		view.setSaveModeEnabled(true);
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}

}
