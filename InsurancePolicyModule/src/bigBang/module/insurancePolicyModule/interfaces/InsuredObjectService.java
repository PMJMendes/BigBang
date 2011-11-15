package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsuredObjectService")
public interface InsuredObjectService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static InsuredObjectServiceAsync instance;
		public static InsuredObjectServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(InsuredObjectService.class);
			}
			return instance;
		}
	}

}
