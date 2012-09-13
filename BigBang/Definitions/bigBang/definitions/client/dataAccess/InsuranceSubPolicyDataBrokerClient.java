package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.SubPolicy;

public interface InsuranceSubPolicyDataBrokerClient extends
DataBrokerClient<SubPolicy> {

	public void addInsuranceSubPolicy(SubPolicy subPolicy);

	public void updateInsuranceSubPolicy(SubPolicy subPolicy);

	public void removeInsuranceSubPolicy(String subPolicyId);

	public void remapItemId(String Id, String newId);

}
