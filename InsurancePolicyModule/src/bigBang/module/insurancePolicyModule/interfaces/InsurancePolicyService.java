package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsurancePolicyService")
public interface InsurancePolicyService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static InsurancePolicyServiceAsync instance;
		public static InsurancePolicyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(InsurancePolicyService.class);
			}
			return instance;
		}
	}
}
