package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;

public interface ExerciseDataBroker extends DataBrokerInterface<Exercise> {

		public void getExercise(String id, ResponseHandler<Exercise> handler);
		
		public void getProcessExercises(String ownerId, ResponseHandler<Collection<ExerciseStub>> handler);
		
		public SearchDataBroker<ExerciseStub> getSearchBroker();
		
		public void remapItemId(String oldId, String newId, boolean newIdInScratchPad);
}
