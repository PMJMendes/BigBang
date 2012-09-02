package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
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
		VALIDATE, SET_FOR_RETURN, ON_CREATE_PAYMENT_NOTE,
		MARK_FOR_PAYMENT,
		SEND_RECEIPT,
		INSURER_ACCOUNTING, AGENT_ACCOUNTING,
		PAYMENT_TO_CLIENT, RETURN_TO_AGENCY, CREATE_SIGNATURE_REQUEST, SET_DAS_NOT_NECESSARY, REQUEST_DAS, NOT_PAYED_INDICATION,
		RETURN_PAYMENT
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
		void allowMarkForPayment(boolean allow);
		void allowSendReceipt(boolean allow);
		void allowInsurerAccounting(boolean allow);
		void allowAgentAccounting(boolean allow);
		void allowPaymentToClient(boolean hasPermission);
		void allowReturnToInsurer(boolean hasPermission);
		void allowCreateSignatureRequest(boolean hasPermission);
		void allowSetDASDesnecessary(boolean hasPermission);
		void allowSetNotPaid(boolean hasPermission);
		void allowReturnPayment(boolean hasPermission);
		//Children Lists
		//TODO

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();

		HasValueSelectables<Document> getDocumentsList();

		HasValueSelectables<Contact> getContactsList();

		HasValueSelectables<BigBangProcess> getSubProcessesList();

		HasValueSelectables<HistoryItemStub> getHistoryList();

		void allowRequestDAS(boolean hasPermission);

	}

	protected Display view;
	protected boolean bound;
	protected ReceiptDataBroker receiptBroker;
	private String receiptId;

	public ReceiptSearchOperationViewPresenter(View view) {
		setView(view);
		this.receiptBroker = (ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
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
				case MARK_FOR_PAYMENT:
					onMarkForPayment();
					break;
				case SEND_RECEIPT:
					onSendReceipt();
					break;
				case INSURER_ACCOUNTING:
					onInsurerAccounting();
					break;
				case AGENT_ACCOUNTING:
					onAgentAccounting();
					break;
				case PAYMENT_TO_CLIENT:
					onPaymentToClient();
					break;
				case RETURN_TO_AGENCY:
					onReturnToAgency();
					break;
				case CREATE_SIGNATURE_REQUEST:
					onCreateSignatureRequest();
					break;
				case SET_DAS_NOT_NECESSARY:
					setDasNotNecessary();
					break;
				case REQUEST_DAS:
					requestDas();
					break;
				case NOT_PAYED_INDICATION:
					notPayedIndication();
					break;
				case RETURN_PAYMENT:
					returnPayment();
					break;
				}

			}
		});

		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				Contact selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<Contact>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showContact(selectedValue);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				Document selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<Document>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showDocument(selectedValue);
				}
			}
		});
		view.getSubProcessesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				BigBangProcess selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<BigBangProcess>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showSubProcess(selectedValue);
				}
			}
		});
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				HistoryItemStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<HistoryItemStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showHistory(selectedValue);
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	protected void notPayedIndication() {
		receiptBroker.indicateNotPaid(receiptId, new ResponseHandler<Receipt>() {
			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibo marcado como não pago."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível marcar o recibo como não pago."), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();				
			}
		});

	}

	protected void returnPayment(){
		receiptBroker.returnPayment(receiptId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Devolução de Pagamento criada com Sucesso."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível defazer o pagamento do Recibo."), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();				
			}
		});
	}

	protected void requestDas() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "createdasrequest");
		NavigationHistoryManager.getInstance().go(item);	
	}

	protected void setDasNotNecessary() {
		receiptBroker.setDASNotNecessary(receiptId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Indicação de DAS desnecessária efectuada."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível indicar DAS como desnecessária"), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();				
			}
		});

	}

	protected void onCreateSignatureRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "createsignaturerequest");
		NavigationHistoryManager.getInstance().go(item);	
	}

	protected void onReturnToAgency() {
		receiptBroker.returnToInsurer(receiptId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibo devolvido à seguradora"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível devolver o recibo"), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}
		});

	}

	protected void onPaymentToClient() {
		receiptBroker.sendPayment(receiptId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pagamento efectuado ao cliente"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar o pagamento ao cliente"), TYPE.ALERT_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}
		});

	}

	protected void onCreatePaymentNote() {
		receiptBroker.createPaymentNotice(receiptId, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Aviso de cobrança enviado"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar aviso de cobrança"), TYPE.ALERT_NOTIFICATION));
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
			if(receipt.id.equalsIgnoreCase(receiptId)){
				selectable.setSelected(true, false);
			}else if(selectable.isSelected()){
				selectable.setSelected(false, false);
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
				view.allowMarkForPayment(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.MARK_FOR_PAYMENT));
				view.allowSendReceipt(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.SEND_RECEIPT));
				view.allowInsurerAccounting(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.INSURER_ACCOUNTING));
				view.allowAgentAccounting(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.AGENT_ACCOUNTING));
				view.allowPaymentToClient(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.SEND_PAYMENT_TO_CLIENT));
				view.allowReturnToInsurer(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.RETURN_TO_AGENCY));
				view.allowCreateSignatureRequest(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.CREATE_SIGNATURE_REQUEST));
				view.allowSetDASDesnecessary(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.SET_DAS_NOT_NECESSARY));
				view.allowRequestDAS(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.CREATE_DAS_REQUEST));
				view.allowSetNotPaid(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.NOT_PAID_INDICATION));
				view.allowReturnPayment(PermissionChecker.hasPermission(value, BigBangConstants.OperationIds.ReceiptProcess.RETURN_PAYMENT));
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
		if(view.getForm().validate()) {
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

	protected void onMarkForPayment(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "markforpayment");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSendReceipt(){
		String receiptId = view.getForm().getValue().id;
		receiptBroker.sendReceipt(receiptId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onSendReceiptSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendReceiptFailed();
			}
		});
	}

	protected void onInsurerAccounting(){
		String receiptId  =view.getForm().getValue().id;
		receiptBroker.insurerAccounting(receiptId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onInsurerAccountingSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onInsurerAccountingFailed();
			}
		});
	}

	protected void onAgentAccounting(){
		String receiptId = view.getForm().getValue().id;
		receiptBroker.agentAccounting(receiptId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onAgentAccountingSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onAgentAccountingFailed();
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

	protected void onSendReceiptFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Enviar o Recibo"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSendReceiptSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Recibo foi Enviado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onInsurerAccountingSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Prestação de Contas com a Seguradora foi efectuada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onInsurerAccountingFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Prestar Contas com a Seguradora"), TYPE.ALERT_NOTIFICATION));

	}

	protected void onAgentAccountingSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Prestação de Contas com o Mediador foi efectuada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onAgentAccountingFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Prestar Contas com o Mediador"), TYPE.ALERT_NOTIFICATION));

	}

	protected void onFailure(){
		onGetReceiptFailed();
	}

	private void showContact(final Contact contact) {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "contactmanagement");
		item.setParameter("ownerid", contact.ownerId);
		item.setParameter("ownertypeid", contact.ownerTypeId);
		item.setParameter("contactid", contact.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showDocument(final Document document){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "documentmanagement");
		item.setParameter("ownerid", document.ownerId);
		item.setParameter("ownertypeid", document.ownerTypeId);
		item.setParameter("documentid", document.id);
		NavigationHistoryManager.getInstance().go(item);
	}


	private void showSubProcess(final BigBangProcess process){
		String type = process.dataTypeId;
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.SIGNATURE_REQUEST)){
			item.pushIntoStackParameter("display", "signaturerequest");
			item.setParameter("signaturerequestid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);

		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.DAS_REQUEST)){
			item.pushIntoStackParameter("display", "dasrequest");
			item.setParameter("dasrequestid", process.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	private void showHistory(final HistoryItemStub historyItem) {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", view.getForm().getValue().id);
		item.setParameter("historyitemid", historyItem.id);
		NavigationHistoryManager.getInstance().go(item);

	}

}
