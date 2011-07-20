package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.ReceiptProcessToolBar;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSearchOperationViewPresenter;

public class ReceiptSearchOperationView extends View implements ReceiptSearchOperationViewPresenter.Display {
	
	protected static final int SEARCH_PANEL_WIDTH = 300; //PX
	
	protected ReceiptSearchPanel searchPanel;
	protected ReceiptForm form;
	protected ReceiptProcessToolBar operationsToolbar;
	
	public ReceiptSearchOperationView() {
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		this.searchPanel = new ReceiptSearchPanel();
		mainWrapper.addWest(this.searchPanel, SEARCH_PANEL_WIDTH);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		operationsToolbar = new ReceiptProcessToolBar();
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		
		this.form = new ReceiptForm();
		formWrapper.add(form);
		
		mainWrapper.add(formWrapper);
		
		initWidget(mainWrapper);
	}

}
