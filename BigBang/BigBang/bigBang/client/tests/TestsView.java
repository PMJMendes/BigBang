package bigBang.client.tests;

import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	protected TypifiedTextFormField text;
	
	public TestsView(){
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		text = new TypifiedTextFormField();
		text.setReadOnly(false);
		text.setTypifiedTexts("TEST");
	
		wrapper.add(text);
	}

	@Override
	protected void initializeView() {
		return;
	}


}
