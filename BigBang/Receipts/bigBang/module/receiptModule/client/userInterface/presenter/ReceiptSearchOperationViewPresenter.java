package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptSearchOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE, TRANSFER_TO_POLICY, ASSOCIATE_WITH_DEBIT_NOTE,
		VALIDATE, SET_FOR_RETURN, ON_CREATE_PAYMENT_NOTE
	}

	public interface Display {
		//List
		HasValueSelectables<?> getList();

		//Form
		HasEditableValue<Receipt> getForm();
		void scrollFormToTop();
		boolean isFormValid();

		//Permissions
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowTransferToPolicy(boolean allow);
		void allowAssociateDebitNote(boolean hasPermission);
		void allowValidate(boolean hasPermission);
		void allowSetForReturn(boolean hasPermission);
		void allowSendPaymentNotice(boolean hasPermission);

		//Children Lists
		//TODO

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	protected Display view;
	protected boolean bound;
	protected ReceiptProcessDataBroker receiptBroker;
	private String receiptId;

	public ReceiptSearchOperationViewPresenter(View view) {
		setView(view);
		this.receiptBroker = (ReceiptProcessDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
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
		receiptId = parameterHolder.getParameter("receiptid");

		if(receiptId == null || receiptId.isEmpty()) {
			clearView();
		}else{
			showReceipt(receiptId);
		}
	}

	private void bind() {
		if(bound)
			return;

		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selected = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				ReceiptStub selectedValue = selected == null ? null : selected.getValue();
				view.setSaveModeEnabled(false);

				if(selectedValue == null || selectedValue.id == null){
					NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
					navigationItem.removeParameter("receiptid");
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else{
					NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
					navigationItem.setParameter("receiptid", selectedValue.id);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}
			}
		});
		this.view.registerActionInvokedHandler(new ActionInvokedEventHandler<ReceiptSearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case EDIT:
					onEdit();
					break;
				case CANCEL:
					onCancel();
					break;
				case DELETE:
					deleteReceipt();
					break;
				case SAVE:
					onSave();
					break;
				case TRANSFER_TO_POLICY:
					transferToPolicy();
					break;
				case ASSOCIATE_WITH_DEBIT_NOTE:
					associateWithDebitNote();
					break;
				case SET_FOR_RETURN:
					setForReturn();
					break;
				case VALIDATE:
					onValidate();
					break;
				case ON_CREATE_PAYMENT_NOTE:
					onCreatePaymentNote();
					break;
				}

			}
		});

		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	protected void onCreatePaymentNote() {
		receiptBroker.createPaymentNotice(receiptId, new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Aviso de Cobrança enviado"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar nota de débito"), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}
		});
		
	}

	protected void onValidate() {
		
		receiptBroker.validateReceipt(receiptId, new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O recibo foi validado com sucesso"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível validar o recibo"), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}
		});
		
	}

	protected void setForReturn() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "returnreceipt");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void associateWithDebitNote() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "associatewithdebitnote");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void transferToPolicy() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "receipttransfertopolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void clearView(){
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getList().clearSelection();
		view.setSaveModeEnabled(false);
		view.getForm().setReadOnly(true);
	}

	private void showReceipt(String receiptId) {
		for(ValueSelectable<?> selectable : view.getList().getAll()){
			ReceiptStub receipt = (ReceiptStub) selectable.getValue();
			if(receipt.id.equalsIgnoreCase(receiptId) && !selectable.isSelected()){
				selectable.setSelected(true, true);
				return;
			}
		}

		receiptBroker.getReceipt(receiptId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt value) {
				view.clearAllowedPermissions();
				view.allowEdit(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.UPDATE_RECEIPT));
				view.allowDelete(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.DELETE_RECEIPT));
				view.allowTransferToPolicy(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.TRANSFER_TO_POLICY));
				view.allowAssociateDebitNote(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.ASSOCIATE_WITH_DEBIT_NOTE));
				view.allowValidate(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.VALIDATE));
				view.allowSetForReturn(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.SET_FOR_RETURN));
				view.allowSendPaymentNotice(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.CREATE_PAYMENT_NOTICE));
				view.setSaveModeEnabled(false);
				view.getForm().setReadOnly(true);
				view.getForm().setValue(value);
				view.scrollFormToTop();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetReceiptFailed();
			}
		});
	}

	protected void onEdit(){
		view.getForm().setReadOnly(false);
		view.setSaveModeEnabled(true);
	}

	protected void onCancel(){
		view.getForm().revert();
		view.getForm().setReadOnly(true);
		view.setSaveModeEnabled(false);
	}

	protected void onSave() {
		Receipt receipt = view.getForm().getInfo();

		this.receiptBroker.updateReceipt(receipt, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				onSaveSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveFailed();
			}
		});
	}

	protected void deleteReceipt(){
		Receipt receipt = view.getForm().getValue();

		this.receiptBroker.removeReceipt(receipt.id, new ResponseHandler<String>() {

			@Override
			public void onResponse(String response) {
				onDeleteSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onDeleteFailed();
			}
		});
	}

	protected void onGetReceiptFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o Recibo"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("receiptid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Recibo foi Guardado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Recibo"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Recibo foi Eliminado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("receiptid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDeleteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Eliminar o Recibo"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		onGetReceiptFailed();
	}

}
