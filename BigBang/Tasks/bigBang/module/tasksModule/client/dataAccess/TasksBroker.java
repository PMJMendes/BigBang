package bigBang.module.tasksModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;

public interface TasksBroker extends DataBrokerInterface<Task> {

	public void getTask(String id, ResponseHandler<Task> handler);
	public void getPendingTasksCount(ResponseHandler<Integer> handler);
	public void dismissTask(String taskId, ResponseHandler<Void> handler);
	public SearchDataBroker<TaskStub> getSearchBroker();
	public void reassignTask(String taskId, String userId, ResponseHandler<Void> handler);

}
