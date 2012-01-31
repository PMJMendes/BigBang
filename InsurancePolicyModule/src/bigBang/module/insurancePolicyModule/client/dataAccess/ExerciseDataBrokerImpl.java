package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.ExerciseDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
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
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.shared.ExerciseSearchParameter;
import bigBang.module.insurancePolicyModule.shared.ExerciseSortParameter;

public class ExerciseDataBrokerImpl extends DataBroker<Exercise>
implements ExerciseDataBroker {

	protected PolicyExerciseServiceAsync service;
	protected InsurancePolicyServiceAsync policyService;
	protected InsurancePolicyBroker policyBroker;
	protected SearchDataBroker<ExerciseStub> searchBroker;
	protected Map<String, String> tempExercises;
	protected boolean requiresRefresh;

	public ExerciseDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.POLICY_EXERCISE;
		this.service = PolicyExerciseService.Util.getInstance();
		this.policyService = InsurancePolicyService.Util.getInstance();
		this.searchBroker = new ExerciseSearchBroker(this.service);
		this.tempExercises = new HashMap<String, String>();
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
				return;
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
				return;
			}
		});
	}

	@Override
	public void getExercise(final String id, final ResponseHandler<Exercise> handler) {
		policyService.getExerciseInPad(id, new BigBangAsyncCallback<Exercise>() {

			@Override
			public void onSuccess(Exercise result) {
				handler.onResponse(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				service.getExercise(id, new BigBangAsyncCallback<Exercise>() {

					@Override
					public void onSuccess(Exercise result) {
						handler.onResponse(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						handler.onError(new String[]{
								new String("Could not get the exercise")
						});
						super.onFailure(caught);
					}
				});
			}
		});
	}

	@Override
	public void createExercise(String ownerId, final ResponseHandler<Exercise> handler) {
		if(getPolicyBroker().isTemp(ownerId)){
			this.policyService.createFirstExercise(ownerId, new BigBangAsyncCallback<Exercise>() {

				@Override
				public void onSuccess(Exercise result) {
					handler.onResponse(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create exercise in scratch pad.")
					});
					super.onFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Could not create exercise. The owner policy is not in scratch pad")
			});
		}
	}

	@Override
	public void deleteExercise(String exerciseId, final ResponseHandler<Void> handler) {
		getExercise(exerciseId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(Exercise exercise) {
				if(policyBroker.isTemp(exercise.ownerId)){
					policyService.deleteExerciseInPad(exercise.id, new BigBangAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							handler.onResponse(null);
						}

						@Override
						public void onFailure(Throwable caught) {
							handler.onError(new String[]{
									new String("Could not delete the exercise")
							});
							super.onFailure(caught);
						}
					});
				}else{
					handler.onError(new String[]{
							new String("The owner of the exercise is not in editable mode")
					});
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not find the exercise")	
				});
			}
		});
	}

	@Override
	public void updateExercise(Exercise exercise, final ResponseHandler<Exercise> handler) {
		policyService.updateExerciseInPad(exercise, new BigBangAsyncCallback<Exercise>() {

			@Override
			public void onSuccess(Exercise result) {
				handler.onResponse(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
					new String("Could not update the exercise")	
				});
				super.onFailure(caught);
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
		oldId = oldId.toLowerCase();
		newId = newId.toLowerCase();

		if(newIdInScratchPad){
			this.tempExercises.put(oldId, newId);
		}else{
			this.tempExercises.remove(newId);
		}
	}

	private InsurancePolicyBroker getPolicyBroker(){
		if(this.policyBroker == null){
			this.policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		}
		return this.policyBroker;
	}

	private boolean isTemp(String id){
		id = id.toLowerCase();
		return this.tempExercises.containsKey(id) || this.tempExercises.containsValue(id);
	}
	
	private String getEffectiveId(String id){
		id = id.toLowerCase();
		if(this.tempExercises.containsKey(id)){
			return this.tempExercises.get(id);
		}
		return id;
	}
	
}
