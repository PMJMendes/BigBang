package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.DocumentsPreviewList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.CasualtyProcessToolBar;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanel;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter;

public class CasualtySearchOperationView extends View implements CasualtySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected CasualtySearchPanel searchPanel;
	protected CasualtyForm form;
	protected DocumentsPreviewList documentsList;
	protected CasualtyProcessToolBar operationsToolbar;
	
	public CasualtySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		this.searchPanel = new CasualtySearchPanel();
		mainWrapper.addWest(this.searchPanel, SEARCH_PANEL_WIDTH);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		operationsToolbar = new CasualtyProcessToolBar();
		this.form = new CasualtyForm();
		this.documentsList = new DocumentsPreviewList();
		this.documentsList.setHeight("100%");
		
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		contentWrapper.addEast(documentsList, 300);
		contentWrapper.add(formWrapper);
		
		mainWrapper.add(contentWrapper);
		
		initWidget(mainWrapper);		
	}
	
}
