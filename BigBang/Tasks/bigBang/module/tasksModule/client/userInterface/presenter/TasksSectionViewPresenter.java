package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.definitions.shared.TaskStub.Status;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.event.OperationWasExecutedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.tasksModule.client.TasksViewPresenterMapper;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;

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
	}

	private Display view;
	protected TasksBroker broker;
	protected Task currentTask;
	private ViewPresenterController controller;
	private boolean bound = false;

	public TasksSectionViewPresenter(Display view) {
		this.broker = (TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK);
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
		this.controller = new ViewPresenterController(view.getOperationViewContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}

			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				section = section == null ? new String() : section;

				if(!parameters.isStackParameter("display")){
					NavigationHistoryItem item = new NavigationHistoryItem();
					item.setStackParameter("display");
					NavigationHistoryManager.getInstance().go(item);
				}else{
					if(section.equalsIgnoreCase("tasks")){
						String taskId = parameters.getParameter("taskid");
						taskId = taskId == null ? new String() : taskId;
						if(taskId.isEmpty()){
							currentTask = null;
							clearView();
						}else{
							showTaskWithId(taskId, parameters);
						}
					}
				}
			}

			private void showTaskWithId(String taskId, final HasParameters parameters){
				TasksSectionViewPresenter.this.broker.getTask(taskId, new ResponseHandler<Task>() {

					@Override
					public void onResponse(Task response) {
						currentTask = response;
						if(response.status == Status.COMPLETED){
							view.setScreenDescription("Notificação");
							present("TASKS_DISMISS", parameters, true);
						}else{
							String presenterId = getViewPresenterIdForTask(response);
							if(presenterId == null){
								onGetScreenFailed();
							}else {
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
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetTaskFailed();
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
		return TasksViewPresenterMapper.getViewPresenterIdForObjectTypeId(task.objectTypeId);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
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
					navigationItem.removeParameter("taskid");
				}else{
					navigationItem.setParameter("taskid", selectedValue.id);
				}
				NavigationHistoryManager.getInstance().go(navigationItem);
			}
		});

		//APPLICATION-WIDE EVENTS
		EventBus.getInstance().addHandler(OperationWasExecutedEvent.TYPE, new OperationWasExecutedEventHandler() {

			@Override
			public void onOperationWasExecuted(String operationId, String processId) {
				operationWasExecuted(operationId, processId);
			}
		});
	}

	private void operationWasExecuted(String operationId, String objectId){
		if(currentTask != null && currentTask.objectIds[0].equalsIgnoreCase(objectId)){
			for(int i = 0; i < currentTask.operationIds.length; i++) {
				if(currentTask.operationIds[i].equalsIgnoreCase(operationId)){
					view.removeTaskListEntry(currentTask.id);
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("taskid");
					NavigationHistoryManager.getInstance().go(item);
					break;
				}
			}
		}
	}

	private void onGetTaskFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o item de agenda, neste momento"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("taskid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetScreenFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o item de agenda, neste momento"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("taskid");
		NavigationHistoryManager.getInstance().go(item);
	}

}
