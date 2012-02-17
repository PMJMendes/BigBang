package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.core.client.GWT;

public interface SubPolicyObjectService
	extends PolicyObjectService
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
