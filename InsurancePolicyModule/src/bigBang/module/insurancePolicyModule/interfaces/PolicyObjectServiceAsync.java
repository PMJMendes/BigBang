package bigBang.module.insurancePolicyModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InsuredObject;
import bigBang.library.interfaces.SearchServiceAsync;

public interface PolicyObjectServiceAsync
	extends SearchServiceAsync
{
	void getObject(String objectId, AsyncCallback<InsuredObject> callback);
}
