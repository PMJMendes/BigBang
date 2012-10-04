package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.SignatureRequestOperationsToolbar;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.form.SignatureRequestForm;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SignatureRequestViewPresenter.Action;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SignatureRequestView extends View implements SignatureRequestViewPresenter.Display{

	protected SignatureRequestForm form;
	private SignatureRequestOperationsToolbar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	protected ReceiptForm ownerForm;
	protected ListHeader ownerHeader;
	protected HistoryList historyList;

	
	public SignatureRequestView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		toolbar = new SignatureRequestOperationsToolbar() {
			
			@Override
			public void onRepeatSignatureRequestRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestViewPresenter.Action>(Action.REPEAT_SIGNATURE_REQUEST));
			}
			
			@Override
			public void onReceiveResponseRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestViewPresenter.Action>(Action.RECEIVE_REPLY));
				
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<SignatureRequestViewPresenter.Action>(Action.CANCEL));
				
			}
		};
		
		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ownerHeader = new ListHeader("Ficha do recibo");
		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		ownerForm = new ReceiptForm();
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 665);
		
		VerticalPanel signatureRequestPanel = new VerticalPanel();
		signatureRequestPanel.setSize("100%", "100%");
		ListHeader signatureRequestHeader = new ListHeader("Pedido de Assinatura");
		signatureRequestPanel.add(signatureRequestHeader);
		signatureRequestHeader.setHeight("30px");
		form = new SignatureRequestForm();
		signatureRequestPanel.add(toolbar);
		signatureRequestPanel.add(form);
		signatureRequestPanel.setCellHeight(form, "100%");
		signatureRequestPanel.setCellHeight(toolbar, "21px");
		
		SplitLayoutPanel childWrapper = new SplitLayoutPanel();
		historyList = new HistoryList();
		StackPanel stackWrapper = new StackPanel();
		stackWrapper.add(historyList, "Histórico");
		
		stackWrapper.setSize("100%", "100%");
		
		childWrapper.setSize("100%", "100%");
		
		childWrapper.addEast(stackWrapper, 260);
		childWrapper.add(signatureRequestPanel);
		
		mainWrapper.add(childWrapper);
	}
	
	
	@Override
	public void applyOwnerToList(String negotiationId) {
		historyList.setOwner(negotiationId);
	}

	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public HasEditableValue<SignatureRequest> getForm() {
		return form;
	}
	
	@Override
	public HasEditableValue<Receipt> getOwnerForm(){
		return ownerForm;
	}

	@Override
	public void allowCancel(boolean hasPermission) {
		toolbar.allowCancel(hasPermission);
	}

	@Override
	public void allowReceiveResponse(boolean hasPermission) {
		toolbar.allowReceiveResponse(hasPermission);
	}

	@Override
	public void allowRepeatRequest(boolean hasPermission) {
		toolbar.allowRepeatRequest(hasPermission);
	}

	@Override
	public void disableToolbar() {
		toolbar.setEnabled(false);
	}

	@Override
	protected void initializeView() {
		return;
	}


	@Override
	public HasSelectables<ValueSelectable<HistoryItemStub>> getHistoryList() {
		return historyList;
	}


}
