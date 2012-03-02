package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;

public interface ExerciseDataBroker extends DataBrokerInterface<Exercise> {

	public void createExercise(String ownerId, ResponseHandler<Exercise> handler);

	public void getExercise(String id, ResponseHandler<Exercise> handler);
	
	public void updateExercise(Exercise exercise, ResponseHandler<Exercise> handler);
	
	public void deleteExercise(String exerciseId, ResponseHandler<Void> handler);

	public void getProcessExercises(String ownerId, ResponseHandler<Collection<ExerciseStub>> handler);

	public SearchDataBroker<ExerciseStub> getSearchBroker();

	public void remapItemId(String oldId, String newId, boolean newIdInScratchPad);

}
