package bigBang.library.client.userInterface.view;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
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
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
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
	protected ParamReportPanel paramPanel;
	protected VerticalPanel sectionsContainer;
	private View panel;
	private Button generateReport;

	public ReportView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		generateReport = new Button();
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


		panel = new ParamReportPanel();
		((ParamReportPanel)panel).setAvailableParameters(params);

		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateTo(panel);
		}else{
			navigationPanel.setHomeWidget(panel);
		}

		((ParamReportPanel)panel).getButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.GENERATE_REPORT);
			}
		});

	}

	protected void fireAction(Action generateReport) {
		handler.onActionInvoked(new ActionInvokedEvent<ReportViewPresenter.Action>(generateReport));
	}

	@Override
	public void createAndGoToReportPrintSets(PrintSet[] printSets) {

		panel = new PrintSetReportPanel();
		((PrintSetReportPanel)panel).setPrintSets(printSets);

		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateTo(panel);
		}else{
			navigationPanel.setHomeWidget(panel);
		}

		generateReport = new Button("Gerar Relatório");
		generateReport.setEnabled(false);
		generateReport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.GENERATE_REPORT);
			}
		});
		navigationPanel.navBar.setRightWidget(generateReport);

		((PrintSetReportPanel)panel).addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				fireAction(Action.PRINT_SET_SELECTION_CHANGED);
			}
		});
		
	}
	
	@Override
	public HasValueSelectables<PrintSet> getPrintSetPanel(){
		return (PrintSetReportPanel)panel;
	}
	
	@Override
	public void setGenerateReportButtonEnabled(boolean b){
		generateReport.setEnabled(b);
	}

	@Override
	public void createAndGoToReportTransactions(TransactionSet[] transactionSets) {

		panel = new TransactionSetReportPanel();
		((TransactionSetReportPanel)panel).setTransationsSets(transactionSets);

		if(navigationPanel.hasHomeWidget()){
			navigationPanel.navigateTo(panel);
		}else{
			navigationPanel.setHomeWidget(panel);
		}
		//TODO BUTTON
		//((TransactionSetReportPanel)panel).getButton();
	}

	@Override
	public String[] getParameters() {
		return ((ParamReportPanel)panel).getParameters();
	}

	@Override
	public PrintSet getSelectedPrintSet() {
		return ((PrintSetReportPanel)panel).getSelectedPrintSet();
	}

	@Override
	public TransactionSet getSelectedTransactionSet() {
		return ((TransactionSetReportPanel)panel).getSelectedTransactionSet();
	}

	@Override
	public void clearReportSections() {
		this.sectionsContainer.clear();
	}
	
	@Override
	public NavigationPanel getNavigationPanel(){
		return navigationPanel;
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

	@Override
	public void showButton(boolean b) {
		generateReport.setVisible(b);
	}

}
