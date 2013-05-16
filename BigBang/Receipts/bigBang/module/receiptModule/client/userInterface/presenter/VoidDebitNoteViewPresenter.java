package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class VoidDebitNoteViewPresenter implements ViewPresenter{

	private ReceiptDataBroker broker;
	private Display view;
	private boolean bound;

	public enum Action {
		CONFIRM, CANCEL
	}

	public interface Display {
		Widget asWidget();

		HasEditableValue<ReturnMessage> getForm();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
	}

	public VoidDebitNoteViewPresenter(Display view) {
		broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
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

	private void bind() {
		if(bound){
			return;
		}
		
		view.registerActionHandler(new ActionInvokedEventHandler<VoidDebitNoteViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CONFIRM:
					onConfirm();
					break;	
				}
			}				
		});
	}

	protected void onConfirm() {
		if(view.getForm().validate()) {
			broker.voidDebitNote(view.getForm().getInfo(), new ResponseHandler<Receipt>() {

				@Override
				public void onResponse(Receipt response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Nota de débito anulada com sucesso."), TYPE.TRAY_NOTIFICATION));				
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível anular a nota de débito."), TYPE.ALERT_NOTIFICATION));

				}
			});	
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}		
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);						
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.getForm().setValue(null);
		Receipt.ReturnMessage toReturn = new ReturnMessage();
		toReturn.receiptId = parameterHolder.getParameter("receiptid");
		view.getForm().setValue(toReturn);
	}

}
