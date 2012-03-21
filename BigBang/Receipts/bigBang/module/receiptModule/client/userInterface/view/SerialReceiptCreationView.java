package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.ReceiptImagePanel;
import bigBang.module.receiptModule.client.userInterface.ReceiptNumberForm;
import bigBang.module.receiptModule.client.userInterface.SerialReceiptCreationToolbar;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter.Action;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SerialReceiptCreationView extends View implements SerialReceiptCreationViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private ReceiptImagePanel receiptPanel;
	private ReceiptNumberForm receiptNumber;
	private ReceiptForm receipt;
	private SerialReceiptCreationToolbar toolbar;
	
	public SerialReceiptCreationView(){
		wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		receiptPanel = new ReceiptImagePanel();
		listWrapper.add(receiptPanel);
		receiptPanel.setSize("100%", "100%");
		listWrapper.setCellHeight(receiptPanel, "100%");
		wrapper.addWest(listWrapper, 600);
		
		VerticalPanel right = new VerticalPanel();
		toolbar = new SerialReceiptCreationToolbar() {
			
			@Override
			public void onCancelRequest() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSetForReturn() {
				// TODO Auto-generated method stub
				
			}
		};
		
		ListHeader header = new ListHeader("Verificar Número do Recibo");
		header.setSize("100%", "30px");
		right.setSize("100%", "100%");
		receiptNumber = new ReceiptNumberForm();
		//receiptNumber.setSize("100%", "100%");
		receipt = new ReceiptForm();
		right.add(header);
		right.add(receiptNumber.getNonScrollableContent()); 
		ListHeader lowHeader = new ListHeader("Ficha do Recibo");
		lowHeader.setSize("100%", "30px");
		right.add(lowHeader);
		right.add(toolbar);
		right.add(receipt);
		right.setCellHeight(receipt, "100%");
		toolbar.setEnabled(false);
		receipt.setReadOnly(true);
		receiptNumber.setReadOnly(true);
		
		wrapper.add(right);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		// TODO Auto-generated method stub
		
	}

}
