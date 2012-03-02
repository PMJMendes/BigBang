package bigBang.library.interfaces;

import bigBang.definitions.shared.BigBangProcess;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BigBangProcessServiceAsync
	extends Service
{
	void getProcess(String dataObjectId, AsyncCallback<BigBangProcess> callback);
	void getSubProcesses(String dataObjectId, AsyncCallback<BigBangProcess[]> callback);
}
