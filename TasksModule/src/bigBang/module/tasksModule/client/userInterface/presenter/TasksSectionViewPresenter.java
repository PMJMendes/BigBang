package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.event.OperationWasExecutedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.tasksModule.client.OperationToViewPresenterIdMapper;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;
import bigBang.module.tasksModule.client.dataAccess.TasksDataBrokerClient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TasksSectionViewPresenter implements ViewPresenter {

	public interface Display {
		HasValueSelectables<TaskStub> getTaskList();
		HasWidgets getOperationViewContainer();
		Widget asWidget();

		void addTaskListEntry(TaskStub task);
		void updateTaskListEntry(TaskStub task);
		void removeTaskListEntry(String taskId);
		
		void clear();
		void setScreenDescription(String description);
		void showErrorOnFetchingTask();
		void showErrorOnShowingScreen();
	}

	private Display view;
	protected TasksBroker broker;
	protected TasksDataBrokerClient brokerClient;
	protected Task currentTask;
	private boolean bound = false;

	public TasksSectionViewPresenter(Display view) {
		this.broker = (TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK);
		this.brokerClient = getTasksBrokerClient();
		this.broker.registerClient(brokerClient);
		setView((UIObject) view);
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
		initializeController();
	}
	
	private void initializeController(){
		new ViewPresenterController(view.getOperationViewContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				onParameters(historyItem);
			}
			
			@Override
			public void onParameters(HasParameters parameters) {
				if(parameters.getParameter("section").equalsIgnoreCase("tasks")){
					String taskId = parameters.getParameter("taskid");
					if(taskId == null){
						clearView();
					}else{
						showTaskWithId(taskId, parameters);
					}
				}
			}
			
			private void showTaskWithId(String taskId, final HasParameters parameters){
				TasksSectionViewPresenter.this.broker.getTask(taskId, new ResponseHandler<Task>() {

					@Override
					public void onResponse(Task response) {
						String presenterId = getViewPresenterIdForTask(response);
						if(presenterId == null){
							view.showErrorOnShowingScreen();
							view.setScreenDescription("");
							clearPresentation();
						}else{
							view.setScreenDescription(response.description);
							parameters.setParameter("id", response.objectIds[0]);
							
							ViewPresenter presenter = present(presenterId, parameters, true);
							
							if(presenter instanceof HasOperationPermissions){
								((HasOperationPermissions) presenter).setPermittedOperations(response.operationIds);
							}else{
								GWT.log("The ViewPresenter with id " + presenterId + " does not implement HasOperationPermissions");
							}
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						view.showErrorOnFetchingTask();
						clearView();
					}
				});
			}
			
			private void clearView(){
				Collection<ValueSelectable<TaskStub>> selected = view.getTaskList().getSelected();
				if(!selected.isEmpty()){
					view.getTaskList().clearSelection();
				}
				this.clearPresentation();
				view.clear();
			}
		};
	}

	private String getViewPresenterIdForTask(Task task){
		String result = null;
		for(int i = 0; i < task.operationIds.length; i++) {
			if(result != null && !result.equalsIgnoreCase(task.operationIds[i])){
				GWT.log("There is more than one presenter implementation for the operations required by the task with id:" + task.id);
			}
			result = OperationToViewPresenterIdMapper.getViewPresenterIdForOperationId(task.operationIds[i]);
		}
		return result;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		return;
	}
	
	public void bind() {
		if(bound){return;}
		bound = true;
		this.view.getTaskList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<TaskStub> selected = (ValueSelectable<TaskStub>) event.getFirstSelected();
				TaskStub selectedValue = selected == null ? null : selected.getValue();
				NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
				if(selectedValue == null){
					navigationItem.removeParameter("taskId");
				}else{
					navigationItem.setParameter("taskId", selectedValue.id);
				}
				NavigationHistoryManager.getInstance().go(navigationItem);
			}
		});

		//APPLICATION-WIDE EVENTS
		EventBus.getInstance().addHandler(OperationWasExecutedEvent.TYPE, new OperationWasExecutedEventHandler() {

			@Override
			public void onOperationWasExecuted(String operationId, String processId) {
				if(currentTask != null){
					//TODO CHECKS
					broker.getTask(currentTask.id, new ResponseHandler<Task>() {

						@Override
						public void onResponse(Task response) {
						}

						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
				}
			}
		});
	}

	private TasksDataBrokerClient getTasksBrokerClient(){
		return new TasksDataBrokerClient() {
			
			private int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				this.version = number;
			}

			@Override
			public int getDataVersion(String dataElementId) {
				return this.version;
			}

			@Override
			public void updateTask(Task task) {
				//TODO
			}

			@Override
			public void removeTask(String id) {
				//TODO
			}

			@Override
			public void addTask(Task task) {
				// TODO Auto-generated method stub

			}
		};
	}

}
