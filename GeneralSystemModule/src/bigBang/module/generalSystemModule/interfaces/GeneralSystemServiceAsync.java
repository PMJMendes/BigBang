package bigBang.module.generalSystemModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.library.interfaces.Service;

public interface GeneralSystemServiceAsync 
	extends Service
{
	void getGeneralSystemProcessId(AsyncCallback<String> callback);
	void getGeneralSystemId(AsyncCallback<String> callback);
}
