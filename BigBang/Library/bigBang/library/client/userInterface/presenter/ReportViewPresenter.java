package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.Report.Section;
import bigBang.definitions.shared.ReportItem;
import bigBang.definitions.shared.ReportParam;
import bigBang.definitions.shared.TransactionSet;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NavigationRequestEvent;
import bigBang.library.client.event.NavigationRequestEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.interfaces.ReportService;
import bigBang.library.interfaces.ReportServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReportViewPresenter implements ViewPresenter {

	public static enum Action {
		GENERATE_REPORT, 
		PRINT_SET_SELECTION_CHANGED, 
		TRANSACTION_SET_SELECTION_CHANGED
	}

	public static interface Display {
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		HasValueSelectables<ReportItem> createAndGoToReportItems(ReportItem[] items);
		void createAndGoToReportParameters(ReportParam[] result);
		void createAndGoToReportPrintSets(PrintSet[] result);
		void createAndGoToReportTransactions(TransactionSet[] transactionSets);
		String[] getParameters();
		PrintSet getSelectedPrintSet();
		TransactionSet getSelectedTransactionSet();
		void clearReportSections();
		void addReportSection(Section section);
		void goHomeAndClear();
		HasValueSelectables<PrintSet> getPrintSetPanel();
		void setGenerateReportButtonEnabled(boolean b);
		void showButton(boolean b);
		NavigationPanel getNavigationPanel();
	}

	protected boolean bound;
	protected ReportServiceAsync service;
	protected Display view;
	protected String processTypeId;
	protected ReportItem currentItem;

	public ReportViewPresenter(Display view){
		setView((UIObject)view);
		service = ReportService.Util.getInstance();
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		this.processTypeId = parameterHolder.getParameter("processtypeid");

		if(this.processTypeId == null || this.processTypeId.isEmpty()) {
			onFailure();
		}else{
			showProcessReports(this.processTypeId);
		}
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<ReportViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case GENERATE_REPORT:
					onGenerateReport();
					break;
				case PRINT_SET_SELECTION_CHANGED:
					onPrintSetSelectionChanged();
					break;
				case TRANSACTION_SET_SELECTION_CHANGED:
					onTransactionSetSelectionChanged();
					break;
				}
			}
		});
		
		view.getNavigationPanel().navBar.addNavigationEventHandler(new NavigationRequestEventHandler() {
			
			@Override
			public void onNavigationEvent(NavigationRequestEvent event) {
				view.showButton(false);
			}
		});

		this.bound = true;
	}

	protected void onTransactionSetSelectionChanged() {
		view.setGenerateReportButtonEnabled(!(view.getSelectedTransactionSet() == null));
		
	}

	protected void onPrintSetSelectionChanged() {
		view.setGenerateReportButtonEnabled(!view.getPrintSetPanel().getSelected().isEmpty());

	}

	protected void clearView() {
		view.goHomeAndClear();
		currentItem = null;
		view.clearReportSections();
	}

	protected void showProcessReports(String processTypeId){
		service.getProcessItems(processTypeId, new BigBangAsyncCallback<ReportItem[]>() {

			@Override
			public void onResponseSuccess(ReportItem[] result) {
				showItems(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGetNextFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void showItems(ReportItem[] items){
		HasValueSelectables<ReportItem> newList = view.createAndGoToReportItems(items);
		newList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				ReportItem item = (ReportItem) (selected == null ? null : selected.getValue());

				if(item != null) {
					switch(item.type) {
					case CATEGORY:
						showCategory(item);
						view.showButton(false);
						break;
					case PARAM:
						showParamReport(item);
						break;
					case PRINTSET:
						view.showButton(true);
						showPrintSetReport(item);
						break;
					case TRANSACTIONSET:
						showTransactionSetReport(item);
						break;
					}
				}
			}
		});
	}

	protected void showCategory(final ReportItem item){
		service.getSubItems(item.id, new BigBangAsyncCallback<ReportItem[]>() {

			@Override
			public void onResponseSuccess(ReportItem[] result) {
				currentItem = item;
				showItems(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGetNextFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void showParamReport(final ReportItem item) {
		service.getParams(item.id, new BigBangAsyncCallback<ReportParam[]>() {

			@Override
			public void onResponseSuccess(ReportParam[] result) {
				currentItem = item;
				view.createAndGoToReportParameters(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGetNextFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void showPrintSetReport(final ReportItem item) {
		service.getPrintSets(item.id, new BigBangAsyncCallback<PrintSet[]>() {

			@Override
			public void onResponseSuccess(PrintSet[] result) {
				currentItem = item;
				view.createAndGoToReportPrintSets(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGetNextFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void showTransactionSetReport(final ReportItem item) {
		service.getTransactionSets(item.id, new BigBangAsyncCallback<TransactionSet[]>() {

			@Override
			public void onResponseSuccess(TransactionSet[] result) {
				currentItem = item;
				view.createAndGoToReportTransactions(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGetNextFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void onGenerateReport(){
		if(currentItem != null) {
			switch(currentItem.type) {
			case PARAM:
				String[] param = view.getParameters();
				generateParametersReport(param);
				break;
			case PRINTSET:
				PrintSet printSet = view.getSelectedPrintSet();
				generatePrintSetReport(printSet);
				break;
			case TRANSACTIONSET:
				TransactionSet transactionSet = view.getSelectedTransactionSet();
				generateTransactionSetReport(transactionSet);
				break;
			}
		}
	}

	protected void generateParametersReport(String[] parameterValues){
		service.generateParamReport(currentItem.id, parameterValues, new BigBangAsyncCallback<Report>() {

			@Override
			public void onResponseSuccess(Report result) {
				onGenerateReportSuccess();
				showReport(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGenerateReportFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void generatePrintSetReport(PrintSet printSet){
		service.generatePrintSetReport(currentItem.id, printSet.id, new BigBangAsyncCallback<Report>() {

			@Override
			public void onResponseSuccess(Report result) {
				onGenerateReportSuccess();
				showReport(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGenerateReportFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void generateTransactionSetReport(TransactionSet transactionSet){
		service.generateTransactionSetReport(currentItem.id, transactionSet.id, new BigBangAsyncCallback<Report>() {

			@Override
			public void onResponseSuccess(Report result) {
				onGenerateReportSuccess();
				showReport(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onGenerateReportFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void showReport(Report report){
		view.clearReportSections();

		for(Section section : report.sections) {
			view.addReportSection(section);
		}
	}

	protected void onGenerateReportSuccess(){
		//TODO
	}

	protected void onGenerateReportFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gerar o Relatório"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onGetNextFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o item"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível proceder com a geração dos relatórios"), TYPE.ALERT_NOTIFICATION));
	}

}
