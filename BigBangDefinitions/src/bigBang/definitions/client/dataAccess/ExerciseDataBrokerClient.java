package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Exercise;

public interface ExerciseDataBrokerClient extends DataBrokerClient<Exercise> {

	public void addExercise(Exercise exercise);

	public void updateExercise(Exercise exercise);

	public void removeExercise(String id);

}
