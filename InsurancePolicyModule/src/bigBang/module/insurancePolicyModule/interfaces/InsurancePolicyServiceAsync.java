package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.interfaces.SearchServiceAsync;

public interface InsurancePolicyServiceAsync extends SearchServiceAsync{

	void deletePolicy(String policyId, String processId, AsyncCallback<Void> callback);

	void getPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);

	void editPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);

	void voidPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);
	
}
