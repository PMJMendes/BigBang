package bigBang.module.tasksModule.shared;

import bigBang.library.shared.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TasksServiceAsync extends Service{

	void getTasks(AsyncCallback<Task[]> callback);

	void isSolved(String taskId, AsyncCallback<Boolean> callback);

}
