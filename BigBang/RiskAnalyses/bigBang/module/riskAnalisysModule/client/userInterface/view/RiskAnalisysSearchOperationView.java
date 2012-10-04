package bigBang.module.riskAnalisysModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.riskAnalisysModule.client.userInterface.RiskAnalisysProcessToolBar;
import bigBang.module.riskAnalisysModule.client.userInterface.RiskAnalisysSearchPanel;
import bigBang.module.riskAnalisysModule.client.userInterface.form.RiskAnalisysForm;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSearchOperationViewPresenter;

public class RiskAnalisysSearchOperationView extends View implements RiskAnalisysSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected RiskAnalisysSearchPanel searchPanel;
	protected RiskAnalisysForm form;
	protected DocumentsPreviewList documentsList;
	protected RiskAnalisysProcessToolBar operationsToolbar;
	
	public RiskAnalisysSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);

		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new RiskAnalisysSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		documentsList = new DocumentsPreviewList();
		documentsList.setHeight("100%");
		
		operationsToolbar = new RiskAnalisysProcessToolBar();
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		
		form = new RiskAnalisysForm();
		form.setSize("100%", "100%");
		
		formWrapper.add(form);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		contentWrapper.addEast(documentsList, 300);
		contentWrapper.add(formWrapper);
		
		mainWrapper.add(contentWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
}
