package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DocuShareNavigationPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SerialReceiptCreationViewPresenter.Action;

public class SerialReceiptCreationView extends View implements SerialReceiptCreationViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private DocuShareNavigationPanel docuPanel;
	
	public SerialReceiptCreationView(){
		wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		docuPanel = new DocuShareNavigationPanel();
		listWrapper.add(docuPanel);
		docuPanel.setSize("100%", "100%");
		listWrapper.setCellHeight(docuPanel, "100%");
		
		wrapper.addWest(listWrapper, 250);
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

	
	@Override
	public void fillDocuPanel(){
		docuPanel.setParameters(null,null);
	}
}
