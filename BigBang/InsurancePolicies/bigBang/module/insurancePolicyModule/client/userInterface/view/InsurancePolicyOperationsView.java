package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicyOperationsViewPresenter;

public class InsurancePolicyOperationsView extends View implements InsurancePolicyOperationsViewPresenter.Display {

	private HasWidgets container;
	
	public InsurancePolicyOperationsView(){
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
