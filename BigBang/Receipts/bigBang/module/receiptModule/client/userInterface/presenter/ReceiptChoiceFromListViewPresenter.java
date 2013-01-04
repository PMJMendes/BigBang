package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel.Entry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ReceiptChoiceFromListViewPresenter implements ViewPresenter{
	
	private Display view; 
	private ReceiptDataBroker broker;
	private boolean bound = false;
	
	public static enum Action{
		CHOSEN_RECEIPT,
		CANCEL, LIST_CHANGED, NEW_RECEIPT
	}
	
	public ReceiptChoiceFromListViewPresenter(Display view) {
		
		this.broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		setView((UIObject)view);
		
	}
	
	public interface Display{
		HasEditableValue<Receipt> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void clear();
		HasValueSelectables<ReceiptStub> getList();
		void fillList(Collection<ReceiptStub> collection);
		void enableConfirm(boolean b);
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
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ReceiptChoiceFromListViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CHOSEN_RECEIPT:
					getSelectedReceipt();
					break;
				case LIST_CHANGED:
					listChanged();
					break;
				case NEW_RECEIPT:
					onNewReceipt();
					break;
				}
			}
		});
	}

	protected void listChanged() {
		view.enableConfirm(true);
		Collection<ValueSelectable<ReceiptStub>> selected = view.getList().getSelected();
		
		if(selected.size() > 0){

			broker.getReceipt(((ReceiptStub)((Entry)selected.toArray()[0]).getValue()).id,  new ResponseHandler<Receipt>() {
				
				@Override
				public void onResponse(Receipt response) {
					view.getForm().setValue(response);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o recibo."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		
	}

	protected abstract void onCancel();
	
	protected abstract void onNewReceipt();

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.clear();
		view.enableConfirm(false);
	}
	
	public abstract void setReceipts(Collection<ReceiptStub> stubs);
	public abstract void getSelectedReceipt();
	public void fillList(Collection<ReceiptStub> collection){
	
		view.fillList(collection);
		
	}

}
