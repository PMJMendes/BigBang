package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SignatureRequestServiceAsync
	extends Service
{
	void getRequest(String id, AsyncCallback<SignatureRequest> callback);
	void repeatRequest(SignatureRequest request, AsyncCallback<SignatureRequest> callback);
	void receiveResponse(SignatureRequest.Response response, AsyncCallback<SignatureRequest> callback);
	void cancelRequest(SignatureRequest.Cancellation cancellation, AsyncCallback<Void> callback);
}
