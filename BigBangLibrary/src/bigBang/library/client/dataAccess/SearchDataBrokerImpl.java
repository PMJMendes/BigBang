package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.library.shared.NewSearchResult;

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
			public void onResponseSuccess(NewSearchResult result) {
				Collection<T> resultsCollection = new ArrayList<T>();
				for(int i = 0; i < result.results.length; i++)
					resultsCollection.add((T) result.results[i]);
				Search<T> search = new Search<T>(result.workspaceId, result.totalCount, 0, result.results.length, parameters, sorts, resultsCollection);
				workspaces.put(result.workspaceId, search);
				handler.onResponse(search);
			}
		});
	}
	
	@Override
	public void searchOpenForOperation(String operationId,
			final SearchParameter[] parameters, final SortParameter[] sorts, int size,
			final ResponseHandler<Search<T>> handler) {
		this.service.openForOperation(operationId, parameters, sorts, size, new BigBangAsyncCallback<NewSearchResult>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onResponseSuccess(NewSearchResult result) {
				Collection<T> resultsCollection = new ArrayList<T>();
				for(int i = 0; i < result.results.length; i++)
					resultsCollection.add((T) result.results[i]);
				Search<T> search = new Search<T>(result.workspaceId, result.totalCount, 0, result.results.length, parameters, sorts, resultsCollection);
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
				public void onResponseSuccess(NewSearchResult result) {
					Collection<T> resultsCollection = new ArrayList<T>();
					for(int i = 0; i < result.results.length; i++)
						resultsCollection.add((T) result.results[i]);
					Search<T> search = new Search<T>(result.workspaceId, result.totalCount, 0, result.results.length, parameters, sorts, resultsCollection);
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
			public void onResponseSuccess(SearchResult[] result) {
				Search<T> currentSearch = workspaces.get(workspaceId);
				Collection<T> resultsCollection = new ArrayList<T>();
				for(int i = 0; i < result.length; i++)
					resultsCollection.add((T) result[i]);
				Search<T> search = new Search<T>(workspaceId, currentSearch.getTotalResultsCount(), offset, result.length, currentSearch.getParameters(), currentSearch.getSortParameters(), resultsCollection);
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
		return (search.getTotalResultsCount() - search.getOffset() - search.getCount()) > 0;
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

}
