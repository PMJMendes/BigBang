package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptChildrenPanel;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.ReceiptProcessToolBar;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter.Action;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiptSearchOperationView extends View implements ReceiptSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX

	protected ReceiptSearchPanel searchPanel;
	protected ReceiptForm form;
	protected ReceiptProcessToolBar operationsToolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected ReceiptChildrenPanel childrenPanel;
	

	public ReceiptSearchOperationView() {
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");
		
		ListHeader header = new ListHeader("Recibos");
		searchPanelWrapper.add(header);
		
		searchPanel = new ReceiptSearchPanel();
		searchPanelWrapper.add(searchPanel);
		searchPanelWrapper.setCellHeight(searchPanel, "100%");
		
		mainWrapper.addWest(searchPanelWrapper, SEARCH_PANEL_WIDTH);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		operationsToolbar = new ReceiptProcessToolBar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onRequestAdvanceDebit() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestPhysicalReceiptCopy() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestPhysicalReceipt() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestSignature() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.CREATE_SIGNATURE_REQUEST));
			}

			@Override
			public void onRequestDAS() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.REQUEST_DAS));
			}

			@Override
			public void onRequestPurchaseOrderNumber() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnnecessaryDASFlag() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.SET_DAS_NOT_NECESSARY));
			}

			@Override
			public void onSetForReturn() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.SET_FOR_RETURN));

			}

			@Override
			public void onLackOfPaymentFlag() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.NOT_PAYED_INDICATION));

			}

			@Override
			public void onEnterPayment() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.MARK_FOR_PAYMENT));
			}

			@Override
			public void onAssociateWithDebitNote() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.ASSOCIATE_WITH_DEBIT_NOTE));

			}

			@Override
			public void onReceivePhysicalReceipt() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSendPaymentToMediator() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.AGENT_ACCOUNTING));
			}

			@Override
			public void onReturnToAgency() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.RETURN_TO_AGENCY));
			}

			@Override
			public void onSendPaymentToAgency() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.INSURER_ACCOUNTING));

			}

			@Override
			public void onSendPaymentToClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.PAYMENT_TO_CLIENT));
			}

			@Override
			public void onSendReceipt() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.SEND_RECEIPT));
			}

			@Override
			public void onResendPaymentNotice() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSendPaymentNotice() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.ON_CREATE_PAYMENT_NOTE));

			}

			@Override
			public void onValidate() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.VALIDATE));

			}

			@Override
			public void onTransferToPolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.TRANSFER_TO_POLICY));

			}

			@Override
			public void onCreateCreditNote() {
				// TODO Auto-generated method stub

			}
			
			@Override
			public void onReturnPayment() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.RETURN_PAYMENT));
			}
			
		};
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");

		this.form = new ReceiptForm();
		formWrapper.add(form);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		childrenPanel = new ReceiptChildrenPanel();
		childrenPanel.setHeight("100%");
		contentWrapper.addEast(childrenPanel, 300);

		contentWrapper.add(formWrapper);

		mainWrapper.add(contentWrapper);

		if(!bigBang.definitions.client.Constants.DEBUG){
			searchPanel.doSearch();
		}
		
		form.addValueChangeHandler(new ValueChangeHandler<Receipt>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Receipt> event) {
				Receipt receipt = event.getValue();
				childrenPanel.setReceipt(receipt);
			}
		});
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<?> getList() {
		return this.searchPanel;
	}

	@Override
	public HasEditableValue<Receipt> getForm() {
		return this.form;
	}

	@Override
	public boolean isFormValid() {
		return this.form.validate();
	}

	@Override
	public void clearAllowedPermissions() {
		this.operationsToolbar.lockAll();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.operationsToolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void allowEdit(boolean allow) {
		this.operationsToolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.operationsToolbar.allowDelete(allow);
	}

	@Override
	public void scrollFormToTop() {
		this.form.scrollToTop();
	}

	@Override
	public void allowTransferToPolicy(boolean allow) {
		this.operationsToolbar.allowTransfer(allow);
		
	}

	@Override
	public void allowAssociateDebitNote(boolean hasPermission) {
		operationsToolbar.allowAssociateDebitNote(hasPermission);
		
	}

	@Override
	public void allowValidate(boolean hasPermission) {
		operationsToolbar.allowValidate(hasPermission);
		
	}

	@Override
	public void allowSetForReturn(boolean hasPermission) {
		operationsToolbar.allowSetForReturn(hasPermission);
		
	}

	@Override
	public void allowSendPaymentNotice(boolean hasPermission) {
		operationsToolbar.allowCreatePaymentNotice(hasPermission);
		
	}
	
	@Override
	public void allowMarkForPayment(boolean allow) {
		operationsToolbar.allowMarkForPayment(allow);
	}
	
	@Override
	public void allowSendReceipt(boolean allow) {
		operationsToolbar.allowSendReceipt(allow);
	}

	@Override
	public void allowInsurerAccounting(boolean allow) {
		operationsToolbar.allowInsurerAccounting(allow);
	}
	
	@Override
	public void allowAgentAccounting(boolean allow) {
		this.operationsToolbar.allowAgentAccounting(allow);
	}
	
	@Override
	public void allowPaymentToClient(boolean hasPermission) {
		operationsToolbar.allowPaymentToClient(hasPermission);
		
	}

	@Override
	public void allowReturnToInsurer(boolean hasPermission) {
		operationsToolbar.allowReturnToAgency(hasPermission);
		
	}

	@Override
	public void allowCreateSignatureRequest(boolean hasPermission) {
		operationsToolbar.allowCreateSignatureRequest(hasPermission);
		
	}
	
	@Override
	public void allowSetDASDesnecessary(boolean hasPermission){
		operationsToolbar.allowSetDASDesnecessary(hasPermission);
	}
	
	@Override
	public void allowRequestDAS(boolean hasPermission){
		operationsToolbar.allowRequestDAS(hasPermission);
	}
	
	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return this.childrenPanel.contactsList;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return this.childrenPanel.documentsList;
	}
	
	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return this.childrenPanel.subProcessesList;
	}
	
	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public void allowSetNotPaid(boolean hasPermission) {
		operationsToolbar.allowSetNotPaid(hasPermission);
	}
	
	@Override
	public void allowReturnPayment(boolean hasPermission) {
		operationsToolbar.allowReturnPayment(hasPermission);
	}

}
