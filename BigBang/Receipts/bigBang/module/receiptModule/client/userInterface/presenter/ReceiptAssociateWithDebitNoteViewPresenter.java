package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
public class ReceiptAssociateWithDebitNoteViewPresenter implements ViewPresenter{

	public enum Action{
		CONFIRM,
		CANCEL
	}

	private boolean bound = false;
	private ReceiptDataBroker broker;
	private String receiptId;
	private Display view;

	public interface Display{

		Widget asWidget();

		HasValueSelectables<DebitNote> getList();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		void clear();

		void addToList(DebitNote[] debitNotes);

	}

	public ReceiptAssociateWithDebitNoteViewPresenter(Display view){
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

	private void bind(){
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<ReceiptAssociateWithDebitNoteViewPresenter.Action>() {

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


		bound = true;
	}

	protected void onConfirm() {


		for(ValueSelectable<DebitNote> note : view.getList().getSelected()){
			broker.associateWithDebitNote(receiptId, note.getValue().id, new ResponseHandler<Receipt>() {

				@Override
				public void onResponse(Receipt response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A nota de débito foi associada com sucesso ao recibo"), TYPE.TRAY_NOTIFICATION));
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onFail();	
				}
			});
		}

	}
	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	@Override
	public void setParameters(HasParameters parameterHolder) {
		receiptId = parameterHolder.getParameter("receiptid");
		view.clear();
		if(receiptId != null && !receiptId.isEmpty()){

			broker.getRelevantDebitNotes(receiptId, new ResponseHandler<DebitNote[]>() {

				@Override
				public void onResponse(DebitNote[] response) {
					view.addToList(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onFail();
				}
			});
		}
		else{
			onFail();
			onCancel();
		}

	}
	private void onFail() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível associar a nota de débito"), TYPE.ALERT_NOTIFICATION));

	}

}
