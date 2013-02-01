package bigBang.module.quoteRequestModule.interfaces;

import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("QuoteRequestObjectService")
public interface QuoteRequestObjectService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static QuoteRequestObjectServiceAsync instance;
		public static QuoteRequestObjectServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(QuoteRequestObjectService.class);
			}
			return instance;
		}
	}

	public QuoteRequestObject getObject(String objectId) throws SessionExpiredException, BigBangException;
}
