package bigBang.library.interfaces;

import bigBang.library.shared.BigBangProcess;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BigBangProcessServiceAsync extends Service {

	void getProcesses(String processTypeId,
			AsyncCallback<BigBangProcess[]> callback);

}
