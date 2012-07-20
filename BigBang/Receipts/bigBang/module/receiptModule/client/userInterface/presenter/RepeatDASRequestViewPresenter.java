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

public class RepeatDASRequestViewPresenter implements ViewPresenter{

	private DASRequestBroker broker;
	private Display view;
	private boolean bound;

	public enum Action{
		REPEAT_DAS_REQUEST,
		CANCEL
	}

	public interface Display{

		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<DASRequest> getForm();

	}

	public RepeatDASRequestViewPresenter(Display view){
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
		DASRequest request = new DASRequest();
		request.receiptId = parameterHolder.getParameter("receiptid");
		request.id = parameterHolder.getParameter("dasrequestid");
		view.getForm().setValue(request);
	}
	
	public void bind(){
		if(bound){
			return;
		}
		
		view.registerActionHandler(new ActionInvokedEventHandler<RepeatDASRequestViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case REPEAT_DAS_REQUEST:
					onRepeatDASRequest();
					break;
				}
			}
		});
	}

	protected void onRepeatDASRequest() {
		broker.repeatRequest(view.getForm().getInfo(), new ResponseHandler<DASRequest>() {

			@Override
			public void onResponse(DASRequest response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				item.removeParameter("dasrequestid");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Repetição do Pedido de Declaração de Ausência de Sinistro enviada."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao repetir o pedido de Declaração de Ausência de Sinistro."), TYPE.ALERT_NOTIFICATION));
			}
		});
		
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);		
	}

}
