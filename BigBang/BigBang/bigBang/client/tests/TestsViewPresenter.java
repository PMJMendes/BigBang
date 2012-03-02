package bigBang.client.tests;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.CoInsurerSelection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TestsViewPresenter implements ViewPresenter {
	

	
	public static interface Display {
		Widget asWidget();

		void setTypifiedTexts(String tag);
		HasWidgets getContainer();
		void show();
		void setMainCoInsuranceAgency(String string);

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
			
//		ViewPresenter presenter = ViewPresenterFactory.getInstance().getViewPresenter("CONTACT");
//		
//		presenter.go(view.getContainer());
		view.show();
		view.setMainCoInsuranceAgency("4e61a352-31a1-4a65-91c7-9fb700200fb8");
		
//		presenter.setParameters(parameterHolder);
		
	}

}
