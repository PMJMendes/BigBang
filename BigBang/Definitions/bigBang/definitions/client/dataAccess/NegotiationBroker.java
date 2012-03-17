package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.Negotiation; 
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.definitions.shared.Negotiation.Deletion;

public interface NegotiationBroker extends DataBrokerInterface<Negotiation>{

	void getNegotiation(String negotiationId,
			ResponseHandler<Negotiation> handler);

	void updateNegotiation(Negotiation negotiation,
			ResponseHandler<Negotiation> handler);

	void removeNegotiation(Deletion deletion, ResponseHandler<String> handler);

	void cancelNegotiation(Cancellation cancellation,
			ResponseHandler<Negotiation> handler);

	void createExternalInfoRequest(ExternalInfoRequest request,
			ResponseHandler<ExternalInfoRequest> handler); 
	

}
