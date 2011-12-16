package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Receipt;
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
	protected Map<String, String> policyScratchPadIds;
	public boolean requiresRefresh;

	public InsurancePolicyProcessBrokerImpl(){
		this(InsurancePolicyService.Util.getInstance());
	}

	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		this.policyScratchPadIds = new HashMap<String, String>();
		this.searchBroker = new InsurancePolicySearchDataBroker(this.service);
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
			public void onError(Collection<ResponseError> errors) {
			}
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
		if(policy.id == null || inScratchPad(policy.id)){
			if(inScratchPad(policy.id)){
				policy.id = getScratchPadId(policy.id);
			}
			removeFromScratchPad(policy.id);
			this.service.updateHeader(policy, new AsyncCallback<InsurancePolicy>() {
				
				@Override
				public void onSuccess(InsurancePolicy result) {
					String tempId = addToScratchPad(result);
					result.id = tempId;
					handler.onResponse(result);
				}

				@Override
				public void onFailure(Throwable caught) {}
			});
		}
	}

	@Override
	public void removePolicy(final String policyId, final ResponseHandler<String> handler) {
		InsurancePolicyProcessBrokerImpl.this.service.deletePolicy(policyId, new BigBangAsyncCallback<Void>() {

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
	public void openPolicyResource(InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		//If it is a new policy
		if(policy.id == null || inScratchPad(policy.id)){
			if(inScratchPad(policy.id)){
				policy.id = getScratchPadId(policy.id);
			}
			removeFromScratchPad(policy.id);
			this.service.initializeNewPolicy(policy, new AsyncCallback<InsurancePolicy>() {
				
				@Override
				public void onSuccess(InsurancePolicy result) {
					String tempId = addToScratchPad(result);
					result.id = tempId;
					handler.onResponse(result);
				}

				@Override
				public void onFailure(Throwable caught) {}
			});
		}else{ //if it is an existing policy
			service.openForEdit(policy, new AsyncCallback<InsurancePolicy>() {
				
				@Override
				public void onSuccess(InsurancePolicy result) {
					addToScratchPad(result);
					handler.onResponse(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {}
			});
		}
	}

	@Override
	public void commitPolicy(InsurancePolicy policy, final ResponseHandler<InsurancePolicy> handler){
		this.service.commitPolicy(policy.scratchPadId, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onSuccess(InsurancePolicy result) {
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void closePolicyResource(String policyId, final ResponseHandler<Void> handler) {
		if(inScratchPad(policyId)){
			service.discardPolicy(getScratchPadId(policyId), new AsyncCallback<InsurancePolicy>() {
				
				@Override
				public void onSuccess(InsurancePolicy result) {
					handler.onResponse(null);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO
				}
			});
		}
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
		if(inScratchPad(policyId)){
			policyId = this.getScratchPadId(policyId);
			this.service.savePage(data, new BigBangAsyncCallback<InsurancePolicy.TableSection>() {

				@Override
				public void onSuccess(TableSection result) {
					handler.onResponse(result);
				}
			});
		}
	}
	
	@Override
	public void createInsuredObject(String policyId, InsuredObject object,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInsuredObject(String policyId, InsuredObject object,
			ResponseHandler<InsuredObject> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeInsuredObject(String policyId, InsuredObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createExercise(String policyId, ExerciseStub exercise,
			ResponseHandler<ExerciseStub> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExercise(String policyId, ExerciseStub exercise,
			ResponseHandler<ExerciseStub> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeExercise(String policyId, String exerciseId) {
		// TODO Auto-generated method stub
		
	}
	
	protected boolean inScratchPad(String policyId) {
		return this.policyScratchPadIds.containsKey(policyId);
	}
	
	protected String getScratchPadId(String policyId){
		if(inScratchPad(policyId)){
			return policyScratchPadIds.get(policyId);
		}
		throw new RuntimeException("The policy is not currently in the scratchpad");
	}
	
	protected void removeFromScratchPad(String policyId) {
		this.policyScratchPadIds.remove(policyId);
	}
	
	//Adds a policy to the scratchpad and returns a temporary id
	protected String addToScratchPad(InsurancePolicy policy) {
		String tempId = policy.id == null ? policy.scratchPadId : policy.id;
		policyScratchPadIds.put(tempId, policy.id);
		return tempId;
	}

}
