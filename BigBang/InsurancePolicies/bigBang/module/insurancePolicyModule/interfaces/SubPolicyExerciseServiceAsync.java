package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Exercise;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubPolicyExerciseServiceAsync
	extends SearchServiceAsync
{
	void getExercise(String exerciseId, String subPolicyId, AsyncCallback<Exercise> callback);
}
