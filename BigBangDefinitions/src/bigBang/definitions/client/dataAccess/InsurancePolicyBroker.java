package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.Receipt;

public interface InsurancePolicyBroker extends DataBrokerInterface<InsurancePolicy> {
	
	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);
	
	public void updatePolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void removePolicy(String policyId, ResponseHandler<String> handler);
	
	public void getClientPolicies(String clientid, ResponseHandler<Collection<InsurancePolicyStub>> policies);
	
	public void createReceipt(String policyId, Receipt receipt, ResponseHandler<Receipt> handler);
	
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker();

}
