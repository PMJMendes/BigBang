package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SignatureRequest;

public interface SignatureRequestBroker extends DataBrokerInterface<SignatureRequest>{

	void getRequest(String id, 
			ResponseHandler<SignatureRequest> handler);
	
	void repeatRequest(SignatureRequest request, 
			ResponseHandler<SignatureRequest> handler);
	
	void receiveResponse(SignatureRequest.Response response, 
			ResponseHandler<SignatureRequest> handler);
	
	void cancelRequest(SignatureRequest.Cancellation cancellation, 
			ResponseHandler<Void> callback);
	
}
