package bigBang.module.tasksModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Task;
import bigBang.library.interfaces.SearchServiceAsync;

public interface TasksServiceAsync
	extends SearchServiceAsync
{
	void getTask(String clientId, AsyncCallback<Task> callback);
}
