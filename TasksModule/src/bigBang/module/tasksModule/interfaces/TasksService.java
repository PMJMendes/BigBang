package bigBang.module.tasksModule.interfaces;

import bigBang.module.tasksModule.shared.Task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TasksService")
public interface TasksService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static TasksServiceAsync instance;
		public static TasksServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(TasksService.class);
			}
			return instance;
		}
	}
	
	public Task[] getTasks();
	
	public boolean isSolved(String taskId);
}
