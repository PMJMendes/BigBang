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
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ViewPresenterFactory;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class AgendaViewPresenter implements ViewPresenter{

	public interface Display {
		HasValueSelectables<TaskStub> getTaskList();
		Widget asWidget();

		void refreshList();

		void addTaskListEntry(TaskStub task);
		void updateTaskListEntry(TaskStub task);
		void removeTaskListEntry(String taskId);

		void clear();
		void setScreenDescription(String description);
		void setSendToUserVisible(boolean b);
		HasClickHandlers getSendTaskButton();
		String getSelectedUserId();
		HasWidgets getContainer();

	}

	public static enum SectionOperation{
		TASKS, ORGANIZE_MAIL

	}

	private Display view;
	protected TasksBroker broker;
	protected Task currentTask;
	private boolean bound = false;
	ViewPresenter childPresenter;
	private HasParameters parameters;

	public AgendaViewPresenter(Display view) {
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
	}


	private String getViewPresenterIdForTask(Task task){
		return TasksViewPresenterMapper.getViewPresenterIdForObjectTypeId(task.objectTypeId);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameters = parameterHolder;
		String taskId = parameterHolder.getParameter("taskId");

		taskId = taskId == null ? new String() : taskId;

		if(taskId.isEmpty()){
			view.clear();
		}else{
			getTask(taskId);
		}
	}

	private void getTask(String taskId) {
		broker.getTask(taskId, new ResponseHandler<Task>() {

			@Override
			public void onResponse(Task response) {
				currentTask = response;
				if(response.status == Status.COMPLETED){
					view.setScreenDescription("Notificação");
					view.setSendToUserVisible(false);

					if(response.objectTypeId != null && response.objectTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.GENERAL_SYSTEM)){
						childPresenter = ViewPresenterFactory.getInstance().getViewPresenter("REPORT_TASKS");

					}else{
						childPresenter = ViewPresenterFactory.getInstance().getViewPresenter("TASKS_DISMISS");
					}	
					childPresenter.go(view.getContainer());
					childPresenter.setParameters(parameters);
				}else{
					String presenterId = getViewPresenterIdForTask(response);
					if(presenterId == null){
						onGetScreenFailed();
					}else{
						view.setSendToUserVisible(true);
						view.setScreenDescription(response.description);
						parameters.setParameter("id", response.objectIds[0]);
						childPresenter = ViewPresenterFactory.getInstance().getViewPresenter(presenterId);
						childPresenter.go(view.getContainer());
						childPresenter.setParameters(parameters);
					}

					if(childPresenter instanceof HasOperationPermissions){
						((HasOperationPermissions) childPresenter).setPermittedOperations(response.operationIds);

					}else{
						GWT.log("The ViewPresenter with id " + presenterId + " does not implement HasOperationPermissions");
					}
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetTaskFailed();
			}
		});
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

		view.getSendTaskButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sendTaskToNewUser();
			}
		});
	}

	protected void sendTaskToNewUser() {

		broker.reassignTask(currentTask.id, view.getSelectedUserId(), new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Tarefa enviada para outro utilizador"), TYPE.TRAY_NOTIFICATION));
				view.removeTaskListEntry(currentTask.id);
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("taskid");
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar a tarefa para outro utilizador"), TYPE.ALERT_NOTIFICATION));
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
					view.refreshList();
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
