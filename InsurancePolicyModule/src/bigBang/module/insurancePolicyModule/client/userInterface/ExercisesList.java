package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.ExerciseDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class ExercisesList extends FilterableList<ExerciseStub> {

	protected class Entry extends ListEntry<ExerciseStub> {
		public Entry(ExerciseStub value){
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			ExerciseStub exercise = (ExerciseStub) info;
			setTitle(exercise.label);
		};
	}

	protected InsurancePolicyBroker insurancePolicyBroker;
	protected ExerciseDataBrokerClient exerciseBrokerClient;
	protected ExerciseDataBroker exerciseBroker;

	protected String ownerId;

	public ExercisesList(){
		this.exerciseBrokerClient = getExerciseBrokerClient();
		this.insurancePolicyBroker = (InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		this.exerciseBroker = (ExerciseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.POLICY_EXERCISE);
		this.exerciseBroker.registerClient(this.exerciseBrokerClient);
	}

	public void setOwner(String ownerId){
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else{
			if(!insurancePolicyBroker.isTemp(ownerId)){
				this.exerciseBroker.getProcessExercises(ownerId, new ResponseHandler<Collection<ExerciseStub>>() {

					@Override
					public void onResponse(Collection<ExerciseStub> response) {
						clear();
						for(ExerciseStub e : response){
							addEntry(e);
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}
		}
	}

	protected void addEntry(ExerciseStub exercise){
		this.add(new Entry(exercise));		
	}

	protected ExerciseDataBrokerClient getExerciseBrokerClient(){
		return new ExerciseDataBrokerClient() {

			protected int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				version = number;
			}

			@Override
			public int getDataVersion(String dataElementId) {
				return version;
			}

			@Override
			public void updateExercise(Exercise exercise) {
				for(ValueSelectable<ExerciseStub> vs : ExercisesList.this){
					ExerciseStub e = vs.getValue();
					if(e.id.equalsIgnoreCase(exercise.id)){
						vs.setValue(exercise);
						break;
					}
				}
			}

			@Override
			public void removeExercise(String id) {
				for(ValueSelectable<ExerciseStub> vs : ExercisesList.this) {
					ExerciseStub e = vs.getValue();
					if(e.id.equalsIgnoreCase(id)){
						ExercisesList.this.remove(vs);
						break;
					}
				}
			}

			@Override
			public void addExercise(Exercise exercise) {
				String exerciseOwnerId =insurancePolicyBroker.getFinalMapping(exercise.ownerId);
				String currentOwnerId = insurancePolicyBroker.getFinalMapping(ExercisesList.this.ownerId);

				if(exercise != null && exerciseOwnerId != null && ownerId != null && exerciseOwnerId.equalsIgnoreCase(currentOwnerId)){
					ExercisesList.this.addEntry(exercise);
				}
			}

			@Override
			public void remapItemId(String oldId, String newId) {
				return;
			}
		};
	}

	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}

}
