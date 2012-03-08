package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Exercise;

public interface SubPolicyExerciseDataBrokerClient extends
		DataBrokerClient<Exercise> {

	public void addExercise(String ownerId, Exercise exercise);
	
	public void updateExercise(String ownerId, Exercise exercise);
	
	public void removeExercise(String exerciseId);
	
}
