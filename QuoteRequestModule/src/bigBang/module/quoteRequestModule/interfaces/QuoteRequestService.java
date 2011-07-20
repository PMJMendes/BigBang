package bigBang.module.quoteRequestModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("QuoteRequestService")
public interface QuoteRequestService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static QuoteRequestServiceAsync instance;
		public static QuoteRequestServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(QuoteRequestService.class);
			}
			return instance;
		}
	}
}
