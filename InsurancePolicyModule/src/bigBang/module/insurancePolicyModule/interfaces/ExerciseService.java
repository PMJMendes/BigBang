package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExerciseService")
public interface ExerciseService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ExerciseServiceAsync instance;
		public static ExerciseServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ExerciseService.class);
			}
			return instance;
		}
	}

}
