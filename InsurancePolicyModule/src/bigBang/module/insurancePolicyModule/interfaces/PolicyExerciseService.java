package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("PolicyExerciseService")
public interface PolicyExerciseService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static PolicyExerciseServiceAsync instance;
		public static PolicyExerciseServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(PolicyExerciseService.class);
			}
			return instance;
		}
	}

}
