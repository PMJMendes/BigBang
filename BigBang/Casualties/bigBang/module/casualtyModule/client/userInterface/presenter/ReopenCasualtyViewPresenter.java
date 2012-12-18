package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
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

public class ReopenCasualtyViewPresenter  implements ViewPresenter{

	public enum Action{
		REOPEN,
		CANCEL
	}

	public interface Display{
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected CasualtyDataBroker broker;
	protected Display view;
	protected boolean bound = false;
	protected String casualtyId;

	public ReopenCasualtyViewPresenter(Display view) {
		this.broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
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
					onReject();
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


	private void onValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));						
	}


	protected void onReject() {
		if(view.getForm().validate()){


			broker.reOpen(casualtyId, view.getForm().getInfo(), new ResponseHandler<Casualty>(){

				@Override
				public void onResponse(Casualty response) {
					onSuccess();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onReopenFailed();
				}

			});
		}
		else{
			onValidationFailed();
		}
	}


	protected void onSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Reabertura do Sinistro efectuada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);		
	}


	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		casualtyId = parameterHolder.getParameter("casualtyid");
		if(casualtyId != null && !casualtyId.isEmpty()){
			showReopen();
		}else{
			onReopenFailed();
		}

	}


	private void onReopenFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível reabrir o Sinistro"), TYPE.ALERT_NOTIFICATION));
	}


	private void showReopen() {
		this.broker.getCasualty(casualtyId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onReopenFailed();
			}
		});
	}


	private void clearView() {
		view.getForm().setValue(null);		
	}

}
