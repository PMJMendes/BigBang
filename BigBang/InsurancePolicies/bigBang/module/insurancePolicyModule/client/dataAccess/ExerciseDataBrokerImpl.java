package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.ExerciseDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.dataAccess.SubPolicyExerciseDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.SubPolicyExerciseDataBroker;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceOLD;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceOLDAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.shared.ExerciseSearchParameter;
import bigBang.module.insurancePolicyModule.shared.ExerciseSortParameter;

public class ExerciseDataBrokerImpl extends DataBroker<Exercise>
implements ExerciseDataBroker, SubPolicyExerciseDataBroker {

	protected PolicyExerciseServiceAsync service;
	protected SubPolicyExerciseServiceAsync subPolicyExerciseService;
	protected InsurancePolicyServiceOLDAsync policyService;
	protected InsurancePolicyBroker policyBroker;
	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected SearchDataBroker<ExerciseStub> searchBroker;
	protected SearchDataBroker<ExerciseStub> subPolicyExerciseSearchBroker;
	protected Map<String, String> exercisesInScratchPad;
	protected boolean requiresRefresh;

	public ExerciseDataBrokerImpl(){
		this.dataElementId = BigBangConstants.EntityIds.POLICY_EXERCISE;
		this.subPolicyExerciseService = SubPolicyExerciseService.Util.getInstance();
		this.service = PolicyExerciseService.Util.getInstance();
		this.policyService = InsurancePolicyServiceOLD.Util.getInstance();
		this.searchBroker = new ExerciseSearchBroker(this.service);
		this.subPolicyExerciseSearchBroker = new SubPolicyExerciseSearchBroker(this.subPolicyExerciseService);
		this.exercisesInScratchPad = new HashMap<String, String>();
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
	public void getExercise(String id, final ResponseHandler<Exercise> handler) {
		id = getEffectiveId(id);
		if(isTemp(id)){
			policyService.getExerciseInPad(id, new BigBangAsyncCallback<Exercise>() {

				@Override
				public void onResponseSuccess(Exercise result) {
					result.id = getFinalMapping(result.id);
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested exercise")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			this.service.getExercise(id, new BigBangAsyncCallback<Exercise>() {

				@Override
				public void onResponseSuccess(Exercise result) {
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested exercise")	
					});
					super.onResponseFailure(caught);
				}

			});
		}
	}

	@Override
	public void createExercise(String ownerId, final ResponseHandler<Exercise> handler) {
		if(getInsurancePolicyBroker().isTemp(ownerId)){
			ownerId = getInsurancePolicyBroker().getEffectiveId(ownerId);
			policyService.createFirstExercise(ownerId, new BigBangAsyncCallback<Exercise>() {

				@Override
				public void onResponseSuccess(Exercise result) {
					exercisesInScratchPad.put(result.id, result.id);
					incrementDataVersion();
					for(DataBrokerClient<Exercise> client : clients) {
						((ExerciseDataBrokerClient)client).addExercise(result);
						((ExerciseDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create new exercise in scratchpad")	
					});
					super.onResponseFailure(caught);
				}

			});
		}else{
			handler.onError(new String[]{
					new String("Cannot create exercise in policy not in editable mode")	
			});
		}
	}

	@Override
	public void deleteExercise(String exerciseId, final ResponseHandler<Void> handler) {
		exerciseId = getEffectiveId(exerciseId);
		getExercise(exerciseId, new ResponseHandler<Exercise>() {

			@Override
			public void onResponse(final Exercise response) {
				String ownerId = response.ownerId;
				if(getInsurancePolicyBroker().isTemp(ownerId)){
					String tempId = getEffectiveId(response.id);
					policyService.deleteExerciseInPad(tempId, new BigBangAsyncCallback<Void>() {

						@Override
						public void onResponseSuccess(Void result) {
							incrementDataVersion();
							for(DataBrokerClient<Exercise> client : clients) {
								((ExerciseDataBrokerClient)client).removeExercise(response.id);
								((ExerciseDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
							}
							handler.onResponse(null);
						}

						@Override
						public void onResponseFailure(Throwable caught) {
							handler.onError(new String[]{
									new String("Could not delete the exercise")	
							});
							super.onResponseFailure(caught);
						}
					});				
				}else{
					handler.onError(new String[]{
							new String("Cannot delete the exercise")	
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
		String id = getEffectiveId(exercise.id);
		if(isTemp(id)){
			String tempId = exercise.id;
			exercise.id = id;

			policyService.updateExerciseInPad(exercise, new BigBangAsyncCallback<Exercise>() {

				@Override
				public void onResponseSuccess(Exercise result) {
					result.id = getFinalMapping(result.id);
					incrementDataVersion();
					for(DataBrokerClient<Exercise> client : clients) {
						if(client instanceof SubPolicyExerciseDataBrokerClient){
							//DO NOTHING
						}
						else{
							((ExerciseDataBrokerClient)client).updateExercise(result);
							((ExerciseDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
						}
					}
					handler.onResponse(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String()	
					});
					super.onResponseFailure(caught);
				}
			});
			exercise.id = tempId;
		}else{
			handler.onError(new String[]{
					new String("Cannot update exercise in policy not in editable mode")	
			});
		}
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
			public void onError(Collection<ResponseError> errors) {
				handler.onError(new String[]{
						new String("Could not get the resquested process exercises")	
				});
			}
		});
	}

	@Override
	public void getSubPolicyExercise(String exerciseId, final String subPolicyId,
			final ResponseHandler<Exercise> handler) {
		this.subPolicyExerciseService.getExercise(exerciseId, subPolicyId, new BigBangAsyncCallback<Exercise>() {

			@Override
			public void onResponseSuccess(Exercise result) {
				incrementDataVersion();
				for(DataBrokerClient<Exercise> client : clients) {
					if(client instanceof SubPolicyExerciseDataBrokerClient){
						((SubPolicyExerciseDataBrokerClient)client).updateExercise(subPolicyId, result);
						((SubPolicyExerciseDataBrokerClient)client).setDataVersionNumber(getDataElementId(), getCurrentDataVersion());
					}
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not get the sub policy exercise")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getSubPolicyExercises(String subPolicyId,
			final ResponseHandler<Collection<ExerciseStub>> handler) {
		ExerciseSearchParameter parameter= new ExerciseSearchParameter();

		if(getSubPolicyBroker().isTemp(subPolicyId)){
			SubPolicyTypifiedListBroker subPolicyListBroker = SubPolicyTypifiedListBroker.Util.getInstance();
			subPolicyListBroker.getListItems(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES+"/"+subPolicyBroker.getEffectiveId(subPolicyId), new ResponseHandler<Collection<TipifiedListItem>>() {

				@Override
				public void onResponse(Collection<TipifiedListItem> response) {
					ArrayList<ExerciseStub> stubs = new ArrayList<ExerciseStub>();
					for(TipifiedListItem item : response){
						ExerciseStub temp = new ExerciseStub();
						temp.label = item.value;
						temp.id = item.id;
						stubs.add(temp);
					}

					handler.onResponse(stubs);

				}

				@Override
				public void onError(Collection<ResponseError> errors) {	
					handler.onError(new String[]{
							new String("Could not get the subpolicy exercises on creation")	
					});
				}
			});
		}else{		
			parameter.policyId = subPolicyId;
			SearchParameter[] parameters = new SearchParameter[]{
					parameter
			};

			SortParameter sort = new ExerciseSortParameter(ExerciseSortParameter.SortableField.STARTDATE, SortOrder.DESC);

			SortParameter[] sorts = new SortParameter[]{
					sort
			};

			this.getSubPolicyExerciseSearchBroker().search(parameters, sorts, -1, new ResponseHandler<Search<ExerciseStub>>() {

				@Override
				public void onResponse(Search<ExerciseStub> response) {
					handler.onResponse(response.getResults());
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					handler.onError(new String[]{
							new String("Could not get the resquested process exercises")	
					});
				}
			}, true);
		}
	}

	private String getEffectiveId(String id){
		id = id.toLowerCase();
		if(exercisesInScratchPad.containsKey(id)){
			return exercisesInScratchPad.get(id);
		}
		return id;
	}

	private String getFinalMapping(String tempId){
		for(String key : exercisesInScratchPad.keySet()){
			if(exercisesInScratchPad.get(key).equalsIgnoreCase(tempId)){
				return key;
			}
		}
		return tempId;
	}

	@Override
	public SearchDataBroker<ExerciseStub> getSearchBroker() {
		return this.searchBroker;
	}

	public SearchDataBroker<ExerciseStub> getSubPolicyExerciseSearchBroker() {
		return this.subPolicyExerciseSearchBroker;
	}

	@Override
	public void remapItemId(String oldId, String newId,
			boolean newIdInScratchPad) {
		oldId = oldId.toLowerCase();
		newId = newId == null ? null : newId.toLowerCase();

		if(newIdInScratchPad){
			exercisesInScratchPad.put(oldId, newId);
		}else if(newId == null){
			this.exercisesInScratchPad.remove(oldId);
		}else{
			exercisesInScratchPad.remove(newId);
		}
	}

	private InsurancePolicyBroker getInsurancePolicyBroker(){
		if(this.policyBroker == null){
			this.policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		}
		return this.policyBroker;
	}

	private InsuranceSubPolicyBroker getSubPolicyBroker(){
		if(this.subPolicyBroker == null){
			this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		}
		return this.subPolicyBroker;
	}

	private boolean isTemp(String id){
		id = id.toLowerCase();
		return exercisesInScratchPad.containsKey(id) || exercisesInScratchPad.containsValue(id); 
	}

}
