package bigBang.client.tests;

import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.client.userInterface.view.View;

public class TestsView extends View implements TestsViewPresenter.Display {

	public TestsView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

}
