package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.ClientOperationsViewPresenter;;

public class ClientOperationsView extends View implements ClientOperationsViewPresenter.Display {

	private HasWidgets container;
	
	public ClientOperationsView(){
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
