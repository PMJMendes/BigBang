package bigBang.module.tasksModule.client.dataAccess;

import java.util.Collection;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
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
		this.getTask(itemId, new ResponseHandler<Task>() {

			@Override
			public void onResponse(Task response) {
				for(DataBrokerClient<Task> c : TasksBrokerImpl.this.clients) {
					TasksDataBrokerClient b = (TasksDataBrokerClient)c;
					b.addTask(response);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		for(DataBrokerClient<Task> c : TasksBrokerImpl.this.clients) {
			TasksDataBrokerClient b = (TasksDataBrokerClient)c;
			b.removeTask(itemId);
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getTask(itemId, new ResponseHandler<Task>() {

			@Override
			public void onResponse(Task response) {
				for(DataBrokerClient<Task> c : TasksBrokerImpl.this.clients) {
					TasksDataBrokerClient b = (TasksDataBrokerClient)c;
					b.updateTask(response);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getTask(String id, final ResponseHandler<Task> handler) {
		service.getTask(id, new BigBangAsyncCallback<Task>() {

			@Override
			public void onResponseSuccess(Task result) {
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the task")
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getPendingTasksCount(final ResponseHandler<Integer> handler) {
		service.getPendingTasksCount(new BigBangAsyncCallback<Integer>() {

			@Override
			public void onResponseSuccess(Integer result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public SearchDataBroker<TaskStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void dismissTask(final String taskId, final ResponseHandler<Void> handler) {
		this.service.dismissTask(taskId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				notifyItemDeletion(taskId);
				handler.onResponse(null);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not dismiss the task")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void reassignTask(String taskId, String userId,
			final ResponseHandler<Void> handler) {
		this.service.reassignTask(taskId, userId, new BigBangAsyncCallback<Void>() {
			
			@Override
			public void onResponseSuccess(Void result) {
				handler.onResponse(null);
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not reassign the task")	
				});
				super.onResponseFailure(caught);
			}
		});
		
	}

}
