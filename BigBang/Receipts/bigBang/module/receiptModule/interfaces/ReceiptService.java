package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.Receipt;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

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
	
	public Receipt getReceipt(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt editReceipt(Receipt receipt) throws SessionExpiredException, BigBangException;

	public void deleteReceipt(String receiptId) throws SessionExpiredException, BigBangException;
}
