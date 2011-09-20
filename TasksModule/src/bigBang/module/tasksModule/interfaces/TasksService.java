package bigBang.module.tasksModule.interfaces;

import bigBang.definitions.shared.Task;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("TasksService")
public interface TasksService
	extends SearchService
{
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

	public Task getTask(String clientId) throws SessionExpiredException, BigBangException;
}
