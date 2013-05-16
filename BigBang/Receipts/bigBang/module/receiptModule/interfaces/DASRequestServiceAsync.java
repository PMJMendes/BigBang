package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.DASRequest;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DASRequestServiceAsync
	extends Service
{
	void getRequest(String id, AsyncCallback<DASRequest> callback);
	void repeatRequest(DASRequest request, AsyncCallback<DASRequest> callback);
	void receiveResponse(DASRequest.Response response, AsyncCallback<DASRequest> callback);
	void cancelRequest(DASRequest.Cancellation cancellation, AsyncCallback<Void> callback);
}
