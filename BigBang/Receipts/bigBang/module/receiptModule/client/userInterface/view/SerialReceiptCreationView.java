package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptImagePanel;
import bigBang.module.receiptModule.client.userInterface.SerialReceiptCreationForm;
import bigBang.module.receiptModule.client.userInterface.SerialReceiptCreationToolbar;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter.Action;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SerialReceiptCreationView extends View implements SerialReceiptCreationViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private ReceiptImagePanel receiptPanel;
	private SerialReceiptCreationForm form;
	private SerialReceiptCreationToolbar toolbar;
	private ActionInvokedEventHandler<Action> actionHandler;
	
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
			public void saveReceipt() {
				// TODO Auto-generated method stub
				
			}
		};
		
		right.setSize("100%", "100%");
		form = new SerialReceiptCreationForm(){

			@Override
			protected void onClickMarkAsInvalid() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onClickVerifyPolicyNumber() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onClickVerifyReceiptNumber() {
				// TODO Auto-generated method stub
				
			}
			
		};
		right.add(toolbar);
		right.add(form); 
		right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		right.setCellHeight(form, "100%");
		toolbar.setEnabled(false);
		form.setReadOnly(true);
		
		wrapper.add(right);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}

}
