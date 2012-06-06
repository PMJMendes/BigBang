package bigBang.client.tests;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	private InsurancePolicyForm pol = new InsurancePolicyForm() {
		
		@Override
		public void onSubLineChanged(String subLineId) {
			return;
		}
	};
	
	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.add(pol);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
