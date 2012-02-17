package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Exercise;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;

public interface SubPolicyExerciseService
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SubPolicyExerciseServiceAsync instance;
		public static SubPolicyExerciseServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SubPolicyExerciseService.class);
			}
			return instance;
		}
	}

	public Exercise getExercise(String exerciseId, String subPolicyId) throws SessionExpiredException, BigBangException;
}
