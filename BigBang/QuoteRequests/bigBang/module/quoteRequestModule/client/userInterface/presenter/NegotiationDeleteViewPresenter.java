package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Negotiation.Deletion;
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

public class NegotiationDeleteViewPresenter implements ViewPresenter {
	
	public static enum Action{
		DELETE,
		CANCEL
	}

	private NegotiationBroker broker;
	private Display view;
	private boolean bound;
	
	
	public NegotiationDeleteViewPresenter(Display view){
		this.broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		setView((UIObject)view);
	}
	
	
	public static interface Display {
		HasEditableValue<Deletion> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
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
		
		
		Deletion toDelete = new Deletion();
		toDelete.negotiationId = parameterHolder.getParameter("negotiationid");
		view.getForm().setValue(toDelete);
		
		
	}
	
	private void bind(){
		
		view.registerActionHandler(new ActionInvokedEventHandler<NegotiationDeleteViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){
				case DELETE:{
					onDeleteNegotiation();
					break;
				}
				case CANCEL:{
					onCancel();
					break;
				}
				}
				
			}
		
		
		});
		
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDeleteNegotiation() {
		
		broker.removeNegotiation((Deletion) view.getForm(), new ResponseHandler<String>() {

			@Override
			public void onResponse(String response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				item.popFromStackParameter("display");
				item.removeParameter("negotiationid");
				item.removeParameter("ownertypeid");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação eliminada com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a negociação."), TYPE.ALERT_NOTIFICATION));
				
			}
		
		
		});
		
		
	}
	
	

}
