package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.Checkable;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Session;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;


public class MassSignatureRequestViewPresenter implements ViewPresenter{

	public interface Display{

		void addReceiptToSelectedList(ReceiptStub value);
		void removeReceiptFromSelectedList(String id);
		HasCheckables getCheckableSelectedList();
		HasEditableValue<Receipt> getReceiptForm();
		
		HasValueSelectables<ReceiptStub> getMainList();
		HasValueSelectables<ReceiptStub> getSelectedList();
		HasCheckables getCheckableMainList();
		HasEditableValue<Integer> getSignatureRequestForm();

		void refreshMainList();

		void markAllForCheck();
		void markForCheck(String id);
		void markForUncheck(String id);

		void removeAllReceiptsFromSelectedList();
		Widget asWidget();
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		void allowSignatureRequest(boolean b);
		void setManagerFilterValue(String value);

	}

	public enum Action{
		SELECT_ALL, REQUEST_SIGNATURE, CLEAR

	}

	private Display view;
	private boolean bound = false;
	protected ReceiptDataBroker broker;

	public MassSignatureRequestViewPresenter(Display view){
		setView((UIObject) view);
		broker = (ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
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

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CLEAR:
					view.removeAllReceiptsFromSelectedList();
					break;
				case REQUEST_SIGNATURE:
					markReceipts(view.getSelectedList().getAll());
					break;
				case SELECT_ALL:
					view.markAllForCheck();
					break;
				}

			}
		});
		view.getCheckableMainList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();

				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> entry = (ValueSelectable<ReceiptStub>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
					view.addReceiptToSelectedList(entry.getValue());
				}else{
					view.markForUncheck(id);
					view.removeReceiptFromSelectedList(id);
				}

			}
		});
		view.getCheckableSelectedList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();

				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> entry = (ValueSelectable<ReceiptStub>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
				}else{
					view.markForUncheck(id);
					view.removeReceiptFromSelectedList(id);
				}

			}
		});
		view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selectable = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				
				if(selectable!= null){
					view.getSelectedList().clearSelection();
					broker.getReceipt(selectable.getValue().id, new ResponseHandler<Receipt>() {
						
						@Override
						public void onResponse(Receipt response) {
							view.getReceiptForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Recibo seleccionado"), TYPE.ALERT_NOTIFICATION));
						}
					});
				}
				
			}
		});
		view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selectable = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				
				if(selectable!= null){
					view.getMainList().clearSelection();
					broker.getReceipt(selectable.getValue().id, new ResponseHandler<Receipt>() {
						
						@Override
						public void onResponse(Receipt response) {
							view.getReceiptForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Recibo seleccionado"), TYPE.ALERT_NOTIFICATION));
						}
					});
				}
				
			}
		});
		
		bound = true;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		showMassMarkReceiptScreen();
		view.setManagerFilterValue(Session.getUserId());
	}



	private void showMassMarkReceiptScreen() {
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					view.allowSignatureRequest(true);
					view.getReceiptForm().setValue(null);
				}else{
					view.allowSignatureRequest(false);
					onUserLacksPermission();
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
	}

	protected void onUserLacksPermission() {
//		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
	}

	private void clearView() {
		view.getMainList().clearSelection();
		view.removeAllReceiptsFromSelectedList();
		view.getReceiptForm().setValue(null);
		view.getSignatureRequestForm().setValue(null);
	}

	public void markReceipts(Collection<ValueSelectable<ReceiptStub>> collection){
		String[] receiptIds = new String[collection.size()];
		
		if(receiptIds.length == 0){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Nenhum recibo seleccionado"), TYPE.ALERT_NOTIFICATION));				
			return;
		}
		
		int i = 0;
		for(ValueSelectable<ReceiptStub> r : collection){
			receiptIds[i] = r.getValue().id;
			i++;
		}
		
		int replyLimit = view.getSignatureRequestForm().getInfo() == null ? -1 : view.getSignatureRequestForm().getInfo();
		
		broker.massCreateSignatureRequest(receiptIds, replyLimit, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onCreateSignatureRequestSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateSignatureRequestFailed();
			}
		});

	}

	protected void checkUserPermission(final ResponseHandler<Boolean> handler) {
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.RECEIPT, BigBangConstants.OperationIds.ReceiptProcess.NOT_PAID_INDICATION, new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(false);
			}
		});
	}

	protected void onCreateSignatureRequestSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Os Pedidos de Assinatura foram Criados com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}
	
	protected void onCreateSignatureRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Criar os Pedidos de Assinatura"), TYPE.ALERT_NOTIFICATION));
	}

}
