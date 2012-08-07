package bigBang.client.tests;

import bigBang.library.client.userInterface.FileImportFormViewSection;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	protected TypifiedTextFormField text;
	
	public TestsView(){
		initWidget(wrapper);
		wrapper.setSize("100%", "auto");
		
		FileImportFormViewSection section = new FileImportFormViewSection("", "Receipt");
		
		wrapper.add(section);
	}

	@Override
	protected void initializeView() {
		return;
	}


}
