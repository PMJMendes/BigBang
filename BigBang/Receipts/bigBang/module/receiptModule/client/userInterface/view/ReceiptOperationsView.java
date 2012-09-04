package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptOperationsViewPresenter;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;

public class ReceiptOperationsView extends View implements ReceiptOperationsViewPresenter.Display {

	private HasWidgets container;

	public ReceiptOperationsView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		this.container = wrapper;
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasWidgets getContainer() {
		return this.container;
	}

}
