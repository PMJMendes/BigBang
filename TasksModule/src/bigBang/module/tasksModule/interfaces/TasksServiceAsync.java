package bigBang.module.tasksModule.interfaces;

import bigBang.library.interfaces.Service;
import bigBang.module.tasksModule.shared.Task;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TasksServiceAsync extends Service{

	void getTasks(AsyncCallback<Task[]> callback);

	void isSolved(String taskId, AsyncCallback<Boolean> callback);

}
