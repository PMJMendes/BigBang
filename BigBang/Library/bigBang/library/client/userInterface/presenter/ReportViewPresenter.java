package bigBang.library.client.userInterface.presenter;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.shared.PrintSet;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.Report.Section;
import bigBang.definitions.shared.Report.Section.Verb;
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
import bigBang.library.client.event.NavigationRequestEvent.Navigation;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.ReportService;
import bigBang.library.interfaces.ReportServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReportViewPresenter implements ViewPresenter {

	public static enum Action {
		GENERATE_REPORT, 
		PRINT_SET_SELECTION_CHANGED, 
		TRANSACTION_SET_SELECTION_CHANGED,
		PRINT_REPORT,
		EXPORT_EXCEL,
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
		NavigationPanel getNavigationPanel();
		Button addVerb(String title);
		Frame getPrintFrame();
		Frame getExcelFrame();
		
		void allowPrintReport(boolean allow);
		void allowExportExcel(boolean allow);
	}

	protected boolean bound;
	protected ReportServiceAsync service;
	protected Display view;
	protected String processTypeId;
	protected ReportItem currentItem;
	protected ClickHandler handler;
	protected Map<Button, Verb> buttonMap;

	protected String currentPrintFileId, currentExcelFileId;

	public ReportViewPresenter(Display view){
		setView((UIObject)view);
		service = ReportService.Util.getInstance();
		buttonMap = new HashMap<Button, Report.Section.Verb>();
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
				case PRINT_REPORT:
					printCurrentReport();
					break;
				case EXPORT_EXCEL:
					exportCurrentReportExcel();
					break;
				}
			}
		});

		view.getNavigationPanel().navBar.addNavigationEventHandler(new NavigationRequestEventHandler() {

			@Override
			public void onNavigationEvent(NavigationRequestEvent event) {
				if(event.getNavigationCommand().equals(Navigation.PREVIOUS)){
					view.getPrintFrame().setUrl("");
					view.clearReportSections();
					view.allowPrintReport(false);
					view.allowExportExcel(false);
				}
			}
		});

		handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Button temp = (Button) event.getSource();
				temp.setText("A processar");
				temp.setEnabled(false);

				service.RunVerb(buttonMap.get(temp).argument, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Operação executada com sucesso!"), TYPE.TRAY_NOTIFICATION));
						temp.setText(buttonMap.get(temp).label);
					}

					@Override
					public void onFailure(Throwable caught) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível executar a operação."), TYPE.ALERT_NOTIFICATION));
						temp.setText(buttonMap.get(temp).label);
						temp.setEnabled(true);
					}
				});				
			}
		};

		view.getPrintFrame().addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				if ( currentPrintFileId != null ) {
					String str = currentPrintFileId;
					currentPrintFileId = null;
					print();
					FileService.Util.getInstance().Discard(str, new AsyncCallback<Void>() {
	
						@Override
						public void onFailure(Throwable caught) {
						}
	
						@Override
						public void onSuccess(Void result) {
						}
					});
				}
			}
		});
		
		view.getExcelFrame().addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				if ( currentExcelFileId != null ) {
					String str = currentExcelFileId;
					currentExcelFileId = null;
					FileService.Util.getInstance().Discard(str, new AsyncCallback<Void>() {
	
						@Override
						public void onFailure(Throwable caught) {
						}
	
						@Override
						public void onSuccess(Void result) {
						}
					});
				}
			}
		});

		this.bound = true;
	}

	protected void onTransactionSetSelectionChanged() {
		if(view.getSelectedTransactionSet() != null){
			onGenerateReport();
		}
	}

	protected void onPrintSetSelectionChanged() {

		if(!view.getPrintSetPanel().getSelected().isEmpty()){
			onGenerateReport();
		}

	}

	protected void clearView() {
		view.goHomeAndClear();
		currentItem = null;
		view.getPrintFrame().setUrl("");
		view.clearReportSections();
		view.allowPrintReport(false);
		view.allowExportExcel(false);
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
						break;
					case PARAM:
						showParamReport(item);
						break;
					case PRINTSET:
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
				onGenerateReportFailure(caught);
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
				onGenerateReportFailure(caught);
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
				onGenerateReportFailure(caught);
				super.onResponseFailure(caught);
			}
		});
	}

	protected void showReport(Report report){
		view.getPrintFrame().setUrl("");
		view.clearReportSections();

		for(Section section : report.sections) {
			view.addReportSection(section);
			for(Verb verb : section.verbs){
				Button but = view.addVerb(verb.label);
				buttonMap.put(but, verb);
				but.addClickHandler(handler);
			}
		}
		view.allowPrintReport(true);
		view.allowExportExcel(true);
	}

	protected void printCurrentReport(){
		BigBangAsyncCallback<String> printGenerationCallback = new BigBangAsyncCallback<String>() {

			@Override
			public void onResponseSuccess(String result) {
				currentPrintFileId = result;
				Frame frame = ReportViewPresenter.this.view.getPrintFrame();
				frame.setUrl(GWT.getModuleBaseURL() + "bbfile?fileref=" + result);
			}
			@Override
			public void onResponseFailure(Throwable caught) {
				onPrintReportFailure();
				super.onResponseFailure(caught);
			}
		};

		switch(currentItem.type) {
		case PARAM:
			service.generateParamAsHTML(currentItem.id, view.getParameters(), printGenerationCallback);
			break;
		case PRINTSET:
			service.generatePrintSetAsHTML(currentItem.id, view.getSelectedPrintSet().id, printGenerationCallback);
			break;
		case TRANSACTIONSET:
			service.generateTransSetAsHTML(currentItem.id, view.getSelectedTransactionSet().id, printGenerationCallback);
			break;
		}
	}
	
	protected void exportCurrentReportExcel(){
		BigBangAsyncCallback<String> exportExcelCallback = new BigBangAsyncCallback<String>() {

			@Override
			public void onResponseSuccess(String result) {
				currentExcelFileId = result;
				Frame frame = ReportViewPresenter.this.view.getExcelFrame();
				frame.setUrl(GWT.getModuleBaseURL() + "bbfile?fileref=" + result);
			}
			@Override
			public void onResponseFailure(Throwable caught) {
				onExportReportFailure();
				super.onResponseFailure(caught);
			}
		};
		switch(currentItem.type) {
		case PARAM:
			service.generateParamAsXL(currentItem.id, view.getParameters(), exportExcelCallback);
			break;
		case PRINTSET:
			service.generatePrintSetAsXL(currentItem.id, view.getSelectedPrintSet().id, exportExcelCallback);
			break;
		case TRANSACTIONSET:
			service.generateTransSetAsXL(currentItem.id, view.getSelectedTransactionSet().id, exportExcelCallback);
			break;
		}
	}

	protected void print(){
		print(view.getPrintFrame().getElement());
	}

	protected native void print(Element frame) /*-{
		 frame = frame.contentWindow;
         frame.focus();
         frame.print();
	}-*/;

	protected void onGenerateReportSuccess(){
		//TODO
	}

	protected void onPrintReportFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível imprimir o Relatório"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onExportReportFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível exportar o Relatório"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onGenerateReportFailure(Throwable caught){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gerar o Relatório " + caught.getMessage()), TYPE.ALERT_NOTIFICATION));
	}

	protected void onGetNextFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o item"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível proceder com a geração dos relatórios"), TYPE.ALERT_NOTIFICATION));
	}

}
