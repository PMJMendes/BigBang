package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyOperationsViewPresenter;

public class CasualtyOperationsView extends View implements CasualtyOperationsViewPresenter.Display {

	private HasWidgets container;
	
	public CasualtyOperationsView(){
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
		return container;
	}

}
