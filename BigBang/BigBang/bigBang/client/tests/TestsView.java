package bigBang.client.tests;

import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	private NumericTextBoxFormField field = new NumericTextBoxFormField("teste");
	
	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.add(field);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
