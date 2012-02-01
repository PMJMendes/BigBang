package bigBang.library.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.definitions.shared.ExternalInfoRequest.Incoming;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExternRequestServiceAsync
	extends Service
{
	void getRequest(String id, AsyncCallback<ExternRequestService> callback);
	void repeatRequest(ExternRequestService request, AsyncCallback<ExternRequestService> callback);
	void receiveResponse(Incoming response, AsyncCallback<ExternRequestService> callback);
	void cancelRequest(Closing cancellation, AsyncCallback<Void> callback);
}
