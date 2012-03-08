package bigBang.library.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBrokerInterface;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;

public interface SubPolicyExerciseDataBroker extends
		DataBrokerInterface<Exercise> {

	public void getSubPolicyExercise(String exerciseId, String subPolicyId, ResponseHandler<Exercise> handler);
	
	public void getSubPolicyExercises(String subPolicyId, ResponseHandler<Collection<ExerciseStub>> handler);
	
}
