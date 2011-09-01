package bigBang.library.interfaces;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InfoOrDocumentRequestServiceAsync
	extends Service
{
	void repeatRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void receiveResponse(Response response, AsyncCallback<InfoOrDocumentRequest> callback);
	void cancelRequest(Cancellation cancellation, AsyncCallback<Void> callback);
}
