package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Object2;
import bigBang.definitions.shared.Policy2;
import bigBang.library.interfaces.Service;

public interface Policy2ServiceAsync
	extends Service
{
	void getEmptyPolicy(String subLineId, AsyncCallback<Policy2> callback);
	void getEmptyObject(String subLineId, AsyncCallback<Object2> callback);
	void getPolicy(String policyId, AsyncCallback<Policy2> callback);
	void getPolicyObject(String objectId, AsyncCallback<Object2> callback);
	void editPolicy(Policy2 policy, AsyncCallback<Policy2> callback);
}
