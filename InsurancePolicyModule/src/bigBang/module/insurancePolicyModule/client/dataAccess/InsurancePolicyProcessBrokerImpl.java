package bigBang.module.insurancePolicyModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;

public class InsurancePolicyProcessBrokerImpl extends DataBroker<InsurancePolicy> implements InsurancePolicyBroker {
	
	protected InsurancePolicyServiceAsync service;
	protected SearchDataBroker<InsurancePolicyStub> searchBroker;
	public boolean requiresRefresh;
	
	public InsurancePolicyProcessBrokerImpl(){
		this(InsurancePolicyService.Util.getInstance());
	}
	
	public InsurancePolicyProcessBrokerImpl(InsurancePolicyServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.INSURANCE_POLICY;
		this.searchBroker = new InsurancePolicySearchDataBroker(this.service);
	}
	
	@Override
	public void requireDataRefresh() {
		this.requiresRefresh = true;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub
		
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
		this.service.editPolicy(policy, new BigBangAsyncCallback<InsurancePolicy>() {

			@Override
			public void onSuccess(InsurancePolicy result) {
				cache.add(result.id, result);
				incrementDataVersion();
				for(DataBrokerClient<InsurancePolicy> bc : getClients()){
					((InsurancePolicyDataBrokerClient) bc).updateInsurancePolicy(result);
					((InsurancePolicyDataBrokerClient) bc).setDataVersionNumber(BigBangConstants.EntityIds.INSURANCE_POLICY, getCurrentDataVersion());
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removePolicy(final String policyId, final ResponseHandler<String> handler) {
		this.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(final InsurancePolicy response) {
				InsurancePolicyProcessBrokerImpl.this.service.deletePolicy(policyId, response.processId, new BigBangAsyncCallback<Void>() {

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
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker() {
		return this.searchBroker;
	}

	
}
