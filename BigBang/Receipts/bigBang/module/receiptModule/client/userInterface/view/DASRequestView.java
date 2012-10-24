package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.DASRequestOperationsToolbar;
import bigBang.module.receiptModule.client.userInterface.form.DASRequestForm;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.presenter.DASRequestViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.DASRequestViewPresenter.Action;

public class DASRequestView extends View implements DASRequestViewPresenter.Display{

	protected DASRequestForm form;
	private DASRequestOperationsToolbar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	protected ReceiptForm ownerForm;
	protected ListHeader ownerHeader;
	protected HistoryList historyList;
	
	public DASRequestView(){
		
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		toolbar = new DASRequestOperationsToolbar() {
			
			@Override
			public void onRepeatDASRequestRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REPEAT_DAS_REQUEST));
				
			}
			
			@Override
			public void onReceiveResponseRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.RECEIVE_REPLY));
				
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));				
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
		Button backButton = new Button("Voltar", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<DASRequestViewPresenter.Action>(Action.BACK));
			}
		});
		ownerHeader.setLeftWidget(backButton);
		mainWrapper.addWest(ownerWrapper, 665);
		
		VerticalPanel dasRequestPanel = new VerticalPanel();
		dasRequestPanel.setSize("100%", "100%");
		ListHeader dasRequestHeader = new ListHeader("Declaração de Ausência de Sinistro");
		dasRequestPanel.add(dasRequestHeader);
		dasRequestHeader.setHeight("30px");
		form = new DASRequestForm();
		dasRequestPanel.add(toolbar);
		dasRequestPanel.add(form);
		dasRequestPanel.setCellHeight(form, "100%");
		dasRequestPanel.setCellHeight(toolbar, "21px");
		
		SplitLayoutPanel childWrapper = new SplitLayoutPanel();
		historyList = new HistoryList();
		StackPanel stackWrapper = new StackPanel();
		stackWrapper.add(historyList, "Histórico");
		
		stackWrapper.setSize("100%", "100%");
		
		childWrapper.setSize("100%", "100%");
		
		childWrapper.addEast(stackWrapper, 260);
		childWrapper.add(dasRequestPanel);
		
		mainWrapper.add(childWrapper);
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public HasEditableValue<DASRequest> getForm() {
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
	
	
	@Override
	public void applyOwnerToList(String negotiationId) {
		historyList.setOwner(negotiationId);
	}


}
