package bigBang.module.receiptModule.client.userInterface.presenter;


import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Receipt.ReturnMessage;
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

public class ReceiptReturnViewPresenter implements ViewPresenter{
	
	public enum Action{
		CONFIRM,
		CANCEL
	}
	
	public interface Display{
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<ReturnMessage> getForm();
		Widget asWidget();
		
		
	}
	
	private boolean bound = false;
	private ReceiptProcessDataBroker broker;
	private Display view;
	private String receiptId;
	
	public ReceiptReturnViewPresenter(Display view){
		broker = (ReceiptProcessDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		setView((UIObject) view);
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
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ReceiptReturnViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				switch(action.getAction()){
				case CONFIRM:
					onConfirm();
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

	protected void onConfirm() {
		ReturnMessage message = view.getForm().getInfo();
		
		broker.setForReturn(message, new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				onSuccess();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onFail();
			}
		});
		
	}

	protected void onFail() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível marcar o recibo para devolução"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O recibo foi marcado para devolução com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		receiptId = parameterHolder.getParameter("receiptid");
		if(receiptId != null && !receiptId.isEmpty()){
			clearView();
		}
		else{
			onFail();
			onCancel();
		}
	}
	
	protected void clearView(){
		
		ReturnMessage message = new ReturnMessage();
		message.receiptId = receiptId;
		view.getForm().setValue(message);
	}

}
