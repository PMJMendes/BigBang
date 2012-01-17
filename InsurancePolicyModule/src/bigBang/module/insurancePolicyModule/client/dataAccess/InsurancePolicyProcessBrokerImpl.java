package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.ExerciseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.Remap.RemapId;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;

public class InsurancePolicyProcessBrokerImpl extends DataBroker<InsurancePolicy> implements InsurancePolicyBroker {

	protected InsurancePolicyServiceAsync service;
	protected SearchDataBroker<InsurancePolicyStub> searchBroker;
	protected InsuredObjectDataBroker insuredObjectsBroker;
	protected ExerciseDataBroker exerciseDataBroker;
	protected Map<String, String> policiesInScratchPad;
	public boolean requiresRefresh;

	public InsurancePolicyProcessBrokerImpl(){
		this(InsurancePolicyService.Util.getInstance());
	}

	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service){
		this(InsurancePolicyService.Util.getInstance(), ((InsuredObjectDataBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)), ((ExerciseDataBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_EXERCISE)));
	}

	public InsurancePolicyProcessBrokerImpl(InsuredObjectDataBroker insuredObjectDataBroker, ExerciseDataBroker exerciseDataBroker){
		this(InsurancePolicyService.Util.getInstance(), insuredObjectDataBroker, exerciseDataBroker);
	}

	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service, InsuredObjectDataBroker insuredObjectDataBroker, ExerciseDataBroker exerciseDataBroker) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		this.policiesInScratchPad = new HashMap<String, String>();
		this.searchBroker = new InsurancePolicySearchDataBroker(this.service);
		this.insuredObjectsBroker = insuredObjectDataBroker;
		this.exerciseDataBroker = exerciseDataBroker;
	}

	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		this.getPolicy(itemId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).addInsurancePolicy(response);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		cache.remove(itemId);
		incrementDataVersion();
		for(DataBrokerClient<InsurancePolicy> bc : getClients()){
			((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(itemId);
			((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
		}
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		this.getPolicy(itemId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				cache.add(response.id, response);
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(response);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	@Override
	public void getPolicy(final String policyId,
			final ResponseHandler<InsurancePolicy> handler) {
		if(cache.contains(policyId) && !requiresRefresh){
			handler.onResponse((InsurancePolicy) cache.get(policyId));
		}else{
			if(inScratchPad(policyId)){
				this.service.getPolicyInPad(policyId, new BigBangAsyncCallback<InsurancePolicy>() {

					@Override
					public void onSuccess(InsurancePolicy result) {
						incrementDataVersion();
						for(DataBrokerClient<InsurancePolicy> bc : getClients()){
							((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
							((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
						}
						handler.onResponse(result);
						requiresRefresh = false;
					}
				});
			}else{
				this.service.getPolicy(policyId, new BigBangAsyncCallback<InsurancePolicy>() {

					@Override
					public void onSuccess(InsurancePolicy result) {
						cache.add(policyId, result);
						incrementDataVersion();
						for(DataBrokerClient<InsurancePolicy> bc : getClients()){
							((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
							((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
						}
						handler.onResponse(result);
						requiresRefresh = false;
					}

					@Override
					public void onFailure(Throwable caught) {
						handler.onError(new String[]{
								new String("Could not get the requested policy")
						});
						super.onFailure(caught);
					}
				});
			}
		}
	}

	@Override
	public void updatePolicy(final InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {

		if(inScratchPad(policy.id)){
			service.updateHeader(policy, new BigBangAsyncCallback<InsurancePolicy>() {

				@Override
				public void onSuccess(InsurancePolicy result) {
					cache.add(result.id, result);
					incrementDataVersion();
					for(DataBrokerClient<InsurancePolicy> bc : getClients()){
						((InsurancePolicyDataBrokerClient) bc).addInsurancePolicy(result);
						((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not save policy header")	
					});
					super.onFailure(caught);
				}

			});
		}else{
			handler.onError(new String[]{
					new String("Could not save the policy header. The policy is not in scratch pad")
			});
		}
	}

	@Override
	public void removePolicy(final String policyId, final ResponseHandler<String> handler) {
		this.service.deletePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				cache.remove(policyId);
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(policyId);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(policyId);
			}
		});
	}

	@Override
	public void getClientPolicies(String clientid,
			final ResponseHandler<Collection<InsurancePolicyStub>> policies) {
		InsurancePolicySearchParameter parameter = new InsurancePolicySearchParameter();
		parameter.ownerId = clientid;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SortParameter sort = new InsurancePolicySortParameter(InsurancePolicySortParameter.SortableField.NUMBER, SortOrder.DESC);		
		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		this.searchBroker.search(parameters, sorts, -1, new ResponseHandler<Search<InsurancePolicyStub>>() {

			@Override
			public void onResponse(Search<InsurancePolicyStub> response) {
				policies.onResponse(response.getResults());
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				policies.onError(new String[]{
						new String("Could not get the client policies")	
				});
			}
		});
	}

	@Override
	public void createReceipt(String policyId, Receipt receipt,
			final ResponseHandler<Receipt> handler) {
		this.service.createReceipt(policyId, receipt, new BigBangAsyncCallback<Receipt>() {

			@Override
			public void onSuccess(Receipt result) {
				handler.onResponse(result);
				DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT).notifyItemCreation(result.id);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not create receipt")	
				});
				super.onFailure(caught);
			}

		});
	}

	@Override
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker() {
		return this.searchBroker;
	}

	@Override
	public void openPolicyResource(final InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		//If it is a new policy
		service.openPolicyScratchPad(policy.id, new BigBangAsyncCallback<Remap[]>() {

			@Override
			public void onSuccess(Remap[] result) {
				//If new policy
				if(result.length == 1 && result[0].remapIds.length == 1 && result[0].remapIds[0].oldId == null){
					InsurancePolicy policy = new InsurancePolicy();
					RemapId remapId = result[0].remapIds[0];
					policy.id = remapId.newId;
					if(remapId.newIdIsInPad){
						policiesInScratchPad.put(policy.id, null);
					}
					handler.onResponse(policy);
				}else{
					doRemapping(result);
					policy.id = getTempMapping(policy.id);
					getPolicy(policy.id, handler);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not open the policy resource")
				});
				super.onFailure(caught);
			}

		});
	}

	@Override
	public void commitPolicy(final InsurancePolicy policy, final ResponseHandler<InsurancePolicy> handler){
		this.service.commitPad(policy.id, new BigBangAsyncCallback<Remap[]>() {

			@Override
			public void onSuccess(Remap[] result) {
				doRemapping(result);
				policy.id = getTempMapping(policy.id);
				handler.onResponse(policy);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not commit the scratch pad")	
				});
				super.onFailure(caught);
			}

		});
	}

	@Override
	public void closePolicyResource(String policyId, final ResponseHandler<Void> handler) {
		if(inScratchPad(policyId)){
			policyId = getTempMapping(policyId);
			service.discardPad(policyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onSuccess(Remap[] result) {
					doRemapping(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
						new String("Could not close the policy resource")	
					});
					super.onFailure(caught);
				}
			});
		}
	}

	@Override
	public void openCoverageDetailsPage(String policyId,
			String insuredObjectId, String exerciseId,
			final ResponseHandler<TableSection> handler) {
		if(inScratchPad(policyId)){
			service.getPageForEdit(policyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<InsurancePolicy.TableSection>() {

				@Override
				public void onSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested page for edit")
					});
					super.onFailure(caught);
				}
			});
		}else{
			service.getPage(policyId, insuredObjectId, exerciseId, new BigBangAsyncCallback<InsurancePolicy.TableSection>() {

				@Override
				public void onSuccess(TableSection result) {
					handler.onResponse(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get the requested page")
					});
					super.onFailure(caught);
				}
			});
		}
	}

	@Override
	public void saveCoverageDetailsPage(String policyId,
			String insuredObjectId, String exerciseId, TableSection data,
			final ResponseHandler<TableSection> handler) {
		if(inScratchPad(policyId)){
			this.service.savePage(data, new BigBangAsyncCallback<InsurancePolicy.TableSection>() {

				@Override
				public void onSuccess(TableSection result) {
					handler.onResponse(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
						new String("Coulg not save the page")	
					});
					super.onFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Could not save the page")
			});
		}
	}

	@Override
	public void createInsuredObject(String policyId, InsuredObject object,
			final ResponseHandler<InsuredObject> handler) {
		//		if(inScratchPad(policyId)){
		//			policyId = this.getScratchPadId(policyId);
		//			this.service.createObjectInPad(policyId, new BigBangAsyncCallback<InsuredObject>() {
		//
		//				@Override
		//				public void onSuccess(InsuredObject result) {
		//					handler.onResponse(result);
		//				}
		//			});
		//		}
	}

	@Override
	public void updateInsuredObject(String policyId, InsuredObject object,
			final ResponseHandler<InsuredObject> handler) {
		//		if(inScratchPad(policyId)){
		//			policyId = this.getScratchPadId(policyId);
		//			this.service.updateObjectInPad(object, new BigBangAsyncCallback<InsuredObject>() {
		//
		//				@Override
		//				public void onSuccess(InsuredObject result) {
		//					handler.onResponse(result);
		//				}
		//			});
		//		}
	}

	@Override
	public void removeInsuredObject(String policyId, InsuredObject object, final ResponseHandler<Void> handler) {
		service.deleteObjectInPad(object.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				handler.onResponse(null);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						new String("Could not remove insured object from scratch pad")	
				});
				super.onFailure(caught);
			}

		});
	}

	@Override
	public void createExercise(String policyId, Exercise exercise,
			final ResponseHandler<Exercise> handler) {
		if(inScratchPad(policyId)){
			service.openNewExercise(policyId, exercise, new BigBangAsyncCallback<Exercise>() {

				@Override
				public void onSuccess(Exercise result) {
					handler.onResponse(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not create exercise in scratch pad")	
					});
					super.onFailure(caught);
				}
			});
		}
	}

	@Override
	public void updateExercise(String policyId, Exercise exercise,
			ResponseHandler<Exercise> handler) {
		//service.
	}

	@Override
	public void removeExercise(String policyId, String exerciseId, final ResponseHandler<Void> handler) {
		if(inScratchPad(policyId)){
			service.deleteExerciseInPad(exerciseId, new BigBangAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					handler.onResponse(null);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not remove exercise from scratch pad")	
					});
					super.onFailure(caught);
				}
			});
		}else{
			handler.onError(
					new String[]{
							new String("Could not remove exercise, since the owner in not in scratch pad")
					});
		}
	}

	protected boolean inScratchPad(String policyId) {
		return this.policiesInScratchPad.containsKey(policyId) || this.policiesInScratchPad.containsValue(policyId);
	}

	@Override
	public boolean isTemp(InsurancePolicy policy) {
		return inScratchPad(policy.id);
	}

	protected void doRemapping(Remap[] remappings){
		for(int i = 0; i < remappings.length; i++) {
			Remap remap = remappings[i];

			//POLICIES
			if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}
			}
			//INSURED OBJECTS
			//			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)){
			//				for(int j = 0; j < remap.remapIds.length; j++){
			//					RemapId remapId = remap.remapIds[j];
			//					this.insuredObjectsBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
			//				}
			//			//EXERCISES
			//			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_EXERCISE)){
			//				for(int j = 0; j < remap.remapIds.length; j++){
			//					RemapId remapId = remap.remapIds[j];
			//					this.exerciseDataBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
			//				}
			//			}else{
			//				GWT.log("Could not remap item id for typeId = " + remap.typeId);
			//			}
		}
	}

	@Override
	public void remapItemId(String oldId, String newId, boolean newInScratchPad) {
		cache.remove(oldId);
		if(newInScratchPad){
			this.policiesInScratchPad.put(oldId, newId);
		}else{
			this.policiesInScratchPad.remove(newId);
		}		
		incrementDataVersion();
		for(DataBrokerClient<InsurancePolicy> bc : getClients()){
			((InsurancePolicyDataBrokerClient) bc).remapItemId(oldId, newId);
			((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
		}
	}

	@Override
	public String getTempMapping(String id) {
		if(this.policiesInScratchPad.containsKey(id)){
			return this.policiesInScratchPad.get(id);
		}
		return id;
	}

}
