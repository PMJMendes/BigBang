package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

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
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
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
	public void getPolicy(String policyId,
			final ResponseHandler<InsurancePolicy> handler) {
		policyId = getEffectiveId(policyId);
		if(isTemp(policyId)){
			this.service.getPolicyInPad(policyId, new BigBangAsyncCallback<InsurancePolicy>() {

				@Override
				public void onSuccess(InsurancePolicy result) {
					result.id = getFinalMapping(result.id);
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

	@Override
	public void updatePolicy(InsurancePolicy policy,
			final ResponseHandler<InsurancePolicy> handler) {
		String policyId = getEffectiveId(policy.id);
		if(isTemp(policyId)){
			policy.id = policyId;
			service.updateHeader(policy, new BigBangAsyncCallback<InsurancePolicy>() {

				@Override
				public void onSuccess(InsurancePolicy result) {
					result.id = getFinalMapping(result.id);
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
	public void removePolicy(String id, final ResponseHandler<String> handler) {
		final String policyId = getFinalMapping(id);
		this.service.deletePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				String finalId = getFinalMapping(policyId);
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).removeInsurancePolicy(finalId);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(finalId);
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
	public void createReceipt(String id, Receipt receipt,
			final ResponseHandler<Receipt> handler) {
		String policyId = getFinalMapping(id);
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
	public void openPolicyResource(final String policyId,
			final ResponseHandler<InsurancePolicy> handler) {

		//NEW POLICY
		if(policyId == null){
			service.openPolicyScratchPad(policyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onSuccess(Remap[] result) {
					//If new policy
					if(result.length == 1 && result[0].remapIds.length == 1 && result[0].remapIds[0].oldId == null){
						InsurancePolicy policy = new InsurancePolicy();
						RemapId remapId = result[0].remapIds[0];
						policy.id = remapId.newId;
						policiesInScratchPad.put("new", policy.id);
						handler.onResponse(policy);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not open the new policy resource")
					});
					super.onFailure(caught);
				}

			});
		}else if(!isTemp(policyId)){
			//EXISTING POLICY
			service.openPolicyScratchPad(policyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onSuccess(Remap[] result) {
					doRemapping(result);
					getPolicy(policyId, handler);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not open the policy resource")
					});
					super.onFailure(caught);
				}

			});
		}else{
			handler.onError(new String[]{
					new String("Cannot open a policy resource twice for the same policy")	
			});
		}
	}

	@Override
	public void commitPolicy(final InsurancePolicy policy, final ResponseHandler<InsurancePolicy> handler){
		String policyId = getEffectiveId(policy.id);
		this.service.commitPad(policyId, new BigBangAsyncCallback<Remap[]>() {

			@Override
			public void onSuccess(Remap[] result) {
				String policyId = result[0].remapIds[0].newId;
				doRemapping(result);
				getPolicy(policyId, handler);
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
		policyId = getEffectiveId(policyId);
		if(isTemp(policyId)){
			service.discardPad(policyId, new BigBangAsyncCallback<Remap[]>() {

				@Override
				public void onSuccess(Remap[] result) {
					doRemapping(result);
					handler.onResponse(null);
				}

				@Override
				public void onFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not close the policy resource")	
					});
					super.onFailure(caught);
				}
			});
		}else{
			handler.onError(new String[]{
					new String("Cannot close an unopened policy resource")
			});
		}
	}

	@Override
	public void openCoverageDetailsPage(String policyId,
			String insuredObjectId, String exerciseId,
			final ResponseHandler<TableSection> handler) {
		policyId = getEffectiveId(policyId);
		if(isTemp(policyId)){
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
		policyId = getEffectiveId(policyId);
		if(isTemp(policyId)){
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
					new String("Could not save the page on an unopened policy resource")
			});
		}
	}

	@Override
	public boolean isTemp(String policyId) {
		return this.policiesInScratchPad.containsKey(policyId) || this.policiesInScratchPad.containsValue(policyId);
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

			//OBJECTS
			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					this.insuredObjectsBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}

			//EXERCISES
			}else if(remap.typeId.equalsIgnoreCase(BigBangConstants.EntityIds.POLICY_EXERCISE)){
				for(int j = 0; j < remap.remapIds.length; j++){
					RemapId remapId = remap.remapIds[j];
					this.exerciseDataBroker.remapItemId(remapId.oldId, remapId.newId, remapId.newIdIsInPad);
				}
			}else{
				GWT.log("Could not remap item id for typeId = " + remap.typeId);
			}
		}
	}

	@Override
	public void validatePolicy(String policyId, final ResponseHandler<Void> handler) {
		service.validatePolicy(policyId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				handler.onResponse(null);
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.onError(new String[]{
						caught.getMessage()	
				});
				super.onFailure(caught);
			}
		});
	}

	@Override
	public void remapItemId(String oldId, String newId, boolean newInScratchPad) {
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
	public void discardTemp(String policyId) {
		this.policiesInScratchPad.remove(policyId);
		String finalId = getFinalMapping(policyId);
		this.policiesInScratchPad.remove(finalId);
	}

	public String getEffectiveId(String id){
		if(this.policiesInScratchPad.containsKey(id)){
			return this.policiesInScratchPad.get(id);
		}
		return id;
	}

	public String getFinalMapping(String tempId){
		for(String key : this.policiesInScratchPad.keySet()){
			if(this.policiesInScratchPad.get(key).equalsIgnoreCase(tempId)){
				return key;
			}
		}
		return tempId;
	}

}
