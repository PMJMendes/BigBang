package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptSectionViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

public class ReceiptSectionView extends View implements ReceiptSectionViewPresenter.Display{

	private HasWidgets wrapper;
	
	public ReceiptSectionView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		this.wrapper = wrapper;
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasWidgets getContainer() {
		return this.wrapper;
	}
	
}
