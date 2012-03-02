package bigBang.module.quoteRequestModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestOperationsViewPresenter;

public class QuoteRequestOperationsView extends View implements QuoteRequestOperationsViewPresenter.Display {

	private HasWidgets container;
	
	public QuoteRequestOperationsView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		setSize("100%", "100%");
		
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
