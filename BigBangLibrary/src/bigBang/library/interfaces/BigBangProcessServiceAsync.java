package bigBang.library.interfaces;

import bigBang.library.shared.BigBangProcess;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BigBangProcessServiceAsync
	extends Service
{
	void getSubProcesses(String parentProcessId, AsyncCallback<BigBangProcess[]> callback);
}
