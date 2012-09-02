package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubCasualty;
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

public class SubCasualtyMarkForClosingViewPresenter implements ViewPresenter {

	public static enum Action {
		MARK_FOR_CLOSING,
		CANCEL
	} 

	public static interface Display {
		HasEditableValue<String> getForm();

		void allowMarkForClosing(boolean allow);

		void registerEventHandler(ActionInvokedEventHandler<Action> action);
		Widget asWidget();
	}

	private Display view;
	private SubCasualtyDataBroker broker;
	private boolean bound =  false;
	private String currentSubCasualtyId;

	public SubCasualtyMarkForClosingViewPresenter(Display view){
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
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
		this.currentSubCasualtyId = parameterHolder.getParameter("subcasualtyid");
		this.currentSubCasualtyId = this.currentSubCasualtyId == null ? new String() : this.currentSubCasualtyId;

		if(this.currentSubCasualtyId.isEmpty()){
			clearView();
		}else{
			showMarkForClosing(this.currentSubCasualtyId);
		}
	}

	private void bind(){
		if(bound){return;}

		view.registerEventHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case MARK_FOR_CLOSING:
					if(view.getForm().validate()) {
						markForClosing(SubCasualtyMarkForClosingViewPresenter.this.currentSubCasualtyId, view.getForm().getInfo());
					}
					break;
				case CANCEL:
					onMarkForClosingCancelled();
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS

		bound = true;
	}

	private void clearView(){
		view.allowMarkForClosing(false);
		view.getForm().setValue(null);
	}

	private void showMarkForClosing(String subCasualtyId){
		broker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(SubCasualty response) {
				//TODO check permissions FJVC
				view.getForm().setReadOnly(false);
				view.allowMarkForClosing(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientFailed();
			}
		});
	}

	private void markForClosing(String subCasualtyId, String revisorId){
		this.broker.markForClosing(subCasualtyId, revisorId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onMarkForClosingSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onMarkForClosingFailed();
			}
		});
	}

	private void onMarkForClosingSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Marcação de Encerramento efectuada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível Marcar o Encerramento"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	} 

	private void onMarkForClosingFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível Marcar o Encerramento"), TYPE.ALERT_NOTIFICATION));
	}

	private void onMarkForClosingCancelled(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
