package bigBang.module.receiptModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ReceiptService")
public interface ReceiptService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ReceiptServiceAsync instance;
		public static ReceiptServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ReceiptService.class);
			}
			return instance;
		}
	}
}
