package bigBang.module.riskAnalisysModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysOperationsViewPresenter;;

public class RiskAnalisysOperationsView extends View implements RiskAnalisysOperationsViewPresenter.Display {

	private HasWidgets container;
	
	public RiskAnalisysOperationsView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		this.container = wrapper;
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	public HasWidgets getContainer() {
		return this.container;
	}

}
