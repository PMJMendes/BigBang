package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Exercise;
import bigBang.library.interfaces.SearchServiceAsync;

public interface PolicyExerciseServiceAsync
	extends SearchServiceAsync
{
	void getExercise(String exerciseId, AsyncCallback<Exercise> callback);
}
