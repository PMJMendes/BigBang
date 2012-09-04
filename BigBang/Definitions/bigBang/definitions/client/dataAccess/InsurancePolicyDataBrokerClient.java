package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Policy2;

public interface InsurancePolicyDataBrokerClient extends
		DataBrokerClient<Policy2> {
	
	public void addInsurancePolicy(Policy2 policy);
	
	public void updateInsurancePolicy(Policy2 policy);
	
	public void removeInsurancePolicy(String policyId);
	
	public void remapItemId(String oldId, String newId);

}
