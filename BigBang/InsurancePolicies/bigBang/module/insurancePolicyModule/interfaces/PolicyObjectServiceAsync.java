package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.InsuredObject;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PolicyObjectServiceAsync
	extends SearchServiceAsync
{
	void getObject(String objectId, AsyncCallback<InsuredObject> callback);
}
