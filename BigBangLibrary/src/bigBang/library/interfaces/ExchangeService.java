package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExchangeService")
public interface ExchangeService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ExchangeServiceAsync instance;
		public static ExchangeServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ExchangeService.class);
			}
			return instance;
		}
	}

	ExchangeItem[] getItems() throws SessionExpiredException, BigBangException;
}
