package bigBang.library.client.dataAccess;

import bigBang.library.client.response.ResponseHandler;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;

public interface SearchDataBroker<T extends SearchResult> extends DataBrokerInterface<T> {

	/**
	 * Opens and performs a search operation
	 * @param parameters The parameters for the search
	 * @param size How many results will be returned
	 * @param handler The handler to be notified on  response
	 */
	public void search(SearchParameter[] parameters, int size, ResponseHandler<Search<T>> handler);

	/**
	 * Performs a search in an already open workspace
	 * @param workspaceId The id of the workspace in which the search is being performed 
	 * @param parameters The parameters for the search
	 * @param size How many results will be returned
	 * @param handler The handler to be notified on response
	 */
	public void search(String workspaceId, SearchParameter[] parameters, int size, ResponseHandler<Search<T>> handler);
	
	/**
	 * Gets the search results from a current search within a given range
	 * @param workspaceId The id of the workspace in which the search is being performed 
	 * @param from The index of the first result to be returned
	 * @param to The index of the last result to be returned
	 * @param handler The handler to be notified on response
	 */
	public void getResults(String workspaceId, int offset, int count, ResponseHandler<Search<T>> handler);
	
	/**
	 * Returns whether or not there are any results that have not yet been fetched
	 * @param workspaceId The id of the workspace
	 * @return true if there are any results left and false otherwise
	 */
	public boolean hasMoreResults(String workspaceId);
	
	/**
	 * Disposes of the resources required for the search
	 * @param workspaceId The id of the workspace whose resources will be disposed of
	 */
	public void disposeSearch(String workspaceId);

}
