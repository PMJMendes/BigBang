package bigBang.client.tests;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	private InsurancePolicyForm field;
	
	public TestsView(){

		field = new InsurancePolicyForm() {
			
			@Override
			public void onSubLineChanged(String subLineId) {
				// TODO Auto-generated method stub
				
			}
		};
		
		field.setReadOnly(true);
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.add(field);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
