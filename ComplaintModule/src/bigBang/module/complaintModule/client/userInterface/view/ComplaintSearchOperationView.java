package bigBang.module.complaintModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.complaintModule.client.userInterface.ComplaintForm;
import bigBang.module.complaintModule.client.userInterface.ComplaintSearchPanel;
import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintSearchOperationViewPresenter;

public class ComplaintSearchOperationView extends View implements ComplaintSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 300; //PX
	protected ComplaintSearchPanel searchPanel;
	protected ComplaintForm form;
	
	public ComplaintSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new ComplaintSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		form = new ComplaintForm();
		mainWrapper.add(form);
		
		initWidget(mainWrapper);		
	}
	
}
