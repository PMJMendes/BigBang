package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DASRequest;

public interface DASRequestBroker extends DataBrokerInterface<DASRequest>{
	
	void getRequest(String id, ResponseHandler<DASRequest> handler);

	void repeatRequest(DASRequest request, ResponseHandler<DASRequest> handler);
	
	void receiveResponse(DASRequest.Response request, ResponseHandler<DASRequest> handler);

	void cancelRequest(DASRequest.Cancellation request, ResponseHandler<Void> handler);
}
