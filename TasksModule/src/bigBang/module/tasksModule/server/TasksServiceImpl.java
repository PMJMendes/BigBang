package bigBang.module.tasksModule.server;

import bigBang.library.server.EngineImplementor;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.shared.Task;

public class TasksServiceImpl
	extends EngineImplementor
	implements TasksService
{
	private static final long serialVersionUID = 1L;

	public Task[] getTasks()
	{
		Task[] result = new Task[20];
		
		for(int i = 0; i < 20; i++){
			Task task = new Task();
			task.id = ""+i;
			task.operationId = i%2==0 ? "clientSearchOperation" : "clientMergeOperation";
			task.operationInstanceId = "123456";
			task.description = "Fusão de clientes " + i;
			task.entryDate = "2011-02-03 12:00:00";
			task.targetId = "clienteTeste";
			result[i] = task;
		}
		return result;
	}

	public boolean isSolved(String taskId)
	{
		return false;
	}
}
