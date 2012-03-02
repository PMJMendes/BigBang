package bigBang.module.tasksModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.tasksModule.interfaces.TasksService;

public class TasksSearchDataBroker extends SearchDataBrokerImpl<TaskStub> implements SearchDataBroker<TaskStub> {

	public TasksSearchDataBroker(){
		this(TasksService.Util.getInstance());
	}
	
	public TasksSearchDataBroker(SearchServiceAsync service) {
		super(service);
	}
	
	@Override
	public void search(SearchParameter[] parameters, SortParameter[] sorts,
			int size, final  ResponseHandler<Search<TaskStub>> handler) {
		super.search(parameters, sorts, size, new ResponseHandler<Search<TaskStub>>() {

			@Override
			public void onResponse(Search<TaskStub> response) {
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

}
