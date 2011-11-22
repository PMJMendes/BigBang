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
import bigBang.library.client.event.ScreenInvokedEvent;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;
import bigBang.module.tasksModule.client.dataAccess.TasksDataBrokerClient;
import bigBang.module.tasksModule.client.event.NumberOfTasksUpdateEvent;
import bigBang.module.tasksModule.shared.TaskSearchParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter.SortableField;

import com.google.gwt.user.client.Timer;

public class TasksNotificationsManager extends Timer implements TasksDataBrokerClient {

	final int DELAY = 1000 * 10 * 60; //miliseconds
	private EventBus eventBus;
	protected TasksBroker broker;
	protected int currentCount = 0;
	protected String lastTimestamp = null;

	public TasksNotificationsManager(EventBus eventBus){
		this.eventBus = eventBus;
		this.broker = ((TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK));
	}

	public void run() {
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
				for(TaskStub t : response.getResults()){
					broker.notifyItemCreation(t.id);
					lastTimestamp = t.timeStamp;
					fireTaskNotification(t);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
		this.schedule(this.DELAY);
	}

	protected void updateTasksCount(int count){
		eventBus.fireEvent(new NumberOfTasksUpdateEvent(count));
	}

	protected void fireTaskNotification(TaskStub t){
		Notification notification = new Notification("Agenda", "Foi-lhe atribuída uma nova tarefa:\n"+t.description, new ScreenInvokedEvent(null, TasksSection.ID, null));
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

}
