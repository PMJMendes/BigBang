package bigBang.client.tests;

import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	private NumericTextBoxFormField number;
	
	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		number = new NumericTextBoxFormField("Guita");
		number.setValue(123456789.12);
		System.out.println(number.getValue());
		wrapper.add(number);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
