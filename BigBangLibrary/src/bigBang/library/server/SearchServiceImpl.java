package bigBang.library.server;

import java.util.HashMap;

import bigBang.library.client.SearchResult;
import bigBang.library.interfaces.SearchService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {

	private static final long serialVersionUID = 1L;

	public SearchResult[] search(HashMap<String, String> filters, String[] requiredFields, String searchTerms) {
		SearchResult[] result = new SearchResult[15];
		for(int i = 0; i  < result.length; i++){
			result[i] = new SearchResult();
		}
		return result;
	}
}
