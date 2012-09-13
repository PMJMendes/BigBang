package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.SubPolicyExerciseDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.SubPolicyExerciseDataBroker;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class SubPolicyExercisesList extends FilterableList<ExerciseStub> {

	protected class Entry extends ListEntry<ExerciseStub> {
		public Entry(ExerciseStub value){
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			ExerciseStub exercise = (ExerciseStub) info;
			setTitle(exercise.label == null ? "" : exercise.label);
		};
	}

	protected InsuranceSubPolicyBroker insuranceSubPolicyBroker;
	protected SubPolicyExerciseDataBrokerClient exerciseBrokerClient;
	protected SubPolicyExerciseDataBroker exerciseBroker;

	protected String ownerId;

	public SubPolicyExercisesList(){
		this.exerciseBrokerClient = getSubPolicyExerciseBrokerClient();
		this.insuranceSubPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.exerciseBroker = (SubPolicyExerciseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.POLICY_EXERCISE);
		this.exerciseBroker.registerClient(this.exerciseBrokerClient);
		showFilterField(false);
	}

	public void setOwner(String ownerId){
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else{
			this.exerciseBroker.getSubPolicyExercises(ownerId, new ResponseHandler<Collection<ExerciseStub>>() {

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

	protected void addEntry(ExerciseStub exercise){
		this.add(new Entry(exercise));		
	}

	protected SubPolicyExerciseDataBrokerClient getSubPolicyExerciseBrokerClient(){
		return new SubPolicyExerciseDataBrokerClient() {

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
			public void updateExercise(String ownerId, Exercise exercise) {
				if(SubPolicyExercisesList.this.ownerId == null || !SubPolicyExercisesList.this.ownerId.equalsIgnoreCase(ownerId)) { return; }

				for(ValueSelectable<ExerciseStub> vs : SubPolicyExercisesList.this){
					ExerciseStub e = vs.getValue();
					if(e.id.equalsIgnoreCase(exercise.id)){
						vs.setValue(exercise);
						break;
					}
				}
			}

			@Override
			public void removeExercise(String id) {
				for(ValueSelectable<ExerciseStub> vs : SubPolicyExercisesList.this) {
					ExerciseStub e = vs.getValue();
					if(e.id.equalsIgnoreCase(id)){
						SubPolicyExercisesList.this.remove(vs);
						break;
					}
				}
			}

			@Override
			public void addExercise(String ownerId, Exercise exercise) {
				if(SubPolicyExercisesList.this.ownerId == null || !SubPolicyExercisesList.this.ownerId.equalsIgnoreCase(ownerId)) { return; }

				String exerciseOwnerId = /*insuranceSubPolicyBroker.getFinalMapping(*/exercise.ownerId/*)*/;
				String currentOwnerId = /*insuranceSubPolicyBroker.getFinalMapping(*/SubPolicyExercisesList.this.ownerId/*)*/;

				if(exercise != null && exerciseOwnerId != null && ownerId != null && exerciseOwnerId.equalsIgnoreCase(currentOwnerId)){
					SubPolicyExercisesList.this.addEntry(exercise);
				}
			}
		};
	}

	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}

}
