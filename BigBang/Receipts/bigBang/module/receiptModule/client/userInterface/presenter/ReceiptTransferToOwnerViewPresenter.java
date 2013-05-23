package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.OwnerRef;
import bigBang.definitions.shared.Receipt;
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

public class ReceiptTransferToOwnerViewPresenter implements ViewPresenter{

	public static enum Action{
		CONFIRM,
		CANCEL
	}

	public interface Display{
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<OwnerRef> getForm();
		Widget asWidget();
	}

	private boolean bound = false;
	private ReceiptDataBroker broker;
	private String receiptId;
	private Display view;

	public ReceiptTransferToOwnerViewPresenter(
			Display view) {
		broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
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

		view.registerActionHandler(new ActionInvokedEventHandler<ReceiptTransferToOwnerViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onConfirmTransfer();
					break;
				case CANCEL:
					onCancelTransfer();
					break;
				}

			}
		});

		bound = true;

	}

	protected void onCancelTransfer() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void clearView(){
		view.getForm().setValue(null);
	}

	protected void onConfirmTransfer() {
		if(view.getForm().validate()) {
			OwnerRef owner = view.getForm().getInfo();
			broker.transferToOwner(receiptId, owner, new ResponseHandler<Receipt>() {

				@Override
				public void onResponse(Receipt response) {
					onTransferToOwnerSuccess();

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onTransferToOwnerFailed();				
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		receiptId = parameterHolder.getParameter("receiptid");
		if(receiptId != null && !receiptId.isEmpty()){
			clearView();
		}
		else{
			onTransferToOwnerFailed();
			onCancelTransfer();
		}
	}

	protected void onTransferToOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível transferir o recibo"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onTransferToOwnerSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O recibo foi transferido com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	} 

}
