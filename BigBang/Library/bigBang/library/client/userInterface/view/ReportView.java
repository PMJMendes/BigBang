package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report.Section;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportParam;
import bigBang.definitions.shared.TransactionSet;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.presenter.ReportViewPresenter;
import bigBang.library.client.userInterface.presenter.ReportViewPresenter.Action;
import bigBang.library.client.userInterface.reports.ParamReportPanel;
import bigBang.library.client.userInterface.reports.PrintSetReportPanel;
import bigBang.library.client.userInterface.reports.ReportCategoryPanel;
import bigBang.library.client.userInterface.reports.TransactionSetReportPanel;

public class ReportView extends View implements ReportViewPresenter.Display {

	protected NavigationPanel navigationPanel;
	protected ActionInvokedEventHandler<Action> handler;
	protected VerticalPanel sectionsContainer;
	
	public ReportView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		navigationPanel = new NavigationPanel("Relatórios");
		navigationPanel.setSize("100%", "100%");
		wrapper.addWest(navigationPanel, 400);
		
		VerticalPanel sectionsWrapper = new VerticalPanel();
		sectionsWrapper.setSize("100%", "100%");
		sectionsWrapper.setStyleName("emptyContainer");
		wrapper.add(sectionsWrapper);
		
		ListHeader sectionsHeader = new ListHeader("Relatório");
		sectionsWrapper.add(sectionsHeader);
		
		sectionsContainer = new VerticalPanel();
		sectionsContainer.setWidth("100%");
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		scrollPanel.add(sectionsContainer);
		
		sectionsWrapper.add(scrollPanel);
		sectionsWrapper.setCellHeight(scrollPanel, "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public HasValueSelectables<ReportItem> createAndGoToReportItems(
			ReportItem[] items) {
		ReportCategoryPanel panel = new ReportCategoryPanel();
		panel.setReportItems(items);
		
		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateTo(panel);
		}else{
			navigationPanel.setHomeWidget(panel);
		}
		
		return panel;
	}

	@Override
	public void createAndGoToReportParameters(ReportParam[] params) {
//		ParamReportPanel panel = new ParamReportPanel();
//		panel.setAvailableParameters(params);
//		
//		if(navigationPanel.hasHomeWidget()){
//			navigationPanel.navigateTo(panel);
//		}else{
//			navigationPanel.setHomeWidget(panel);
//		}
	}

	@Override
	public void createAndGoToReportPrintSets(PrintSet[] printSets) {
		PrintSetReportPanel panel = new PrintSetReportPanel();
		panel.setPrintSets(printSets);
		
		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateTo(panel);
		}else{
			navigationPanel.setHomeWidget(panel);
		}
	}

	@Override
	public void createAndGoToReportTransactions(TransactionSet[] transactionSets) {
		TransactionSetReportPanel panel = new TransactionSetReportPanel();
		panel.setTransationsSets(transactionSets);
		
		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateTo(panel);
		}else{
			navigationPanel.setHomeWidget(panel);
		}
	}

	@Override
	public String[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintSet getSelectedPrintSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionSet getSelectedTransactionSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearReportSections() {
		this.sectionsContainer.clear();
	}

	@Override
	public void addReportSection(Section section) {
		FlowPanel panel = new FlowPanel();
		panel.setWidth("100%");
		panel.getElement().setInnerHTML(section.htmlContent);
		sectionsContainer.add(panel);
	}

	@Override
	public void goHomeAndClear() {
		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateToFirst();
		}
	}

}
