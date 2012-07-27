package bigBang.module.receiptModule.client.userInterface.presenter;

import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

public class SerialMarkForPaymentViewPresenter extends
		MarkForPaymentViewPresenter {

	public static interface Display extends MarkForPaymentViewPresenter.Display {
		HasValueSelectables<ReceiptStub> getList();
		void removeFromList(ValueSelectable<ReceiptStub> entry);
	}
	
	public SerialMarkForPaymentViewPresenter(Display view) {
		super(view);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
	}
	
	@Override
	protected void clearView() {
		super.clearView();
		getView().getList().clearSelection();
	}
	
	@Override
	protected void bind() {
		super.bind();
		getView().getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				ReceiptStub receipt = selected == null ? null : (ReceiptStub) selected.getValue();
				
				if(receipt == null) {
					clearView();
				}else{
					showMarkForPayment(receipt.id);
				}
			}
		});
	}

	protected Display getView(){
		return (Display) this.view;
	}
	
	@Override
	protected void onMarkForPaymentSuccess() {
		String receiptId = view.getForm().getValue().id;
		for(ValueSelectable<ReceiptStub> selectable : getView().getList().getAll()) {
			if(selectable.getValue().id.equalsIgnoreCase(receiptId)){
				selectable.setSelected(false, true);
				getView().removeFromList(selectable);
			}
		}
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A cobrança do Recibo foi efectuada com Sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	@Override
	protected void onGetReceiptFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível efectuar a Cobrança do Recibo"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	@Override
	protected void onMarkForPaymentFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível efectuar a Cobrança do Recibo"), TYPE.ALERT_NOTIFICATION));
	}
	
}
