package bigBang.library.client.dataAccess;

import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SortParameter;

public class SearchDataBrokerImpl<T extends SearchResult> extends DataBroker<T> implements SearchDataBroker<T> {

	protected SearchServiceAsync service;
	protected Map<String, Search<T>> workspaces; 

	public SearchDataBrokerImpl(SearchServiceAsync service){
		this.service = service;
		workspaces = new HashMap<String, Search<T>>();
	}

	@Override
	public void requireDataRefresh() {}

	@Override
	public void search(final SearchParameter[] parameters, final SortParameter[] sorts, int size,
			final ResponseHandler<Search<T>> handler) {
		this.service.openSearch(parameters, sorts, size, new BigBangAsyncCallback<NewSearchResult>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(NewSearchResult result) {
				Search<T> search = new Search<T>(result.workspaceId, result.totalCount, 0, result.results.length, parameters, sorts, (T[]) result.results);
				workspaces.put(result.workspaceId, search);
				handler.onResponse(search);
			}
		});
	}

	@Override
	public void search(final String workspaceId, final SearchParameter[] parameters, final SortParameter[] sorts,
			final int size, final ResponseHandler<Search<T>> handler) {
		if(workspaces.containsKey(workspaceId)){
			this.service.search(workspaceId, parameters, sorts, size, new BigBangAsyncCallback<NewSearchResult>() {

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(NewSearchResult result) {
					Search<T> search = new Search<T>(result.workspaceId, result.totalCount, 0, result.results.length, parameters, sorts, (T[]) result.results);
					workspaces.put(workspaceId, search);
					handler.onResponse(search);
				}
			});
		}
	}

	@Override
	public void getResults(final String workspaceId, final int offset, int count,
			final ResponseHandler<Search<T>> handler) {
		this.service.getResults(workspaceId, offset, count, new BigBangAsyncCallback<SearchResult[]>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(SearchResult[] result) {
				Search<T> currentSearch = workspaces.get(workspaceId);
				Search<T> search = new Search<T>(workspaceId, currentSearch.totalResultsCount, offset, result.length, currentSearch.parameters, currentSearch.getSortParameters(), (T[]) result);
				workspaces.put(workspaceId, search);
				handler.onResponse(search);
			}
		});
	}

	@Override
	public void disposeSearch(String workspaceId) {
		workspaces.remove(workspaceId);
	}

	@Override
	public boolean hasMoreResults(String workspaceId) {
		Search<T> search = workspaces.get(workspaceId);
		return (search.totalResultsCount - search.offset - search.getCount()) > 0;
	}

}
