package bigBang.module.expenseModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExpenseService")
public interface ExpenseService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ExpenseServiceAsync instance;
		public static ExpenseServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ExpenseService.class);
			}
			return instance;
		}
	}
}
