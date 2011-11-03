package bigBang.module.tasksModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.interfaces.TasksServiceAsync;

public class TasksBrokerImpl extends DataBroker<Task> implements TasksBroker {

	protected boolean refreshRequired = true;
	protected TasksServiceAsync service;
	protected SearchDataBroker<TaskStub> searchBroker;

	public TasksBrokerImpl(){
		this.service = TasksService.Util.getInstance();
		this.searchBroker = new TasksSearchDataBroker();
		this.dataElementId = BigBangConstants.EntityIds.TASK;
	}
	
	@Override
	public void requireDataRefresh() {
		this.refreshRequired = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		return;
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		return;
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		return;
	}

	@Override
	public void getTask(String id, final ResponseHandler<Task> handler) {
		service.getTask(id, new BigBangAsyncCallback<Task>() {

			@Override
			public void onSuccess(Task result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void getPendingTasksCount(final ResponseHandler<Integer> handler) {
		service.getPendingTasksCount(new BigBangAsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public SearchDataBroker<TaskStub> getSearchBroker() {
		return this.searchBroker;
	}

}
