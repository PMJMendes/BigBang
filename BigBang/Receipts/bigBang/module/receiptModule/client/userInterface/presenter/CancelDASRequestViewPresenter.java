package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.DASRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DASRequest.Cancellation;
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

public class CancelDASRequestViewPresenter implements ViewPresenter{

	private DASRequestBroker broker;
	private Display view;
	private boolean bound;

	public enum Action{
		CANCEL_DAS_REQUEST,
		CANCEL
	}

	public interface Display{

		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<DASRequest.Cancellation> getForm();

	}

	public CancelDASRequestViewPresenter(Display view){
		broker = (DASRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DAS_REQUEST);
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

		view.getForm().setValue(null);
		Cancellation toCancel = new Cancellation();
		toCancel.requestId = parameterHolder.getParameter("dasrequestid");
		view.getForm().setValue(toCancel);

	}

	public void bind(){
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CANCEL_DAS_REQUEST:
					onCancelDASRequest();
					break;	
				}
			}
		});

		bound = true;
	}

	protected void onCancelDASRequest() {
		if(view.getForm().validate()) {
			broker.cancelRequest(view.getForm().getInfo(), new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "DAS cancelada com sucesso."), TYPE.TRAY_NOTIFICATION));				
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível cancelar a DAS."), TYPE.ALERT_NOTIFICATION));

				}
			});	
		}
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);				
	}


}
