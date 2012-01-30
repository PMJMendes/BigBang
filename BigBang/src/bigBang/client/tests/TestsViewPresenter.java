package bigBang.client.tests;

import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TestsViewPresenter implements ViewPresenter {
	

	
	public static interface Display {
		Widget asWidget();

		void setTypifiedTexts(String tag);

	}
	
	private Display view;
	
	public TestsViewPresenter(Display view){
		
		setView((UIObject) view);
		 
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
			
		view.setTypifiedTexts("TEST");
		
	}

}
