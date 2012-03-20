package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptChildrenLists;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.ReceiptProcessToolBar;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter.Action;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiptSearchOperationView extends View implements ReceiptSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX

	protected ReceiptSearchPanel searchPanel;
	protected ReceiptForm form;
	protected ReceiptProcessToolBar operationsToolbar;
	protected DocumentsPreviewList documentsList;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected ReceiptChildrenLists childrenLists;

	public ReceiptSearchOperationView() {
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		childrenLists = new ReceiptChildrenLists();

		this.searchPanel = new ReceiptSearchPanel();
		mainWrapper.addWest(this.searchPanel, SEARCH_PANEL_WIDTH);

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
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestDAS() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestPurchaseOrderNumber() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUnnecessaryDASFlag() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSetForReturn() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLackOfPaymentFlag() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onEnterPayment() {
				// TODO Auto-generated method stub

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
				// TODO Auto-generated method stub

			}

			@Override
			public void onReturnToAgency() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSendPaymentToAgency() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSendPaymentToClient() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSendReceipt() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResendPaymentNotice() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSendPaymentNotice() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onValidate() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransferToPolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptSearchOperationViewPresenter.Action>(Action.TRANSFER_TO_POLICY));

			}

			@Override
			public void onCreateCreditNote() {
				// TODO Auto-generated method stub

			}
		};
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");

		this.form = new ReceiptForm();
		formWrapper.add(form);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		documentsList = new DocumentsPreviewList();
		documentsList.setHeight("100%");
		contentWrapper.addEast(documentsList, 300);

		contentWrapper.add(formWrapper);

		mainWrapper.add(contentWrapper);

		if(!bigBang.definitions.client.Constants.DEBUG){
			searchPanel.doSearch();
		}
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

}
