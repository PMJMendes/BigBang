package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SubPolicyObjectService")
public interface SubPolicyObjectService
	extends PolicyObjectServiceOLD
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SubPolicyObjectServiceAsync instance;
		public static SubPolicyObjectServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SubPolicyObjectService.class);
			}
			return instance;
		}
	}
}
