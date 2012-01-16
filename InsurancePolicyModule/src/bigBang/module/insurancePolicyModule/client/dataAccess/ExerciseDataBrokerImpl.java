package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.ExerciseDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.shared.ExerciseSearchParameter;
import bigBang.module.insurancePolicyModule.shared.ExerciseSortParameter;

public class ExerciseDataBrokerImpl extends DataBroker<Exercise>
		implements ExerciseDataBroker {

	protected PolicyExerciseServiceAsync service;
	protected SearchDataBroker<ExerciseStub> searchBroker;
	protected Collection<String> exercisesInScratchPanel;
	protected boolean requiresRefresh;
	
	public ExerciseDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.POLICY_EXERCISE;
		this.service = PolicyExerciseService.Util.getInstance();
		this.searchBroker = new ExerciseSearchBroker(this.service);
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getExercise(itemId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(Exercise response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Exercise> bc : getClients()){
					((ExerciseDataBrokerClient) bc).addExercise(response);
					((ExerciseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_EXERCISE, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<Exercise> bc : getClients()){
			((ExerciseDataBrokerClient) bc).removeExercise(itemId);
			((ExerciseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_EXERCISE, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getExercise(itemId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(Exercise response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<Exercise> bc : getClients()){
					((ExerciseDataBrokerClient) bc).updateExercise(response);
					((ExerciseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_EXERCISE, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}

	@Override
	public void getExercise(String id, final ResponseHandler<Exercise> handler) {
		this.service.getExercise(id, new BigBangAsyncCallback<Exercise>() {

			@Override
			public void onSuccess(Exercise result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void getProcessExercises(String ownerId,
			final ResponseHandler<Collection<ExerciseStub>> handler) {
		ExerciseSearchParameter parameter= new ExerciseSearchParameter();
		parameter.policyId = ownerId;
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		
		SortParameter sort = new ExerciseSortParameter(ExerciseSortParameter.SortableField.STARTDATE, SortOrder.DESC);
		
		SortParameter[] sorts = new SortParameter[]{
				sort
		};
		
		this.getSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<ExerciseStub>>() {

			@Override
			public void onResponse(Search<ExerciseStub> response) {
				handler.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public SearchDataBroker<ExerciseStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void remapItemId(String oldId, String newId,
			boolean newIdInScratchPad) {
		Exercise exercise = (Exercise) this.cache.get(oldId);
		if(exercise != null) {
			cache.remove(oldId);
			exercise.id = newId;
			cache.add(newId, exercise);
		}
//		for(String s : this.exercisesInScratchPad){
//			if(s.equalsIgnoreCase(oldId)){
//				exercisesInScratchPad.remove(s);
//				exercisesInScratchPad.add(newId);
//				break;
//			}
//		}
		incrementDataVersion();
		for(DataBrokerClient<Exercise> bc : getClients()){
			((ExerciseDataBrokerClient) bc).remapItemId(oldId, newId);
			((ExerciseDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.POLICY_EXERCISE, getCurrentDataVersion());
		}
	}
	
}
