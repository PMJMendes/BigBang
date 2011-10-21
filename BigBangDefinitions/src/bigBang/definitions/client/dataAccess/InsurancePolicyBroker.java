package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;

public interface InsurancePolicyBroker {
	
	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);
	
	public void updatePolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void removePolicy(String policyId, ResponseHandler<String> handler);
	
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker();

}
