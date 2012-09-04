package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Report;
import bigBang.definitions.shared.Report.Section;
import bigBang.definitions.shared.Task;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.ReportService;
import bigBang.library.interfaces.ReportServiceAsync;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReportTasksViewPresenter implements ViewPresenter {

	public static enum Action {
		GO_TO_REPORT,
		DISMISS
	}

	public static interface Display {
		void clearReportSections();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void addReportSection(Section section);
	}

	protected String reportTaskId;
	protected Display view;
	protected TasksBroker broker;
	protected ReportServiceAsync reportService = GWT.create(ReportService.class);

	public ReportTasksViewPresenter(Display view) {
		this.broker = (TasksBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TASK);
		setView((UIObject)view);
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
		this.reportTaskId = parameterHolder.getParameter("taskid");
		this.reportTaskId = this.reportTaskId == null ? new String() : this.reportTaskId;

		if(this.reportTaskId.isEmpty()){
			clearView();
		}else{
			showReportTask(this.reportTaskId);
		}
	}

	protected void clearView(){
		view.clearReportSections();
	}

	protected void showReportTask(String id){
		broker.getTask(id, new ResponseHandler<Task>() {

			@Override
			public void onResponse(Task response) {
				if(response.reportId != null){
					generateParametersReport(response.reportId, response.params);
				}else{
					onFailure();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}

	protected void generateParametersReport(String reportId, String[] parameterValues){
		reportService.generateParamReport(reportId, parameterValues, new BigBangAsyncCallback<Report>() {

			@Override
			public void onResponseSuccess(Report result) {
				showReport(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				ReportTasksViewPresenter.this.onFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void bind() {
		view.registerActionHandler(new ActionInvokedEventHandler<ReportTasksViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case GO_TO_REPORT:
					onNavigateToReport();
					break;
				case DISMISS:
					onDismiss();
					break;
				}
			}
		});
	}
	
	protected void showReport(Report report){
		view.clearReportSections();
		
		for(Section section : report.sections) {
			view.addReportSection(section);
		}
	}

	protected void onNavigateToReport(){
		//TODO
	}

	protected void onDismiss(){
		broker.dismissTask(reportTaskId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				reportTaskId = null;
				onDismissTaskSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onDismissTaskFailed();
			}
		});
	}

	private void onDismissTaskSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Tomada de Conhecimento foi notificada"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("taskid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o Relatório"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("taskid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onDismissTaskFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível notificar o sistema da Tomada de Conhecimento"), TYPE.ALERT_NOTIFICATION));
	}

}
