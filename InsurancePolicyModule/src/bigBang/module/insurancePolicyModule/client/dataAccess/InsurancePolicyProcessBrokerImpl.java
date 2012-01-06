package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

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
import bigBang.definitions.shared.ExerciseStub;
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
	protected Collection<String> policiesInScratchPad;
	public boolean requiresRefresh;

	public InsurancePolicyProcessBrokerImpl(){
		this(InsurancePolicyService.Util.getInstance());
	}

	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		this.policiesInScratchPad = new ArrayList<String>();
		this.searchBroker = new InsurancePolicySearchDataBroker(this.service);
		insuredObjectsBroker = (InsuredObjectDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT);
		exerciseDataBroker = (ExerciseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.POLICY_EXERCISE);
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
			}
		});
	}

	@Override
	public void getPolicy(final String policyId,
			final ResponseHandler<InsurancePolicy> handler) {
		if(cache.contains(policyId) && !requiresRefresh){
			handler.onResponse((InsurancePolicy) cache.get(policyId));
		}else{
			this.service.getPolicy(policyId, new BigBangAsyncCallback<InsurancePolicy>() {

				@Override
				public void onSuccess(InsurancePolicy result) {
					cache.add(policyId, result);
					incrementDataVersion();
					for(DataBrokerClient<InsurancePolicy> bc : getClients()){
						((InsurancePolicyDataBrokerClient) bc).addInsurancePolicy(result);
						((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
					}
					handler.onResponse(result);
					requiresRefresh = false;
				}
			});
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
			});
		}else{
			throw new RuntimeException("updatePolicy : The policy is not in the scratchpad: " + policy);
		}
	}

	@Override
	public void removePolicy(final String policyId, final ResponseHandler<String> handler) {
		if(inScratchPad(policyId)){
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
		}else{
			throw new RuntimeException("Não é possível remover a apólice");
		}
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
//		service.openPolicyScratchPad(policy.id, new BigBangAsyncCallback<Remap[]>() {
//
//			@Override
//			public void onSuccess(Remap[] result) {
//				//If new policy
//				if(result.length == 1 && result[0].remapIds.length == 1 && result[0].remapIds[0].oldId == null){
//					InsurancePolicy policy = new InsurancePolicy();
//					RemapId remapId = result[0].remapIds[0];
//					policy.id = remapId.newId;
//					if(remapId.newIdIsInPad){
//						addToScratchPad(policy);
//					}
//					handler.onResponse(policy);
//
//				}else{
//					
//					doRemapping(result);
//					getPolicy(policy.id, handler);
//				}
//			}
//		});
//		
//		
//		if(policy.id == null || inScratchPad(policy.id)){
//			if(inScratchPad(policy.id)){
//				policy.id = getScratchPadId(policy.id);
//			}
//			removeFromScratchPad(policy.id);
//			this.service.initializeNewPolicy(policy, new AsyncCallback<InsurancePolicy>() {
//
//				@Override
//				public void onSuccess(InsurancePolicy result) {
//					String tempId = addToScratchPad(result);
//					result.id = tempId;
//					handler.onResponse(result);
//				}
//
//				@Override
//				public void onFailure(Throwable caught) {}
//			});
//		}else{ //if it is an existing policy
//			service.openForEdit(policy, new AsyncCallback<InsurancePolicy>() {
//
//				@Override
//				public void onSuccess(InsurancePolicy result) {
//					addToScratchPad(result);
//					handler.onResponse(result);
//				}
//
//				@Override
//				public void onFailure(Throwable caught) {}
//			});
//		}
	}

	@Override
	public void commitPolicy(InsurancePolicy policy, final ResponseHandler<InsurancePolicy> handler){
//		this.service.commitPolicy(policy.scratchPadId, new BigBangAsyncCallback<InsurancePolicy>() {
//
//			@Override
//			public void onSuccess(InsurancePolicy result) {
//				handler.onResponse(result);
//			}
//		});
	}

	@Override
	public void closePolicyResource(String policyId, final ResponseHandler<Void> handler) {
//		if(inScratchPad(policyId)){
//			service.discardPolicy(getScratchPadId(policyId), new AsyncCallback<InsurancePolicy>() {
//
//				@Override
//				public void onSuccess(InsurancePolicy result) {
//					handler.onResponse(null);
//				}
//
//				@Override
//				public void onFailure(Throwable caught) {
//					// TODO
//				}
//			});
//		}
	}

	@Override
	public void openCoverageDetailsPage(String policyId,
			String insuredObjectId, String exerciseId,
			ResponseHandler<TableSection> handler) {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveCoverageDetailsPage(String policyId,
			String insuredObjectId, String exerciseId, TableSection data,
			final ResponseHandler<TableSection> handler) {
//		if(inScratchPad(policyId)){
//			policyId = this.getScratchPadId(policyId);
//			this.service.savePage(data, new BigBangAsyncCallback<InsurancePolicy.TableSection>() {
//
//				@Override
//				public void onSuccess(TableSection result) {
//					handler.onResponse(result);
//				}
//			});
//		}
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
			});
		}else{
			
		}
	}

	@Override
	public void updateExercise(String policyId, Exercise exercise,
			ResponseHandler<Exercise> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeExercise(String policyId, String exerciseId, final ResponseHandler<Void> handler) {
		if(inScratchPad(policyId)){
			service.deleteExerciseInPad(exerciseId, new BigBangAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					handler.onResponse(null);
				}
			});
		}else{
			
		}
	}

	protected boolean inScratchPad(String policyId) {
		for(String s : this.policiesInScratchPad){
			if(s.equalsIgnoreCase(policyId)){
				return true;
			}
		}
		return false;
	}

	protected void removeFromScratchPad(String policyId) {
		for(String s : this.policiesInScratchPad){
			if(s.equalsIgnoreCase(policyId)){
				policiesInScratchPad.remove(s);
			}
		}
	}

	//Adds a policy to the scratchpad and returns a temporary id
	protected void addToScratchPad(InsurancePolicy policy) {
		for(String s : this.policiesInScratchPad){
			if(s.equalsIgnoreCase(policy.id)){
				return;
			}
		}
		policiesInScratchPad.add(policy.id);
	}

	@Override
	public boolean isTemp(InsurancePolicy policy) {
		return inScratchPad(policy.id);
	}

	@Override
	public void remapItemId(String oldId, String newId, boolean newInScratchPad) {
		InsurancePolicy policy = (InsurancePolicy) this.cache.get(oldId);
		if(policy != null) {
			cache.remove(oldId);
			policy.id = newId;
			cache.add(newId, policy);
		}
		for(String s : this.policiesInScratchPad){
			if(s.equalsIgnoreCase(oldId)){
				policiesInScratchPad.remove(s);
				policiesInScratchPad.add(newId);
				break;
			}
		}
		incrementDataVersion();
		for(DataBrokerClient<InsurancePolicy> bc : getClients()){
			((InsurancePolicyDataBrokerClient) bc).remapItemId(oldId, newId);
			((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
		}
	}

}
