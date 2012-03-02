package bigBang.module.complaintModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintOperationsViewPresenter;
import bigBang.library.client.userInterface.view.View;

public class ComplaintOperationsView extends View implements ComplaintOperationsViewPresenter.Display {

	private HasWidgets container;
	
	public ComplaintOperationsView(){
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
