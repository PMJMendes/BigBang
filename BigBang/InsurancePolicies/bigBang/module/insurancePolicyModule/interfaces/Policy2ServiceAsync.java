package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Object2;
import bigBang.definitions.shared.InsurancePolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Policy2ServiceAsync
	extends InsurancePolicyServiceAsync
{
	void getEmptyPolicy(String subLineId, AsyncCallback<InsurancePolicy> callback);
	void getEmptyObject(String subLineId, AsyncCallback<Object2> callback);
	void getPolicy2(String policyId, AsyncCallback<InsurancePolicy> callback);
	void getPolicyObject(String objectId, AsyncCallback<Object2> callback);
	void editPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
}
