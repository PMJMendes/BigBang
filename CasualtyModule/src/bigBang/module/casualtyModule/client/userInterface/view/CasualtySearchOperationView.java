package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanel;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter;

public class CasualtySearchOperationView extends View implements CasualtySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 300; //PX
	
	protected CasualtySearchPanel searchPanel;
	protected CasualtyForm form;
	
	public CasualtySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		this.searchPanel = new CasualtySearchPanel();
		mainWrapper.addWest(this.searchPanel, SEARCH_PANEL_WIDTH);
		
		this.form = new CasualtyForm();
		mainWrapper.add(form);
		
		initWidget(mainWrapper);		
	}
	
}
