package bigBang.library.interfaces;

import java.util.HashMap;

import bigBang.library.client.SearchResult;

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

	SearchResult[] search(HashMap<String, String> filters, String[] requiredFields, String searchTerms);

}
