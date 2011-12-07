package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.InsuredObject;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("PolicyObjectService")
public interface PolicyObjectService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static PolicyObjectServiceAsync instance;
		public static PolicyObjectServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(PolicyObjectService.class);
			}
			return instance;
		}
	}

	public InsuredObject getObject(String objectId) throws SessionExpiredException, BigBangException;
}
