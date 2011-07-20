package bigBang.module.riskAnalisysModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.riskAnalisysModule.client.userInterface.RiskAnalisysForm;
import bigBang.module.riskAnalisysModule.client.userInterface.RiskAnalisysSearchPanel;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSearchOperationViewPresenter;

public class RiskAnalisysSearchOperationView extends View implements RiskAnalisysSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 300; //PX
	
	protected RiskAnalisysSearchPanel searchPanel;
	protected RiskAnalisysForm form;
	
	public RiskAnalisysSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new RiskAnalisysSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		form = new RiskAnalisysForm();
		form.setSize("100%", "100%");
		mainWrapper.add(form);
		
		initWidget(mainWrapper);
	}
	
}
