package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Receipt.PaymentInfo;
import bigBang.definitions.shared.Receipt.PaymentInfo.Payment;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class MarkForPaymentViewPresenter implements ViewPresenter {

	public static enum Action {
		ADD_PAYMENT,
		MARK_FOR_PAYMENT,
		CANCEL
	}

	public static interface Display {
		HasValue<Receipt> getForm();
		HasEditableValue<Payment[]> getPaymentsHolder();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void addPayment(Payment payment);
		
		//PERMISSIONS
		void allowMarkForPayment(boolean allow);
		void allowNewPayment(boolean allow);

		Widget asWidget();
	}

	protected Display view;
	protected boolean bound = false;
	protected ReceiptDataBroker broker;

	public MarkForPaymentViewPresenter(Display view){
		broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
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

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String receiptId = parameterHolder.getParameter("receiptId");
		
		clearView();
		
		if(receiptId == null || receiptId.isEmpty()){
			onGetReceiptFailed();
		}else{
			showMarkForPayment(receiptId);
		}
	}
	
	protected void clearView() {
		view.getForm().setValue(null);
		view.getPaymentsHolder().setValue(null);
		view.allowMarkForPayment(false);
		view.allowNewPayment(false);
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<MarkForPaymentViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case ADD_PAYMENT:
					onAddPayment();
					break;
				case MARK_FOR_PAYMENT:
					onMarkForPayment();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	protected void showMarkForPayment(String receiptId) {
		broker.getReceipt(receiptId, new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				view.allowMarkForPayment(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ReceiptProcess.MARK_FOR_PAYMENT));
				view.allowNewPayment(true);
				view.getForm().setValue(response);
				view.getPaymentsHolder().setValue(null);
				onAddPayment();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetReceiptFailed();
			}
		});
	}
	
	protected void onAddPayment(){
		Payment payment = new Payment();
		view.addPayment(payment);
	}
	
	protected void onMarkForPayment(){
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.receiptId = view.getForm().getValue().id;
		paymentInfo.payments = view.getPaymentsHolder().getInfo();
		
		broker.markPayed(paymentInfo, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				onMarkForPaymentSuccess();
				if(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ReceiptProcess.CREATE_DAS_REQUEST)){
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("Pagamento posterior à data limite", "Será necessária uma DAS"), TYPE.INFO_TRAY_NOTIFICATION));
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onMarkForPaymentFailed();
			}
		});
	}
	
	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onGetReceiptFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível efectuar a Cobrança do Recibo"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onMarkForPaymentFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível efectuar a Cobrança do Recibo"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onMarkForPaymentSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A cobrança do Recibo foi efectuada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
