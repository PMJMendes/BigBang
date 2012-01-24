package bigBang.client.tests;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.view.View;

public class TestsView extends View implements TestsViewPresenter.Display {

	private VerticalPanel wrapper = new VerticalPanel();
	private TypifiedTextFormField form = new TypifiedTextFormField();
	
	public TestsView(){
		
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		wrapper.add(form);
		
	}
	
	@Override
	protected void initializeView() {
		return;
	}

}
