package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.InsurancePolicy;

public interface InsurancePolicyDataBrokerClient extends
		DataBrokerClient<InsurancePolicy> {
	
	public void addInsurancePolicy(InsurancePolicy policy);
	
	public void updateInsurancePolicy(InsurancePolicy policy);
	
	public void removeInsurancePolicy(String policyId);
	
	public void remapItemId(String oldId, String newId);

}
