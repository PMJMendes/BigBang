package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;

public class CoverageManagementOperationView extends View implements CoverageManagementOperationViewPresenter.Display {

	private final int COVERAGE_LIST_WIDTH = 400; //PX
	
	public CoverageManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		wrapper.addWest(new List<Object>(), 300);
		wrapper.addEast(new List<Object>(), 300);
		wrapper.add(new List<Object>());
		
		initWidget(wrapper);
	}
	
}
