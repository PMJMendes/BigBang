package bigBang.library.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExternRequestServiceAsync
	extends Service
{
	void getRequest(String id, AsyncCallback<ExternalInfoRequest> callback);
	void sendInformation(Outgoing outgoing, AsyncCallback<ExternalInfoRequest> callback);
	void receiveAdditional(Incoming response, AsyncCallback<ExternalInfoRequest> callback);
	void closeRequest(Closing closing, AsyncCallback<Void> callback);
}
