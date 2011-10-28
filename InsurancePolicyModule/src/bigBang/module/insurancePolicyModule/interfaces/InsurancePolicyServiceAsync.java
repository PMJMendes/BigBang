package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
import bigBang.library.interfaces.SearchServiceAsync;

public interface InsurancePolicyServiceAsync extends SearchServiceAsync{

	void deletePolicy(String policyId, AsyncCallback<Void> callback);

	void getPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);

	void editPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);

	void voidPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);

	void createReceipt(String policyId, Receipt receipt,
			AsyncCallback<Receipt> callback);
	
}
