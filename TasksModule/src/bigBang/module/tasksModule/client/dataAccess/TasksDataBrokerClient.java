package bigBang.module.tasksModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.Task;

public interface TasksDataBrokerClient extends DataBrokerClient<Task> {
	
	public void addTask(Task task);
	public void removeTask(String id);
	public void updateTask(Task task);
	
}
