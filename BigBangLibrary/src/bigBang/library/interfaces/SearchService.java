package bigBang.library.interfaces;

import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SearchService")
public interface SearchService extends RemoteService, Service {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SearchServiceAsync instance;
		public static SearchServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SearchService.class);
			}
			return instance;
		}
	}

	SearchResult[] search(String workspaceId, SearchParameter parameter, int size);

}
