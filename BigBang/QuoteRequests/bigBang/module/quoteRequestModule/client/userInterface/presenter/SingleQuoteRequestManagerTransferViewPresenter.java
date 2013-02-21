package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequest;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SingleQuoteRequestManagerTransferViewPresenter implements ViewPresenter {

	public static enum Action {
		TRANSFER,
		CANCEL
	} 
	
	public static interface Display {
		HasEditableValue<String> getForm();
		
		void allowTransfer(boolean allow);
		
		void registerEventHandler(ActionInvokedEventHandler<Action> action);
		Widget asWidget();
	}

	private Display view;
	private QuoteRequestBroker broker;
	private boolean bound =  false;
	private String currentQuoteRequestId;
	
	public SingleQuoteRequestManagerTransferViewPresenter(Display view){
		this.broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
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
		this.currentQuoteRequestId = parameterHolder.getParameter("quoterequestid");
		this.currentQuoteRequestId = this.currentQuoteRequestId == null ? new String() : this.currentQuoteRequestId;
		
		if(this.currentQuoteRequestId.isEmpty()){
			clearView();
		}else{
			showManagerTransfer(this.currentQuoteRequestId);
		}
	}
	
	private void bind(){
		if(bound){return;}
		
		view.registerEventHandler(new ActionInvokedEventHandler<SingleQuoteRequestManagerTransferViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TRANSFER:
					transferClient(SingleQuoteRequestManagerTransferViewPresenter.this.currentQuoteRequestId, view.getForm().getInfo());
					break;
				case CANCEL:
					onManagerTransferCancelled();
					break;
				}
			}
		});
		
		//APPLICATION-WIDE EVENTS
		
		bound = true;
	}
	
	private void clearView(){
		view.allowTransfer(false);
		view.getForm().setValue(null);
	}
	
	private void showManagerTransfer(String quoteRequestId){
		broker.getQuoteRequest(quoteRequestId, new ResponseHandler<QuoteRequest>() {
			
			@Override
			public void onResponse(QuoteRequest response) {
				//TODO check permissions FJVC
				view.getForm().setValue(Session.getUserId());
				view.getForm().setReadOnly(false);
				view.allowTransfer(true);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientFailed();
			}
		});
	}
	
	private void transferClient(String quoteRequestId, String newManagerId){
		this.broker.createQuoteRequestManagerTransfer(new String[]{quoteRequestId}, newManagerId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				onManagerTransferSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onManagerTransferFailed();
			}
		});
	}
	
	private void onManagerTransferSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Transferência de Gestor criada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	} 
	
	private void onManagerTransferFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onManagerTransferCancelled(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
