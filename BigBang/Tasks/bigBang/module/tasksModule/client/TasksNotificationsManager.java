package bigBang.module.tasksModule.client;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.event.OperationWasExecutedEventHandler;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;
import bigBang.module.tasksModule.client.dataAccess.TasksDataBrokerClient;
import bigBang.module.tasksModule.client.event.NumberOfTasksUpdateEvent;
import bigBang.module.tasksModule.shared.TaskSearchParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter.SortableField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class TasksNotificationsManager extends Timer implements TasksDataBrokerClient {

	final int DELAY = 1000 * 10 * 60; //miliseconds
	private EventBus eventBus;
	protected TasksBroker broker;
	protected int currentCount = 0;
	protected String lastTimestamp = null;
	protected boolean running = false;

	public TasksNotificationsManager(){
		this.eventBus = EventBus.getInstance();
		this.broker = ((TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK));
		bindToEvents();
	}

	public void run() {
		this.running = true;
		updatePendingTasksCount();
		this.schedule(this.DELAY);
	}

	protected void updateTasksCount(int count){
		eventBus.fireEvent(new NumberOfTasksUpdateEvent(count));
	}

	protected void fireTaskNotification(TaskStub t){
		Notification notification = new Notification("Agenda", "Foi-lhe atribuída uma nova tarefa:\n"+t.description);
		this.eventBus.fireEvent(new NewNotificationEvent(notification,
				Notification.TYPE.TRAY_NOTIFICATION));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return -1;
	}

	@Override
	public void addTask(Task task) {
		this.currentCount++;
		this.updateTasksCount(this.currentCount);
	}

	@Override
	public void removeTask(String id) {
		this.currentCount--;
		this.updateTasksCount(this.currentCount);
	}

	@Override
	public void updateTask(Task task) {
		return;
	}
	
	private void updatePendingTasksCount(){
		this.broker.getPendingTasksCount(new ResponseHandler<Integer>() {

			@Override
			public void onResponse(Integer response) {
				TasksNotificationsManager.this.updateTasksCount(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});

		TaskSearchParameter param = new TaskSearchParameter();
		param.afterTimestamp = this.lastTimestamp;
		SearchParameter[] parameters = new TaskSearchParameter[]{
				param
		};
		SortParameter[] sorts = new SortParameter[]{
				new TaskSortParameter(SortableField.CREATION_DATE, SortOrder.ASC)
		};

		this.broker.getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<TaskStub>>() {

			@Override
			public void onResponse(Search<TaskStub> response) {
				String lastTimestamp = null;
				for(TaskStub t : response.getResults()){
					lastTimestamp = t.timeStamp;
					if(TasksNotificationsManager.this.lastTimestamp != null){
						broker.notifyItemCreation(t.id);
						fireTaskNotification(t);
					}
				}
				TasksNotificationsManager.this.lastTimestamp = lastTimestamp;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				GWT.log("Could not get new task notifications");
			}
		}, true);
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	private void bindToEvents(){
		EventBus.getInstance().addHandler(OperationWasExecutedEvent.TYPE, new OperationWasExecutedEventHandler() {
			
			@Override
			public void onOperationWasExecuted(String operationId, String processId) {
				updatePendingTasksCount();
			}
		});
	}

}
